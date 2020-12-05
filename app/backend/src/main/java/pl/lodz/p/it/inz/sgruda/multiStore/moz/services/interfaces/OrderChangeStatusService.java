package pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.OrderNotExistsException;

public interface OrderChangeStatusService {
    OrderEntity getOrderByIdentifier(String identifier) throws OrderNotExistsException;
    void changeStatus(OrderEntity orderEntity) throws AppBaseException;
}
