package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class PromotionIsActiveException extends AppBaseException {
    static final public String KEY_PROMOTION_IS_ACTIVE = "error.promotion.is.active";

    public PromotionIsActiveException() {
        super(KEY_PROMOTION_IS_ACTIVE);
    }
}
