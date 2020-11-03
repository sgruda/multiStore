package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;

public interface AccountDetailsService {
    AccountEntity getAccountByUsername(String username) throws AccountNotExistsException;
    AccountEntity getAccountByEmail(String mail) throws AccountNotExistsException;
}
