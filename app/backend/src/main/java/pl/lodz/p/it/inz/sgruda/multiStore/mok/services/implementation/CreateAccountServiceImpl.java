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
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccessLevelEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.BasketEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.OptimisticLockAppException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.EmailAlreadyExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.IncorrectRoleNameException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.RolesSetEmptyException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.UsernameAlreadyExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccessLevelRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccountRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.CreateAccountService;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.RoleName;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

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
public class CreateAccountServiceImpl implements CreateAccountService {
    private AccountRepository accountRepository;
    private AccessLevelRepository accessLevelRepository;

    @Autowired
    public CreateAccountServiceImpl(AccountRepository accountRepository, AccessLevelRepository accessLevelRepository) {
        this.accountRepository = accountRepository;
        this.accessLevelRepository = accessLevelRepository;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AccountEntity createAccount(AccountEntity accountEntity, Set<String> roles) throws AppBaseException {
        if(accountRepository.existsByUsername(accountEntity.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }
        if(accountRepository.existsByEmail(accountEntity.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
        Set<AccessLevelEntity> accessLevelEntitySet = new LinkedHashSet<>();
        if(roles.size() == 0)
            throw new RolesSetEmptyException();
        for(String roleString : roles) {
            if(!roleString.matches("(ROLE_CLIENT|ROLE_EMPLOYEE|ROLE_ADMIN)")) {
                throw new IncorrectRoleNameException();
            }
            Optional<AccessLevelEntity> opt = accessLevelRepository.findByRoleName(RoleName.valueOf(roleString));
            if(opt.isPresent())
                accessLevelEntitySet.add(opt.get());
        }
        accountEntity.setAccessLevelEntities(accessLevelEntitySet);

        BasketEntity basketEntity = new BasketEntity(accountEntity);
        accountEntity.setBasketEntity(basketEntity);
        try{
            return accountRepository.saveAndFlush(accountEntity);
        }
        catch(OptimisticLockingFailureException ex){
            throw new OptimisticLockAppException();
        }
    }
}
