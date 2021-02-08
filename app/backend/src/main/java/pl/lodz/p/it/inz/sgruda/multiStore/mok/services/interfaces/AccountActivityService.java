package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.OptimisticLockAppException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;

public interface AccountActivityService {
    void blockAccount(AccountEntity accountEntity) throws OptimisticLockAppException;
    void unblockAccount(AccountEntity accountEntity) throws OptimisticLockAppException;
    AccountEntity getAccountByEmail(String mail) throws AccountNotExistsException;
}
