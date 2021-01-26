package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.EmailAlreadyVerifyException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccountRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.MailVerifierService;

import java.util.Optional;

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
        timeout = 5
)
public class MailVerifierServiceImpl implements MailVerifierService {
    private AccountRepository accountRepository;

    @Autowired
    public MailVerifierServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void verifyEmail(String veryficationToken) throws AccountNotExistsException, EmailAlreadyVerifyException {
        Optional<AccountEntity> optionalAccountEntity= accountRepository.findByVeryficationToken(veryficationToken);
        if(optionalAccountEntity.isPresent()) {
            AccountEntity accountToConfirm = optionalAccountEntity.get();
            if(accountToConfirm.isEmailVerified()) {
                throw new EmailAlreadyVerifyException();
            }
            accountToConfirm.setEmailVerified(true);
        } else {
            throw new AccountNotExistsException();
        }
    }
}
