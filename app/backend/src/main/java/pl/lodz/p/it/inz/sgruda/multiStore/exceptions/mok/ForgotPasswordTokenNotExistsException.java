package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class ForgotPasswordTokenNotExistsException  extends AppBaseException {
    static final public String KEY_FORGOT_PASSWORD_TOKEN_NOT_EXISTS = "error.forgot.password.token.not.exists";

    public ForgotPasswordTokenNotExistsException() {
        super(KEY_FORGOT_PASSWORD_TOKEN_NOT_EXISTS);
    }

}
