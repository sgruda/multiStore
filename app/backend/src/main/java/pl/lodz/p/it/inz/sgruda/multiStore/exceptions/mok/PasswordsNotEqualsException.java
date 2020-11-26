package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class PasswordsNotEqualsException extends AppBaseException {
    static final public String KEY_PASSWORDS_NOT_EQUALS = "error.passwords.not.equals";

    public PasswordsNotEqualsException() {
        super(KEY_PASSWORDS_NOT_EQUALS);
    }

}