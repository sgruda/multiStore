package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;

import java.util.Set;

public interface AccountAccessLevelService {
    AccountEntity getAccountByEmail(String mail) throws AccountNotExistsException;
    void addAccessLevel(AccountEntity accountEntity, Set<String> accessLevelSetToAdd) throws AppBaseException;
    void removeAccessLevel(AccountEntity accountEntity, Set<String> accessLevelSetToRemove) throws AppBaseException;
}
