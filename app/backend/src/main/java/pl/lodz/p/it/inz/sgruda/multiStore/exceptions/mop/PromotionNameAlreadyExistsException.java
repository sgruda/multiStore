package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class PromotionNameAlreadyExistsException extends AppBaseException {
    static final public String KEY_PROMOTION_NAME_EXISTS = "error.promotion.name.exists";

    public PromotionNameAlreadyExistsException() {
        super(KEY_PROMOTION_NAME_EXISTS);
    }
}
