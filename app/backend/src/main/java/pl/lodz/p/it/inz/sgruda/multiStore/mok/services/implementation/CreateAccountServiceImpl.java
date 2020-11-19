package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccessLevelEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.EmailAlreadyExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.IncorrectRoleNameException;
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
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        timeout = 5
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
        for(String roleString : roles) {
            if(!roleString.matches("(ROLE_CLIENT|ROLE_EMPLOYEE|ROLE_ADMIN)")) {
                throw new IncorrectRoleNameException();
            }
            Optional<AccessLevelEntity> opt = accessLevelRepository.findByRoleName(RoleName.valueOf(roleString));
            if(opt.isPresent())
                accessLevelEntitySet.add(opt.get());
        }
        accountEntity.setAccessLevelEntities(accessLevelEntitySet);

        return accountRepository.saveAndFlush(accountEntity);
    }
}
