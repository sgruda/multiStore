package pl.lodz.p.it.inz.sgruda.multiStore.security.oauth2;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.OAuth2Service;
import pl.lodz.p.it.inz.sgruda.multiStore.security.UserPrincipal;

@Log
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private OAuth2Service oAuth2Service;

    @Autowired
    public CustomOAuth2UserService(OAuth2Service oAuth2Service) {
        this.oAuth2Service = oAuth2Service;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        AccountEntity account;
        try {
            account = oAuth2Service.updateAccountOAuth2(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User);
        } catch (AccountNotExistsException e) {
            account = oAuth2Service.registerAccountOAuth2(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User);
        }
        return UserPrincipal.create(account, oAuth2User.getAttributes());
    }

}
