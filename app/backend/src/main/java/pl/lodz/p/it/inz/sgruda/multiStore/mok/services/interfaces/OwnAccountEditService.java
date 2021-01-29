package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.OperationDisabledForAccountException;

public interface OwnAccountEditService {
    void editAccount(AccountEntity accountEntity) throws OperationDisabledForAccountException;
    void changeAccountLanguage(AccountEntity accountEntity);
    AccountEntity getAccountByEmail(String mail) throws AccountNotExistsException;
}
