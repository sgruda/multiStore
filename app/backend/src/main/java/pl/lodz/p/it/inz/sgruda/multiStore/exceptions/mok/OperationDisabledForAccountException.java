package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class OperationDisabledForAccountException extends AppBaseException {
    static final public String KEY_ACCOUNT_OPERATION_DISABLED_OAUTH2 = "error.account.operation.disabled.oauth2";

    public OperationDisabledForAccountException() {
        super(KEY_ACCOUNT_OPERATION_DISABLED_OAUTH2);
    }

}