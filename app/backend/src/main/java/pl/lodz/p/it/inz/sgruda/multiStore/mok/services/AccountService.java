package pl.lodz.p.it.inz.sgruda.multiStore.mok.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccessLevelEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.auth.OAuth2WrongProviderException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.auth.OAuth2AuthenticationProcessingException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.http.HttpBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.EmailAlreadyExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.EmailAlreadyVerifyException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.UsernameAlreadyExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccessLevelRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccountRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.security.TokenJWTService;
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
public class AccountService {
    private @Autowired AuthenticationManager authenticationManager;
    private @Autowired TokenJWTService tokenService;
    private @Autowired AccountRepository accountRepository;
    private @Autowired AccessLevelRepository accessLevelRepository;
    private @Autowired PasswordEncoder passwordEncoder;


    public String authenticateAccount(String username, String password) {
         Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return  tokenService.generateToken(authentication);
    }
    public AccountEntity registerAccount(AccountEntity account) throws EmailAlreadyExistsException, UsernameAlreadyExistsException {
        if(accountRepository.existsByUsername(account.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }
        if(accountRepository.existsByEmail(account.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        AccessLevelEntity clientRole = accessLevelRepository.findByRoleName(RoleName.ROLE_CLIENT)
                .orElseThrow(() -> new HttpBaseException("User Role not set."));

        account.setAccessLevelEntities(Collections.singleton(clientRole));
        return accountRepository.save(account);
    }
    public void verifyEmail(String veryficationToken) throws AccountNotExistsException, EmailAlreadyVerifyException {
        Optional<AccountEntity> optionalAccountEntity= accountRepository.findByVeryficationToken(veryficationToken);
        if(optionalAccountEntity.isPresent()) {
            AccountEntity accountToConfirm = optionalAccountEntity.get();
            if(accountToConfirm.isEmailVerified()) {
                throw new EmailAlreadyVerifyException();
            }
            accountToConfirm.setEmailVerified(true);
            accountRepository.save(accountToConfirm);
        } else {
            throw new AccountNotExistsException();
        }
    }
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

        return accountRepository.save(account);
    }
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
