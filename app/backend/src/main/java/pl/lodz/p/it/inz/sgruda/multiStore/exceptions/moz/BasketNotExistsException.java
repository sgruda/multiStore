package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class BasketNotExistsException extends AppBaseException {
    static final public String KEY_BASKET_NOT_EXIST = "error.basket.not.exists";

    public BasketNotExistsException() {
        super(KEY_BASKET_NOT_EXIST);
    }
}