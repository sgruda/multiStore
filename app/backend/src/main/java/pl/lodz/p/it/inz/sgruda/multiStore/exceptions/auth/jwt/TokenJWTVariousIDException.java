package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.auth.jwt;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class TokenJWTVariousIDException extends AppBaseException {
    static final public String KEY_JWT_VARIOUS_ID = "error.jwt.various.id";

    public TokenJWTVariousIDException() {
        super(KEY_JWT_VARIOUS_ID);
    }

    public TokenJWTVariousIDException(String message) {
        super(message);
    }

    public TokenJWTVariousIDException(Throwable cause) {
        super(KEY_JWT_VARIOUS_ID, cause);
    }
}