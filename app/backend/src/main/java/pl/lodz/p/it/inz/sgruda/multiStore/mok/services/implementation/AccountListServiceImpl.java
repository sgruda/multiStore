package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccountRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.AccountListService;

import java.util.List;

@Log
@Service
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        timeout = 5
)
public class AccountListServiceImpl implements AccountListService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountListServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Page<AccountEntity> getAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    @Override
    public Page<AccountEntity> getAccountsByTextInNameOrEmail(String textToSearch, Pageable pageable) {
        return accountRepository.findByTextInNameOrEmail(textToSearch, pageable);
    }

    @Override
    public List<AccountEntity> getAccountsByTextInNameOrEmail(String textToSearch, Sort sort) {
        return accountRepository.findByTextInNameOrEmail(textToSearch, sort);
    }

    @Override
    public Page<AccountEntity> getAccountsByActive(boolean active, Pageable pageable) {
        return accountRepository.findByActive(active, pageable);
    }
}
