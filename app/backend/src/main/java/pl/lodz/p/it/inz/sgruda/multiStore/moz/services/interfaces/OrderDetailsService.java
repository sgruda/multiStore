package pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.OrderNotExistsException;

public interface OrderDetailsService {
    OrderEntity getOrderByIdentifier(String identifier) throws OrderNotExistsException;
}
