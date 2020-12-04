package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class CurrentOrderStatusIsFinalException extends AppBaseException {
    static final public String KEY_CURRENT_ORDER_STATUS_IS_FINAL = "error.current.order.status.is.final";

    public CurrentOrderStatusIsFinalException() {
        super(KEY_CURRENT_ORDER_STATUS_IS_FINAL);
    }
}
