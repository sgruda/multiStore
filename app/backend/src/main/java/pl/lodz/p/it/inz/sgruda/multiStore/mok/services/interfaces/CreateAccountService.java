package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

import java.util.Set;

public interface CreateAccountService {
    AccountEntity createAccount(AccountEntity accountEntity, Set<String> roles) throws AppBaseException;
}
