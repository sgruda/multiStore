package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.AppBaseException;

public class EmailAlreadyExistsException  extends AppBaseException {
    static final public String KEY_ACCOUNT_EMAIL_EXISTS = "error.account.email.exists";

    public EmailAlreadyExistsException() {
        super(KEY_ACCOUNT_EMAIL_EXISTS);
    }

    public EmailAlreadyExistsException(String message) {
        super(message);
    }

    public EmailAlreadyExistsException(Throwable cause) {
        super(KEY_ACCOUNT_EMAIL_EXISTS, cause);
    }
}