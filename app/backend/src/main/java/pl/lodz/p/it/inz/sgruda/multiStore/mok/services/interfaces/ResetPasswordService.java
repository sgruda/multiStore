package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public interface ResetPasswordService {
    String resetPassword(String email) throws AppBaseException;
}
