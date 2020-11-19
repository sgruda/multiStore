package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class RemovingAllAccessLevelsException extends AppBaseException {
    static final public String KEY_ACCOUNT_REMOVE_ALL_ACCESS_LEVELS = "error.account.remove.all.access.levels";

    public RemovingAllAccessLevelsException() {
        super(KEY_ACCOUNT_REMOVE_ALL_ACCESS_LEVELS);
    }
}