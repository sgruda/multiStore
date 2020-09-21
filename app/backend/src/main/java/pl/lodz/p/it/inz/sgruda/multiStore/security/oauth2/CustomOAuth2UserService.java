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
import org.springframework.util.StringUtils;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.OAuth2AuthenticationProcessingException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccountRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.security.UserPrincipal;
import pl.lodz.p.it.inz.sgruda.multiStore.security.oauth2.user.OAuth2UserInfo;
import pl.lodz.p.it.inz.sgruda.multiStore.security.oauth2.user.OAuth2UserInfoFactory;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.AuthProvider;

import java.util.Optional;
@Log
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private AccountRepository accountRepository;

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
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
        Optional<AccountEntity> accountOptional = accountRepository.findByEmail(oAuth2UserInfo.getEmail());
        AccountEntity account;
        if(accountOptional.isPresent()) {
            account = accountOptional.get();
            log.severe("WTF++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            log.severe("account.getProvider() = " + account.getProvider());
            log.severe("oAuth2UserRequest.getClientRegistration().getRegistrationId() = " + oAuth2UserRequest.getClientRegistration().getRegistrationId());
            log.severe("AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId())) = " + AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
            log.severe("if = " + !account.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId())));
            if(!account.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        account.getProvider() + " account. Please use your " + account.getProvider() +
                        " account to login.");
            }
            account = updateExistingUser(account, oAuth2UserInfo);
        } else {
            account = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(account, oAuth2User.getAttributes());
    }

    private AccountEntity registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        AccountEntity account = new AccountEntity();
        account.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()));
        account.setProviderId(oAuth2UserInfo.getId());
        account.setFirstname(oAuth2UserInfo.getFirstname());
        account.setLastname(oAuth2UserInfo.getLastname());
        account.setEmail(oAuth2UserInfo.getEmail());
        log.severe("WTF  registerNewUser account = " + account.toString());
//        account.setImageUrl(oAuth2UserInfo.getImageUrl());
        return accountRepository.save(account);
    }

    private AccountEntity updateExistingUser(AccountEntity existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setFirstname(oAuth2UserInfo.getFirstname());
        existingUser.setLastname(oAuth2UserInfo.getLastname());
//        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return accountRepository.save(existingUser);
    }

}
