package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class ProductNotExistsException extends AppBaseException {
    static final public String KEY_PRODUCT_NOT_EXISTS = "error.product.not.exists";

    public ProductNotExistsException() {
        super(KEY_PRODUCT_NOT_EXISTS);
    }

    public ProductNotExistsException(String message) {
        super(message);
    }

    public ProductNotExistsException(Throwable cause) {
        super(KEY_PRODUCT_NOT_EXISTS, cause);
    }
}