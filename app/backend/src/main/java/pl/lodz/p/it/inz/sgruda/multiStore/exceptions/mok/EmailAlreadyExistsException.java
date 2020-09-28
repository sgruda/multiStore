package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.AppBaseException;

public class EmailAlreadyExistsException  extends AppBaseException {
    static final public String KEY_ACCOUNT_EMAIL_PROBLEM = "error.account.email.exists";

    public EmailAlreadyExistsException() {
        super(KEY_ACCOUNT_EMAIL_PROBLEM);
    }

    public EmailAlreadyExistsException(String message) {
        super(message);
    }

    public EmailAlreadyExistsException(Throwable cause) {
        super(KEY_ACCOUNT_EMAIL_PROBLEM, cause);
    }
}