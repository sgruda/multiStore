package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class TitleAlreadyExistsException extends AppBaseException {
    static final public String KEY_PRODUCT_TITLE_EXISTS = "error.product.title.exists";

    public TitleAlreadyExistsException() {
        super(KEY_PRODUCT_TITLE_EXISTS);
    }
}
