package pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.BasketEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderedItemEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop.ProductNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.OrderedItemNotExistException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.StatusNotExistsException;

import java.util.Set;

public interface OrderSubmitService {
    ProductEntity getProductEntityByTitle(String title) throws ProductNotExistsException;
    OrderedItemEntity getOrderedItemsEntityByIdentifier(String indentifier) throws OrderedItemNotExistException;
    OrderedItemEntity joinOrderedItemsEntityWithProductEntity(OrderedItemEntity orderedItemEntity, ProductEntity productEntity);
    double calcPrice(Set<OrderedItemEntity> orderedItems);
    void createOrder(BasketEntity basketEntity) throws StatusNotExistsException;
}
