package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class OrderNotExistsException extends AppBaseException {
    static final public String KEY_ORDER_NOT_EXISTS = "error.order.not.exists";

    public OrderNotExistsException() {
        super(KEY_ORDER_NOT_EXISTS);
    }
}