package pl.lodz.p.it.inz.sgruda.multiStore.moz.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.PromotionEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.BasketEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderedItemEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.*;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.repositories.*;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces.OrderSubmitService;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.StatusName;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Log
@Service
@Retryable(
        maxAttempts = 5,
        backoff = @Backoff(delay = 500)
)
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        transactionManager = "mozTransactionManager",
        timeout = 5
)
public class OrderSubmitServiceImpl implements OrderSubmitService {
    private OrderRepository orderRepository;
    private OrderedItemRepository orderedItemRepository;
    private StatusRepository statusRepository;
    private BasketRepository basketRepository;
    private ProductRepositoryMOZ productRepository;
    private PromotionRepositoryMOZ promotionRepository;

    @Autowired
    public OrderSubmitServiceImpl(OrderRepository orderRepository, OrderedItemRepository orderedItemRepository,
                                  StatusRepository statusRepository, BasketRepository basketRepository,
                                  ProductRepositoryMOZ productRepository,
                                  PromotionRepositoryMOZ promotionRepository) {
        this.orderRepository = orderRepository;
        this.orderedItemRepository = orderedItemRepository;
        this.statusRepository = statusRepository;
        this.basketRepository = basketRepository;
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public OrderedItemEntity getOrderedItemsEntityByIdentifier(String indentifier) throws OrderedItemNotExistException {
        return orderedItemRepository.findByIdentifier(indentifier)
                .orElseThrow(() -> new OrderedItemNotExistException());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public BasketEntity getBasketEntity(String ownerEmail) throws BasketNotExistsException {
        return basketRepository.findByAccountEntityEmail(ownerEmail)
                .orElseThrow(() -> new BasketNotExistsException());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public double calcPrice(Set<OrderedItemEntity> orderedItems) {
        return new BigDecimal(orderedItems.stream()
                .mapToDouble(
                        item -> {
                            Collection<PromotionEntity> promotionEntities = promotionRepository.findByCategoryEntity(item.getProductEntity().getCategoryEntity());
                            double discount = 0;
                            if(promotionEntities.size() > 0) {
                                discount = promotionEntities.stream()
                                        .mapToDouble(promo -> promo.isActive() ? promo.getDiscount() : 0.0)
                                        .sum();
                                if(promotionEntities.size() > 1 && discount > 50)
                                    discount = 50;
                            }
                            return item.getOrderedNumber() * item.getProductEntity().getPrice() * (1.0 - discount/100.0);
                        }
                )
                .sum()
        ).setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public void createOrder(BasketEntity basketEntity, String address) throws AppBaseException {
        OrderEntity orderEntity = new OrderEntity();

        orderEntity.setOrderDate(LocalDateTime.now());
        orderEntity.setAccountEntity(basketEntity.getAccountEntity());
        orderEntity.setOrderedItemEntities(basketEntity.getOrderedItemEntities());
        orderEntity.setTotalPrice(this.calcPrice(orderEntity.getOrderedItemEntities()));
        orderEntity.setStatusEntity(statusRepository.findByStatusName(StatusName.submitted)
                                                    .orElseThrow(() -> new StatusNotExistsException())
        );
        orderEntity.setAddress(address);

        for(OrderedItemEntity itemEntity : orderEntity.getOrderedItemEntities()) {
            if(!itemEntity.getProductEntity().isActive())
                throw new ProductAlreadyInactiveException();
            ProductEntity orderedProduct = productRepository.findByTitle(itemEntity.getProductEntity().getTitle()).get();
            if(orderedProduct.getInStore() < itemEntity.getOrderedNumber())
                throw new NotEnoughProductsException();
            else
                orderedProduct.setInStore(orderedProduct.getInStore() - itemEntity.getOrderedNumber());
        }
        basketEntity.setOrderedItemEntities(null);

        basketRepository.saveAndFlush(basketEntity);
        orderRepository.saveAndFlush(orderEntity);
    }
}
