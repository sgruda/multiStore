package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;

public interface OwnPasswordChangeService {
    void changePassword(AccountEntity accountEntity, String newPassword, String oldPassword) throws AppBaseException;
    AccountEntity getAccountByEmail(String mail) throws AccountNotExistsException;
}
