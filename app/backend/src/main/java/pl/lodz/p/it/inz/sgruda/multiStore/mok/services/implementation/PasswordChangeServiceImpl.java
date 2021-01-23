package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.OperationDisabledForAccountException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccountRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.PasswordChangeService;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.AuthProvider;

@Log
@Service
@Retryable(
        maxAttempts = 5,
        backoff = @Backoff(delay = 500)
)
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        transactionManager = "mokTransactionManager",
        timeout = 5
)
public class PasswordChangeServiceImpl implements PasswordChangeService {
    private AccountRepository accountRepository;

    @Autowired
    public PasswordChangeServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void changePassword(AccountEntity accountEntity, String newEncodedPassword) throws OperationDisabledForAccountException {
        if(accountEntity.getProvider() == AuthProvider.system)
            accountEntity.setPassword(newEncodedPassword);
        else
            throw new OperationDisabledForAccountException();
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AccountEntity getAccountByEmail(String mail) throws AccountNotExistsException {
        return accountRepository.findByEmail(mail)
                .orElseThrow(() -> new AccountNotExistsException());
    }
}