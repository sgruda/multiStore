package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.EmailAlreadyVerifyException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.OperationDisabledForAccountException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccountRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.AccountActivityService;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.MailVerifierService;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.NotEmailVerifiedAccountService;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.AuthProvider;

import java.util.Optional;

@Log
@Service
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        timeout = 5
)
public class NotEmailVerifiedAccountServiceImpl implements NotEmailVerifiedAccountService {
    private AccountRepository accountRepository;

    @Autowired
    public NotEmailVerifiedAccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountEntity getAccountByEmailIfNotVerified(String mail) throws AppBaseException {
        Optional<AccountEntity> opt =  accountRepository.findByEmail(mail);
        if(opt.isEmpty())
            throw new AccountNotExistsException();
        AccountEntity accountEntity = opt.get();
        if(accountEntity.getProvider() != AuthProvider.system)
            throw new OperationDisabledForAccountException();
        if(accountEntity.getAuthenticationDataEntity().isEmailVerified())
            throw new EmailAlreadyVerifyException();
        return accountEntity;
    }
}