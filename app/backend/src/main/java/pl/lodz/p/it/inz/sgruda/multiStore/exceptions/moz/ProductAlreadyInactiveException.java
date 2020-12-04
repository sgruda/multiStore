package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class ProductAlreadyInactiveException extends AppBaseException {
    static final public String KEY_PRODUCT_ALREADY_INACTIVE = "error.product.already.inactive";

    public ProductAlreadyInactiveException() {
        super(KEY_PRODUCT_ALREADY_INACTIVE);
    }
}
