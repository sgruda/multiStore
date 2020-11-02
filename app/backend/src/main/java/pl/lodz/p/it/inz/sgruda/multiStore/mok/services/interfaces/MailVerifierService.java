package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.EmailAlreadyVerifyException;

public interface MailVerifierService {
    void verifyEmail(String veryficationToken) throws AccountNotExistsException, EmailAlreadyVerifyException;
}
