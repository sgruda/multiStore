package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccessLevelEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.BasketEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.http.HttpBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.EmailAlreadyExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.UsernameAlreadyExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccessLevelRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccountRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.AuthService;
import pl.lodz.p.it.inz.sgruda.multiStore.security.TokenJWTService;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.RoleName;

import java.util.Collections;

@Log
@Service
@Retryable(
        maxAttempts = 5,
        backoff = @Backoff(delay = 2500)
)
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        transactionManager = "mokTransactionManager",
        timeout = 5
)
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;
    private TokenJWTService tokenService;
    private AccountRepository accountRepository;
    private AccessLevelRepository accessLevelRepository;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, TokenJWTService tokenService, AccountRepository accountRepository, AccessLevelRepository accessLevelRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.accountRepository = accountRepository;
        this.accessLevelRepository = accessLevelRepository;
    }

    @Override
    public String authenticateAccount(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenService.generateToken(authentication);
    }

    @Override
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
        BasketEntity basketEntity = new BasketEntity(account);
        account.setBasketEntity(basketEntity);
        return accountRepository.saveAndFlush(account);
    }
}
