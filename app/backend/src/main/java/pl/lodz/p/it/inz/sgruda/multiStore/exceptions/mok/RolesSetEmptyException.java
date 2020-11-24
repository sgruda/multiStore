package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class RolesSetEmptyException  extends AppBaseException {
    static final public String KEY_ROLES_SET_EMPTY_EXCEPTION = "error.roles.set.empty";

    public RolesSetEmptyException() {
        super(KEY_ROLES_SET_EMPTY_EXCEPTION);
    }

}