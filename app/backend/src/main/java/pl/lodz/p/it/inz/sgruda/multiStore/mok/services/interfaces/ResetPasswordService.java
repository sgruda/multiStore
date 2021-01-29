package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.Language;

public interface ResetPasswordService {
    Language getAccountLanguage(String email) throws AppBaseException;
    String resetPassword(String email) throws AppBaseException;
    void changeResetPassword(String resetPasswordToken, String newPasswordEncoded) throws AppBaseException;
}
