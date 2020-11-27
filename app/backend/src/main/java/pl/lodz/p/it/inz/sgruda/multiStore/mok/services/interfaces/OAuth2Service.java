package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces;

import org.springframework.security.oauth2.core.user.OAuth2User;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;

public interface OAuth2Service {
    AccountEntity registerAccountOAuth2(String registrationId, OAuth2User oAuth2User);
    AccountEntity updateAccountOAuth2(String registrationId, OAuth2User oAuth2User) throws AccountNotExistsException;
}
