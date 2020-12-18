package pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.BasketEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderedItemEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.BasketNotExistsException;

import java.util.List;
import java.util.Set;

public interface BasketHandlerService {
    BasketEntity getBasketEntityByOwnerEmail(String ownerMail) throws BasketNotExistsException;
    OrderedItemEntity getOrderedItemEntityOrCreateNew(String identifier, int orderedNumber, String productTitle, ProductEntity orderedProductIfPresent) throws AppBaseException;
    ProductEntity getProductEntityByTitle(String title) throws AppBaseException;
    OrderedItemEntity getOrderedItemEntity(String identifier) throws AppBaseException;
    void editOrderedItemInBasket(OrderedItemEntity orderedItemEntity, BasketEntity basketEntity) throws AppBaseException;
    void addToBasket(Set<OrderedItemEntity> orderedItemEntities, BasketEntity basketEntity);
    void removeFromBasket(Set<OrderedItemEntity> orderedItemEntities, BasketEntity basketEntity);
}
