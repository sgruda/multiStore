package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.OptimisticLockAppException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.OperationDisabledForAccountException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccountRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AuthenticationDataRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.PasswordChangeService;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.AuthProvider;

@Log
@Service
@Retryable(
        maxAttempts = 5,
        backoff = @Backoff(delay = 500),
        exclude = {AppBaseException.class}
)
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        transactionManager = "mokTransactionManager",
        timeout = 5,
        rollbackFor = {OptimisticLockAppException.class}
)
public class PasswordChangeServiceImpl implements PasswordChangeService {
    private AccountRepository accountRepository;
    private AuthenticationDataRepository authenticationDataRepository;

    @Autowired
    public PasswordChangeServiceImpl(AccountRepository accountRepository, AuthenticationDataRepository authenticationDataRepository) {
        this.accountRepository = accountRepository;
        this.authenticationDataRepository = authenticationDataRepository;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void changePassword(AccountEntity accountEntity, String newEncodedPassword) throws OperationDisabledForAccountException, OptimisticLockAppException {
        if(accountEntity.getProvider() == AuthProvider.system) {
            accountEntity.setPassword(newEncodedPassword);
            try {
                accountRepository.saveAndFlush(accountEntity);
                authenticationDataRepository.saveAndFlush(accountEntity.getAuthenticationDataEntity());
            } catch (OptimisticLockingFailureException ex) {
                throw new OptimisticLockAppException();
            }
        } else
            throw new OperationDisabledForAccountException();
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AccountEntity getAccountByEmail(String mail) throws AccountNotExistsException {
        return accountRepository.findByEmail(mail)
                .orElseThrow(() -> new AccountNotExistsException());
    }
}