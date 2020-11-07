package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.EmailAlreadyExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.UsernameAlreadyExistsException;

public interface AuthService {
    String authenticateAccount(String username, String password);
    AccountEntity registerAccount(AccountEntity account) throws EmailAlreadyExistsException, UsernameAlreadyExistsException;

}