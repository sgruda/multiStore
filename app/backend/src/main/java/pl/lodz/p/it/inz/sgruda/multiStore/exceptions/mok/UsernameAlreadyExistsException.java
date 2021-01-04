package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class UsernameAlreadyExistsException extends AppBaseException {
    static final public String KEY_ACCOUNT_USERNAME_PROBLEM = "error.account.username.exists";

    public UsernameAlreadyExistsException() {
        super(KEY_ACCOUNT_USERNAME_PROBLEM);
    }

}