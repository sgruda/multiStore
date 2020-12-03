package pl.lodz.p.it.inz.sgruda.multiStore.moz.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.PromotionEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.BasketEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderedItemEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop.ProductNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.*;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.repositories.*;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces.OrderSubmitService;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.StatusName;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    private BasketRepository basketRepository;
    private ProductRepositoryMOZ productRepository;
    private PromotionRepositoryMOZ promotionRepository;
    private AccountRepositoryMOZ accountRepository;

    @Autowired
    public OrderSubmitServiceImpl(OrderRepository orderRepository, OrderedItemRepository orderedItemRepository,
                                  StatusRepository statusRepository, BasketRepository basketRepository,
                                  ProductRepositoryMOZ productRepository,
                                  PromotionRepositoryMOZ promotionRepository, AccountRepositoryMOZ accountRepository) {
        this.orderRepository = orderRepository;
        this.orderedItemRepository = orderedItemRepository;
        this.statusRepository = statusRepository;
        this.basketRepository = basketRepository;
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public AccountEntity getAccountEntityByEmail(String email) throws AccountNotExistsException {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotExistsException());
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
    public BasketEntity getBasketEntity(String ownerEmail) throws BasketNotExistsException {
        return basketRepository.findByAccountEntity_Email(ownerEmail)
                .orElseThrow(() -> new BasketNotExistsException());
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
                        return item.getOrderedNumber() * item.getProductEntity().getPrice() * (1.0 - discount/100.0);
                    }
                )
                .sum();
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public void createOrder(BasketEntity basketEntity) throws AppBaseException {
        OrderEntity orderEntity = new OrderEntity();

        orderEntity.setOrderDate(LocalDateTime.now());
        orderEntity.setAccountEntity(basketEntity.getAccountEntity());
        orderEntity.setOrderedItemEntities(basketEntity.getOrderedItemEntities());
        orderEntity.setTotalPrice(this.calcPrice(orderEntity.getOrderedItemEntities()));
        orderEntity.setStatusEntity(statusRepository.findByStatusName(StatusName.submitted)
                                                    .orElseThrow(() -> new StatusNotExistsException())
        );

        for(OrderedItemEntity itemEntity : orderEntity.getOrderedItemEntities()) {
            if(!itemEntity.getProductEntity().isActive())
                throw new ProductAlreadyInactiveException();
            ProductEntity orderedProduct = productRepository.findByTitle(itemEntity.getProductEntity().getTitle()).get();
            if(orderedProduct.getInStore() < itemEntity.getOrderedNumber())
                throw new NotEnoughProductsException();
            else
                orderedProduct.setInStore(orderedProduct.getInStore() - itemEntity.getOrderedNumber());

        }

        orderRepository.saveAndFlush(orderEntity);
    }
}
