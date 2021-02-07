package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.OptimisticLockAppException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.EmailAlreadyVerifyException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccountRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AuthenticationDataRepository;
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
        timeout = 5,
        rollbackFor = {OptimisticLockAppException.class}
)
public class MailVerifierServiceImpl implements MailVerifierService {
    private AccountRepository accountRepository;
    private AuthenticationDataRepository authenticationDataRepository;

    @Autowired
    public MailVerifierServiceImpl(AccountRepository accountRepository, AuthenticationDataRepository authenticationDataRepository) {
        this.accountRepository = accountRepository;
        this.authenticationDataRepository = authenticationDataRepository;
    }

    @Override
    public void verifyEmail(String veryficationToken) throws AccountNotExistsException, EmailAlreadyVerifyException, OptimisticLockAppException {
        Optional<AccountEntity> optionalAccountEntity= accountRepository.findByVeryficationToken(veryficationToken);
        if(optionalAccountEntity.isPresent()) {
            AccountEntity accountToConfirm = optionalAccountEntity.get();
            if(accountToConfirm.isEmailVerified()) {
                throw new EmailAlreadyVerifyException();
            }
            accountToConfirm.setEmailVerified(true);
            try{
                accountRepository.saveAndFlush(accountToConfirm);
                authenticationDataRepository.saveAndFlush(accountToConfirm.getAuthenticationDataEntity());
            }
            catch(OptimisticLockingFailureException ex){
                throw new OptimisticLockAppException();
            }
        } else {
            throw new AccountNotExistsException();
        }
    }
}
