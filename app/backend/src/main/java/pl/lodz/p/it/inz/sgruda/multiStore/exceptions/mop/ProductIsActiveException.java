package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class ProductIsActiveException extends AppBaseException {
    static final public String KEY_PRODUCT_IS_ACTIVE = "error.product.is.active";

    public ProductIsActiveException() {
        super(KEY_PRODUCT_IS_ACTIVE);
    }
}
