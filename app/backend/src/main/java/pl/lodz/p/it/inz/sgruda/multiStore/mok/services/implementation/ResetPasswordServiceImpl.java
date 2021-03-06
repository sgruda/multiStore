package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.ForgotPasswordTokenEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.OptimisticLockAppException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.ForgotPasswordTokenNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.IncorrectForgotPasswordTokenException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.OperationDisabledForAccountException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccountRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.ForgotPasswordTokenRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.ResetPasswordService;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.AuthProvider;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.Language;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Log
@Service
@Retryable(
        maxAttempts = 5,
        backoff = @Backoff(delay = 10000),
        exclude = {AppBaseException.class}
)
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        timeout = 5,
        transactionManager = "mokTransactionManager",
        rollbackFor = {OptimisticLockAppException.class}
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
    public Language getAccountLanguage(String email) throws AppBaseException {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotExistsException())
                .getLanguage();
    }

    @Override
    public String resetPassword(String email) throws AppBaseException {
        Optional<AccountEntity> optionalAccountEntity = accountRepository.findByEmail(email);
        if(optionalAccountEntity.isPresent()) {
            AccountEntity accountEntity = optionalAccountEntity.get();
            if(accountEntity.getProvider() != AuthProvider.system)
                throw new OperationDisabledForAccountException();
            if(!accountEntity.getAuthenticationDataEntity().isEmailVerified())
                throw new OperationDisabledForAccountException("error.account.disabled.operation.email.non-verified");
            if(!accountEntity.isActive())
                throw new OperationDisabledForAccountException("error.account.disabled.operation.account.disabled");
            ForgotPasswordTokenEntity newTokenEntity;
            if(accountEntity.getForgotPasswordTokenEntity() == null) {
                newTokenEntity = new ForgotPasswordTokenEntity();
            } else {
                newTokenEntity = accountEntity.getAuthenticationDataEntity().getForgotPasswordTokenEntity();
            }
            newTokenEntity.setAccountEntity(accountEntity);
            newTokenEntity.setExpireDate(LocalDateTime.now().plusSeconds(Integer.parseInt(FORGOT_PASSWORD_TOKEN_LIFETIME) / 1000));
            newTokenEntity.setToken(UUID.randomUUID().toString().replace("-", "").substring(0, 6));
            accountEntity.getAuthenticationDataEntity().setForgotPasswordTokenEntity(newTokenEntity);
            try{
                forgotPasswordTokenRepository.saveAndFlush(newTokenEntity);
                accountRepository.saveAndFlush(accountEntity);
            }
            catch(OptimisticLockingFailureException ex){
                throw new OptimisticLockAppException();
            }
            return newTokenEntity.getToken();
        } else
            throw new AccountNotExistsException();
    }

    @Override
    public void changeResetPassword(String resetPasswordToken, String newPasswordEncoded) throws AppBaseException {
        Optional<ForgotPasswordTokenEntity> optionalForgotPasswordTokenEntity = forgotPasswordTokenRepository.findByToken(resetPasswordToken);
        if(optionalForgotPasswordTokenEntity.isPresent()) {
            ForgotPasswordTokenEntity forgotPasswordTokenEntity = optionalForgotPasswordTokenEntity.get();
            if(forgotPasswordTokenEntity.getExpireDate().isBefore(LocalDateTime.now())) {
                throw new IncorrectForgotPasswordTokenException();
            }
            AccountEntity accountEntity = forgotPasswordTokenEntity.getAccountEntity();
            accountEntity.setPassword(newPasswordEncoded);
            accountEntity.getAuthenticationDataEntity().setForgotPasswordTokenEntity(null);
            try{
                forgotPasswordTokenRepository.delete(forgotPasswordTokenEntity);
                accountRepository.saveAndFlush(accountEntity);
            }
            catch(OptimisticLockingFailureException ex){
                throw new OptimisticLockAppException();
            }
        } else
            throw new ForgotPasswordTokenNotExistsException();
    }

}
