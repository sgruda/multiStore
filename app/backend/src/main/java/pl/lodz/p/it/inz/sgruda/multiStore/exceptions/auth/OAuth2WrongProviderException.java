package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.auth;

import org.springframework.security.core.AuthenticationException;

public class OAuth2WrongProviderException extends AuthenticationException {
    static final public String KEY_OAUTH2_WRONG_PROVIDER = "error.oauth2.wrong.provider";

    public OAuth2WrongProviderException() {
        super(KEY_OAUTH2_WRONG_PROVIDER);
    }

    public OAuth2WrongProviderException(Throwable t) {
        super(KEY_OAUTH2_WRONG_PROVIDER, t);
    }

    public OAuth2WrongProviderException(String msg) {
        super(msg);
    }
}