package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public interface NotEmailVerifiedAccountService {
    AccountEntity getAccountByEmailIfNotVerified(String mail) throws AppBaseException;
    void removeAccountWithNotVerifiedEmail(AccountEntity accountEntity) throws AppBaseException;
}
