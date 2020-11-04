package pl.lodz.p.it.inz.sgruda.multiStore.utils.components;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.SignatureVerifiability;

@Log
@Component
public class SignAccountDTOUtil {
    private SignatureDTOUtil signatureDTOUtil;

    @Autowired
    public SignAccountDTOUtil(SignatureDTOUtil signatureDTOUtil) {
        this.signatureDTOUtil = signatureDTOUtil;
    }

    public void signAccountDTO(AccountDTO dto) {
        if(dto != null) {
            signatureDTOUtil.signDTO(dto);
            if(dto.getAuthenticationDataDTO() != null) {
                signatureDTOUtil.signDTO(dto.getAuthenticationDataDTO());
                if(dto.getAuthenticationDataDTO().getForgotPasswordTokenDTO() != null) {
                    signatureDTOUtil.signDTO(dto.getAuthenticationDataDTO().getForgotPasswordTokenDTO());
                }
            }
        }
    }
    public boolean checkSignatureAccountDTO(AccountDTO dto) {
        boolean result = false;
        if(dto != null) {
            result = signatureDTOUtil.checkSignatureDTO(dto);
            if(dto.getAuthenticationDataDTO() != null) {
                result = signatureDTOUtil.checkSignatureDTO(dto.getAuthenticationDataDTO());
                if(dto.getAuthenticationDataDTO().getForgotPasswordTokenDTO() != null) {
                    result = signatureDTOUtil.checkSignatureDTO(dto.getAuthenticationDataDTO().getForgotPasswordTokenDTO());
                }
            }
        }
        return result;
    }
}
