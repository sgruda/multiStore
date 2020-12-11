package pl.lodz.p.it.inz.sgruda.multiStore.moz.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.BasketEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderedItemEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop.ProductNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.BasketNotContainsItemException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.BasketNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.OrderedItemNotExistException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.ProductAlreadyInactiveException;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.repositories.BasketRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.repositories.OrderedItemRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.repositories.ProductRepositoryMOZ;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces.BasketHandlerService;

import java.util.List;
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
    public OrderedItemEntity getOrderedItemEntityOrCreateNew(String identifier, int orderedNumber, String productTitle) throws AppBaseException {
        Optional<OrderedItemEntity> optional = orderedItemRepository.findByIdentifier(identifier);
        if(optional.isPresent())
            return optional.get();
        else {
            OrderedItemEntity orderedItemEntity = new OrderedItemEntity();
            orderedItemEntity.setOrderedNumber(orderedNumber);
            ProductEntity productEntity = productRepository.findByTitle(productTitle)
                    .orElseThrow(() -> new ProductNotExistsException());
            if(!productEntity.isActive())
                throw new ProductAlreadyInactiveException();
            orderedItemEntity.setProductEntity(productEntity);
            orderedItemRepository.saveAndFlush(orderedItemEntity);
            return orderedItemEntity;
        }
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
        boolean isRemoved = false;
        for(int i = 0; i < basketEntity.getOrderedItemEntities().size(); i++) {
            if(basketEntity.getOrderedItemEntities().get(i).getIdentifier().equals(orderedItemEntity.getIdentifier())) {
                basketEntity.getOrderedItemEntities().remove(i);
                isRemoved = true;
                break;
            }
        }
        if(isRemoved) {
            basketEntity.getOrderedItemEntities().add(orderedItemEntity);
            orderedItemRepository.saveAndFlush(orderedItemEntity);
        } else
            throw new BasketNotContainsItemException();
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public void addToBasket(List<OrderedItemEntity> orderedItemEntities, BasketEntity basketEntity) {
        basketEntity.getOrderedItemEntities().addAll(orderedItemEntities);
        basketRepository.saveAndFlush(basketEntity);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public void removeFromBasket(List<OrderedItemEntity> orderedItemEntities, BasketEntity basketEntity) {
        basketEntity.getOrderedItemEntities()
                .removeIf(item -> !orderedItemEntities.contains(item));
        basketRepository.saveAndFlush(basketEntity);
    }
}
