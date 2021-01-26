package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccountRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.AccountListService;

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
public class AccountListServiceImpl implements AccountListService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountListServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Page<AccountEntity> getFilteredAccounts(String textToSearch, Pageable pageable, Boolean active) {
        if(textToSearch != null) {
            if(active != null)
                return accountRepository.findByTextInNameOrEmailAndFilteredByActive(textToSearch, pageable, active.booleanValue());
            else
                return accountRepository.findByTextInNameOrEmail(textToSearch, pageable);
        } else {
            if(active != null)
                return accountRepository.findAllByActiveEquals(pageable, active.booleanValue());
            else
                return accountRepository.findAll(pageable);
        }
    }
}
