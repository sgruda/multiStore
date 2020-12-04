package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class NotEnoughProductsException extends AppBaseException {
    static final public String KEY_PRODUCT_NOT_ENOUGH = "error.product.not.enough";

    public NotEnoughProductsException() {
        super(KEY_PRODUCT_NOT_ENOUGH);
    }
}