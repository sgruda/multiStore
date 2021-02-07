package pl.lodz.p.it.inz.sgruda.multiStore.moz.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.BasketEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderedItemEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.OptimisticLockAppException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop.ProductNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.BasketNotContainsItemException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.BasketNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.OrderedItemNotExistException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.ProductAlreadyInactiveException;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.repositories.BasketRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.repositories.OrderedItemRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.repositories.ProductRepositoryMOZ;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces.BasketHandlerService;

import java.util.Optional;
import java.util.Set;

@Log
@Service
@Retryable(
        maxAttempts = 5,
        backoff = @Backoff(delay = 500),
        exclude = {AppBaseException.class}
)
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        transactionManager = "mozTransactionManager",
        timeout = 5,
        rollbackFor = {OptimisticLockAppException.class}
)
public class BasketHandlerServiceImpl implements BasketHandlerService {
    private BasketRepository basketRepository;
    private OrderedItemRepository orderedItemRepository;
    private ProductRepositoryMOZ productRepository;

    @Autowired
    public BasketHandlerServiceImpl(BasketRepository basketRepository, OrderedItemRepository orderedItemRepository, ProductRepositoryMOZ productRepository) {
        this.basketRepository = basketRepository;
        this.orderedItemRepository = orderedItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public BasketEntity getBasketEntityByOwnerEmail(String ownerMail) throws BasketNotExistsException {
        return basketRepository.findByAccountEntityEmail(ownerMail)
                .orElseThrow(() -> new BasketNotExistsException());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public OrderedItemEntity getOrderedItemEntityOrCreateNew(String identifier, int orderedNumber, String productTitle,
                                                             ProductEntity orderedProductIfPresent) throws AppBaseException {
        Optional<OrderedItemEntity> optional = orderedItemRepository.findByIdentifier(identifier);
        if(optional.isPresent())
            return optional.get();
        else {
            OrderedItemEntity orderedItemEntity = new OrderedItemEntity();
            orderedItemEntity.setOrderedNumber(orderedNumber);
            ProductEntity productEntity;
            if(orderedProductIfPresent != null)
                productEntity = orderedProductIfPresent;
            else
                productEntity = productRepository.findByTitle(productTitle)
                    .orElseThrow(() -> new ProductNotExistsException());
            if(!productEntity.isActive())
                throw new ProductAlreadyInactiveException();
            orderedItemEntity.setProductEntity(productEntity);
            try{
                orderedItemRepository.saveAndFlush(orderedItemEntity);
            }
            catch(OptimisticLockingFailureException ex){
                throw new OptimisticLockAppException();
            }
            return orderedItemEntity;
        }
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ProductEntity getProductEntityByTitle(String title) throws AppBaseException {
        return productRepository.findByTitle(title)
                .orElseThrow(() -> new ProductNotExistsException());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public OrderedItemEntity getOrderedItemEntity(String identifier) throws AppBaseException {
        return orderedItemRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new OrderedItemNotExistException());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public void editOrderedItemInBasket(OrderedItemEntity orderedItemEntity, BasketEntity basketEntity) throws AppBaseException {
        if(!basketEntity.getOrderedItemEntities()
                .removeIf(item -> item.getIdentifier().equals(orderedItemEntity.getIdentifier())))
            throw new BasketNotContainsItemException();
        basketEntity.getOrderedItemEntities().add(orderedItemEntity);
        try{
            orderedItemRepository.saveAndFlush(orderedItemEntity);
            basketRepository.saveAndFlush(basketEntity);
        }
        catch(OptimisticLockingFailureException ex){
            throw new OptimisticLockAppException();
        }
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public void addToBasket(Set<OrderedItemEntity> orderedItemEntities, BasketEntity basketEntity) throws OptimisticLockAppException {
        for(OrderedItemEntity entity : orderedItemEntities)
            if(!basketEntity.getOrderedItemEntities().contains(entity))
                basketEntity.getOrderedItemEntities().add(entity);
        try{
            basketRepository.saveAndFlush(basketEntity);
        }
        catch(OptimisticLockingFailureException ex){
            throw new OptimisticLockAppException();
        }
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public void removeFromBasket(Set<OrderedItemEntity> orderedItemEntities, BasketEntity basketEntity) throws OptimisticLockAppException {
        basketEntity.getOrderedItemEntities()
                .removeIf(item -> !orderedItemEntities.contains(item));
        try{
            basketRepository.saveAndFlush(basketEntity);
        }
        catch(OptimisticLockingFailureException ex){
            throw new OptimisticLockAppException();
        }
    }
}
