package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;

public interface AccountActivityService {
    void blockAccount(AccountEntity accountEntity);
    void unblockAccount(AccountEntity accountEntity);
    AccountEntity getAccountByEmail(String mail) throws AccountNotExistsException;
}
