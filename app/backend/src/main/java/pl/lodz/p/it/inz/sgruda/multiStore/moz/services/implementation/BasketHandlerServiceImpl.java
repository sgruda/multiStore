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
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.BasketNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.ProductAlreadyInactiveException;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.repositories.BasketRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.repositories.OrderedItemRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.repositories.ProductRepositoryMOZ;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces.BasketHandlerService;

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
        return basketRepository.findByAccountEntity_Email(ownerMail)
                .orElseThrow(() -> new BasketNotExistsException());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public OrderedItemEntity getOrderedItemsEntityOrCreateNew(String identifier, int orderedNumber, String productTitle) throws AppBaseException {
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
    public void addToBasket(Set<OrderedItemEntity> orderedItemEntities, BasketEntity basketEntity) {
        basketEntity.getOrderedItemEntities().addAll(orderedItemEntities);
        basketRepository.saveAndFlush(basketEntity);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public void removeFromBasket(Set<OrderedItemEntity> orderedItemEntities, BasketEntity basketEntity) {
        basketEntity.getOrderedItemEntities()
                .removeIf(item -> !orderedItemEntities.contains(item));
        basketRepository.saveAndFlush(basketEntity);
    }
}
