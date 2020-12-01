package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class CategoryNotExistsException extends AppBaseException {
    static final public String KEY_CATEGORY_NOT_EXISTS = "error.category.not.exists";

    public CategoryNotExistsException() {
        super(KEY_CATEGORY_NOT_EXISTS);
    }
}
