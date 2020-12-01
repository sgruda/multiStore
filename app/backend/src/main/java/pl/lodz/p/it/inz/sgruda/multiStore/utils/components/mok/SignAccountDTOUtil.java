package pl.lodz.p.it.inz.sgruda.multiStore.utils.components.mok;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.SignSimpleDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.SignatureDTOUtil;

@Log
@Component
public class SignAccountDTOUtil extends SignSimpleDTO {

    @Autowired
    public SignAccountDTOUtil(SignatureDTOUtil signatureDTOUtil) {
        super(signatureDTOUtil);
    }

    public void signAccountDTO(AccountDTO dto) {
        if(dto != null) {
            super.signDTO(dto);
            if(dto.getAuthenticationDataDTO() != null) {
                super.signDTO(dto.getAuthenticationDataDTO());
                if(dto.getAuthenticationDataDTO().getForgotPasswordTokenDTO() != null) {
                    super.signDTO(dto.getAuthenticationDataDTO().getForgotPasswordTokenDTO());
                }
            }
        }
    }
}
