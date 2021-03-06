package pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.BasketEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderedItemEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.BasketNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.OrderedItemNotExistException;

import java.util.Set;

public interface OrderSubmitService {
    OrderedItemEntity getOrderedItemsEntityByIdentifier(String indentifier) throws OrderedItemNotExistException;
    BasketEntity getBasketEntity(String ownerEmail) throws BasketNotExistsException;
    double calcPrice(Set<OrderedItemEntity> orderedItems, String askerEmail);
    void createOrder(BasketEntity basketEntity, String address) throws AppBaseException;
}
