package pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.BasketEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderedItemEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop.ProductNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.BasketNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.OrderedItemNotExistException;

import java.util.Set;

public interface OrderSubmitService {
    AccountEntity getAccountEntityByEmail(String email) throws AccountNotExistsException;
    ProductEntity getProductEntityByTitle(String title) throws ProductNotExistsException;
    OrderedItemEntity getOrderedItemsEntityByIdentifier(String indentifier) throws OrderedItemNotExistException;
    OrderedItemEntity joinOrderedItemsEntityWithProductEntity(OrderedItemEntity orderedItemEntity, ProductEntity productEntity);
    BasketEntity getBasketEntity(String ownerEmail) throws BasketNotExistsException;
    double calcPrice(Set<OrderedItemEntity> orderedItems);
    void createOrder(BasketEntity basketEntity) throws AppBaseException;
}
