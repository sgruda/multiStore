package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.auth.jwt;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class TokenJWTHasBeenExpiredException extends AppBaseException {
    static final public String KEY_JWT_EXPIRED = "error.jwt.expired";

    public TokenJWTHasBeenExpiredException() {
        super(KEY_JWT_EXPIRED);
    }

}
