package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class BasketNotContainsItemException extends AppBaseException {
    static final public String KEY_BASKET_NOT_CONTAINS = "error.basket.not.contains.item";

    public BasketNotContainsItemException() {
        super(KEY_BASKET_NOT_CONTAINS);
    }
}
