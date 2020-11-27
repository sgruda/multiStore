package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccessLevelEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.BasketEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.auth.OAuth2AuthenticationProcessingException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.auth.OAuth2WrongProviderException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.http.HttpBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccessLevelRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccountRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.OAuth2Service;
import pl.lodz.p.it.inz.sgruda.multiStore.security.oauth2.user.OAuth2UserInfo;
import pl.lodz.p.it.inz.sgruda.multiStore.security.oauth2.user.OAuth2UserInfoFactory;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.AuthProvider;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.RoleName;

import java.util.Collections;
import java.util.Optional;

@Log
@Service
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        timeout = 5
)
public class OAuth2ServiceImpl implements OAuth2Service {
    private AccountRepository accountRepository;
    private AccessLevelRepository accessLevelRepository;

    @Autowired
    public OAuth2ServiceImpl(AccountRepository accountRepository, AccessLevelRepository accessLevelRepository) {
        this.accountRepository = accountRepository;
        this.accessLevelRepository = accessLevelRepository;
    }

    @Override
    public AccountEntity registerAccountOAuth2(String registrationId, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException();
        }

        AccountEntity account = new AccountEntity(
                oAuth2UserInfo.getFirstName(),
                oAuth2UserInfo.getLastName(),
                oAuth2UserInfo.getEmail(),
                AuthProvider.valueOf(registrationId),
                oAuth2UserInfo.getId()
        );
        AccessLevelEntity clientRole = accessLevelRepository.findByRoleName(RoleName.ROLE_CLIENT)
                .orElseThrow(() -> new HttpBaseException("User Role not set."));

        account.setAccessLevelEntities(Collections.singleton(clientRole));

        BasketEntity basketEntity = new BasketEntity(account);
        account.setBasketEntity(basketEntity);

        return accountRepository.save(account);
    }

    @Override
    public AccountEntity updateAccountOAuth2(String registrationId, OAuth2User oAuth2User) throws AccountNotExistsException {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException();
        }

        Optional<AccountEntity> accountOptional = accountRepository.findByEmail(oAuth2UserInfo.getEmail());
        AccountEntity existingAccount;
        if(!accountOptional.isPresent())
            throw new AccountNotExistsException();
        existingAccount = accountOptional.get();
        if (!existingAccount.getProvider().equals(AuthProvider.valueOf(registrationId))) {
            throw new OAuth2WrongProviderException();
        }
        existingAccount.setFirstName(oAuth2UserInfo.getFirstName());
        existingAccount.setLastName(oAuth2UserInfo.getLastName());
//        existingAccount.setEmail(oAuth2UserInfo.getEmail());

        return accountRepository.save(existingAccount);
    }
}
