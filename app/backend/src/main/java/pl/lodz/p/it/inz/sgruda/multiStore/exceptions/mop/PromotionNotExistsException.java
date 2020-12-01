package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class PromotionNotExistsException extends AppBaseException {
    static final public String KEY_PROMOTION_NOT_EXISTS = "error.promotion.not.exists";

    public PromotionNotExistsException() {
        super(KEY_PROMOTION_NOT_EXISTS);
    }
}
