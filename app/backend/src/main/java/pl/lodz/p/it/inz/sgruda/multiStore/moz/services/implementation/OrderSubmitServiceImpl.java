package pl.lodz.p.it.inz.sgruda.multiStore.moz.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
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
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop.ProductNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.OrderedItemNotExistException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.StatusNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.repositories.*;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces.OrderSubmitService;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.StatusName;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Log
@Service
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
    private ProductRepositoryMOZ productRepository;
    private PromotionRepositoryMOZ promotionRepository;

    @Autowired
    public OrderSubmitServiceImpl(OrderRepository orderRepository, OrderedItemRepository orderedItemRepository,
                                  StatusRepository statusRepository, ProductRepositoryMOZ productRepository, PromotionRepositoryMOZ promotionRepository) {
        this.orderRepository = orderRepository;
        this.orderedItemRepository = orderedItemRepository;
        this.statusRepository = statusRepository;
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ProductEntity getProductEntityByTitle(String title) throws ProductNotExistsException {
        return productRepository.findByTitle(title)
                .orElseThrow(() -> new ProductNotExistsException());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public OrderedItemEntity getOrderedItemsEntityByIdentifier(String indentifier) throws OrderedItemNotExistException {
        return orderedItemRepository.findByIdentifier(indentifier)
                .orElseThrow(() -> new OrderedItemNotExistException());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public OrderedItemEntity joinOrderedItemsEntityWithProductEntity(OrderedItemEntity orderedItemEntity, ProductEntity productEntity) {
        orderedItemEntity.setProductEntity(productEntity);
        return orderedItemEntity;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public double calcPrice(Set<OrderedItemEntity> orderedItems) {
        return orderedItems.stream()
                .mapToDouble(
                    item -> {
                        Collection<PromotionEntity> promotionEntities = promotionRepository.findByCategoryEntity(item.getProductEntity().getCategoryEntity());
                        double discount = 0;
                        if(promotionEntities.size() > 0) {
                            discount = promotionEntities.stream()
                                    .mapToDouble(promo -> promo.isActive() ? promo.getDiscount() : 0.0)
                                    .sum();
                            if(promotionEntities.size() > 1 && discount > 0.5)
                                discount = 0.5;
                        }
                        return item.getOrderedNumber() * item.getProductEntity().getPrice() * (1.0 - discount);
                    }
                )
                .sum();
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public void createOrder(BasketEntity basketEntity) throws StatusNotExistsException {
        OrderEntity orderEntity = new OrderEntity();

        orderEntity.setOrderDate(LocalDateTime.now());
        orderEntity.setAccountEntity(basketEntity.getAccountEntity());
        orderEntity.setOrderedItemsEntities(basketEntity.getOrderedItemsEntities());
        orderEntity.setTotalPrice(this.calcPrice(orderEntity.getOrderedItemsEntities()));
        orderEntity.setStatusEntity(statusRepository.findByStatusName(StatusName.submitted)
                                                    .orElseThrow(() -> new StatusNotExistsException())
        );

        orderRepository.saveAndFlush(orderEntity);
    }
}
