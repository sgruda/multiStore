package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class IncorrectForgotPasswordTokenException extends AppBaseException {
    static final public String KEY_INCORRECT_FORGOT_PASSWORD_TOKEN = "error.incorrect.forgot.password.token.default";

    public IncorrectForgotPasswordTokenException() {
        super(KEY_INCORRECT_FORGOT_PASSWORD_TOKEN);
    }

}
