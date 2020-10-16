package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.AppBaseException;

public class EmailAlreadyVerifyException extends AppBaseException {
    static final public String KEY_ACCOUNT_EMAIL_VERIFIED = "error.account.email.verified";

    public EmailAlreadyVerifyException() {
        super(KEY_ACCOUNT_EMAIL_VERIFIED);
    }

    public EmailAlreadyVerifyException(String message) {
        super(message);
    }

    public EmailAlreadyVerifyException(Throwable cause) {
        super(KEY_ACCOUNT_EMAIL_VERIFIED, cause);
    }
}