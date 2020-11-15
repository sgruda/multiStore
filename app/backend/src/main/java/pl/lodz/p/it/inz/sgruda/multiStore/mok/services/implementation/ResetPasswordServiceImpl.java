package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.ForgotPasswordTokenEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccountRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.ForgotPasswordTokenRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.ResetPasswordService;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.HashMethod;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Log
@Service
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        timeout = 5
)
public class ResetPasswordServiceImpl implements ResetPasswordService {
    private AccountRepository accountRepository;
    private ForgotPasswordTokenRepository forgotPasswordTokenRepository;

    @Value("${app.auth.forgotPasswordToken.expiration.mili-sec}")
    private String FORGOT_PASSWORD_TOKEN_LIFETIME;

    @Autowired
    public ResetPasswordServiceImpl(AccountRepository accountRepository, ForgotPasswordTokenRepository forgotPasswordTokenRepository) {
        this.accountRepository = accountRepository;
        this.forgotPasswordTokenRepository = forgotPasswordTokenRepository;
    }

    @Override
    public String resetPassword(String email) throws AppBaseException {
        Optional<AccountEntity> optionalAccountEntity = accountRepository.findByEmail(email);
        if(optionalAccountEntity.isPresent()) {
            AccountEntity accountEntity = optionalAccountEntity.get();
            if(accountEntity.getForgotPasswordTokenEntity() != null) {
                ForgotPasswordTokenEntity oldToken = accountEntity.getForgotPasswordTokenEntity();
                forgotPasswordTokenRepository.delete(oldToken);
            }
            HashMethod hashMethod = new HashMethod();
            ForgotPasswordTokenEntity newTokenEntity = new ForgotPasswordTokenEntity();
            newTokenEntity.setAccountEntity(accountEntity);
            newTokenEntity.setExpireDate(LocalDateTime.now().plusSeconds(1000 * Integer.parseInt(FORGOT_PASSWORD_TOKEN_LIFETIME)));
            newTokenEntity.setHash(hashMethod.hash(newTokenEntity.getExpireDate().toString() + UUID.randomUUID().toString()));
            forgotPasswordTokenRepository.saveAndFlush(newTokenEntity);
            accountEntity.getAuthenticationDataEntity().setForgotPasswordTokenEntity(newTokenEntity);
            return newTokenEntity.getHash();
        } else
            throw new AccountNotExistsException();
    }
}
