package pl.lodz.p.it.inz.sgruda.multiStore.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.auth.jwt.TokenJWTVariousIDException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccountRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.HashMethod;

@Retryable(
        maxAttempts = 5,
        backoff = @Backoff(delay = 500),
        exclude = {AppBaseException.class}
)
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        transactionManager = "mokTransactionManager",
        timeout = 5
)
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Let people login with either username or email
        AccountEntity account = accountRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with login: " + username)
                );

        return UserPrincipal.create(account);
    }

    // This method is used by JWTAuthenticationFilter
    public UserDetails loadUserByEmailAndCheckId(String email, String hashId) throws TokenJWTVariousIDException {
        AccountEntity account = accountRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found with email : " + email)
        );
        HashMethod hashMethod = new HashMethod();
        if(!hashMethod.hash(String.valueOf(account.getId())).equals(hashId)) {
            throw new TokenJWTVariousIDException();
        }
        return UserPrincipal.create(account);
    }
}
