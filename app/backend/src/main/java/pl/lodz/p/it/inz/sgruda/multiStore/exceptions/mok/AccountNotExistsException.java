package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class AccountNotExistsException extends AppBaseException {
    static final public String KEY_ACCOUNT_NOT_EXISTS = "error.account.not.exists";

    public AccountNotExistsException() {
        super(KEY_ACCOUNT_NOT_EXISTS);
    }

    public AccountNotExistsException(String message) {
        super(message);
    }

    public AccountNotExistsException(Throwable cause) {
        super(KEY_ACCOUNT_NOT_EXISTS, cause);
    }
}