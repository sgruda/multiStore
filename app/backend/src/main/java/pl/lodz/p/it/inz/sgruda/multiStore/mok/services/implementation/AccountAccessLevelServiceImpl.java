package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccessLevelEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.IncorrectRoleNameException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.RemovingAllAccessLevelsException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccessLevelRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccountRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.AccountAccessLevelService;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.RoleName;

import java.util.Optional;
import java.util.Set;

@Log
@Service
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        timeout = 5
)
public class AccountAccessLevelServiceImpl implements AccountAccessLevelService {

    private AccountRepository accountRepository;
    private AccessLevelRepository accessLevelRepository;

    @Autowired
    public AccountAccessLevelServiceImpl(AccountRepository accountRepository, AccessLevelRepository accessLevelRepository) {
        this.accountRepository = accountRepository;
        this.accessLevelRepository = accessLevelRepository;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addAccessLevel(AccountEntity accountEntity, Set<String> accessLevelSetToAdd) throws AppBaseException {
        Set<AccessLevelEntity> accessLevelEntitySet = accountEntity.getAccessLevelEntities();
        for(String roleString : accessLevelSetToAdd) {
            if(!roleString.matches("(ROLE_CLIENT|ROLE_EMPLOYEE|ROLE_ADMIN)")) {
                throw new IncorrectRoleNameException();
            }
            Optional<AccessLevelEntity> opt = accessLevelRepository.findByRoleName(RoleName.valueOf(roleString));
            if(opt.isPresent())
                accessLevelEntitySet.add(opt.get());
        }
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void removeAccessLevel(AccountEntity accountEntity, Set<String> accessLevelSetToRemove) throws AppBaseException {
        Set<AccessLevelEntity> accessLevelEntitySet = accountEntity.getAccessLevelEntities();
        if(accessLevelSetToRemove.size() >= accessLevelEntitySet.size())
            throw new RemovingAllAccessLevelsException();
        for(String roleString : accessLevelSetToRemove) {
            if(!roleString.matches("(ROLE_CLIENT|ROLE_EMPLOYEE|ROLE_ADMIN)")) {
                throw new IncorrectRoleNameException();
            }
            Optional<AccessLevelEntity> opt = accessLevelRepository.findByRoleName(RoleName.valueOf(roleString));
            if(opt.isPresent())
                accessLevelEntitySet.remove(opt.get());
        }
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AccountEntity getAccountByEmail(String mail) throws AccountNotExistsException {
        return accountRepository.findByEmail(mail)
                .orElseThrow(() -> new AccountNotExistsException());
    }
}
