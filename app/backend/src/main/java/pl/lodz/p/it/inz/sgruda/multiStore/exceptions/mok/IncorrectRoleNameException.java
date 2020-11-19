package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class IncorrectRoleNameException extends AppBaseException {
    static final public String KEY_INCORRECT_ROLE_NAME = "error.incorrect.role.name";

    public IncorrectRoleNameException() {
        super(KEY_INCORRECT_ROLE_NAME);
    }

}
