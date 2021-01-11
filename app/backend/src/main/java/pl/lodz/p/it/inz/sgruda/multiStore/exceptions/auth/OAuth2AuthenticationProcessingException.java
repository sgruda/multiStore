package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.auth;

import org.springframework.security.core.AuthenticationException;

public class OAuth2AuthenticationProcessingException extends AuthenticationException {
    static final public String KEY_OAUTH2_AUTHENTICATION_PROCESSING = "error.oauth2.authentication.processing";//Email not found from OAuth2 provider

    public OAuth2AuthenticationProcessingException() {
        super(KEY_OAUTH2_AUTHENTICATION_PROCESSING);
    }

    public OAuth2AuthenticationProcessingException(String msg) {
        super(msg);
    }
}
