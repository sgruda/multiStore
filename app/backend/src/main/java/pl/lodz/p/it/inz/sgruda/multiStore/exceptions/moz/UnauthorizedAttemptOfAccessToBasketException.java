package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class UnauthorizedAttemptOfAccessToBasketException extends AppBaseException {
    static final public String KEY_UNAUTHORIZED_ATTEMPT_OF_ACCESS_TO_BASKET = "error.basket.unauthorized.attempt.of.access";

    public UnauthorizedAttemptOfAccessToBasketException() {
        super(KEY_UNAUTHORIZED_ATTEMPT_OF_ACCESS_TO_BASKET);
    }
}
