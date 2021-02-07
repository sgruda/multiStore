package pl.lodz.p.it.inz.sgruda.multiStore.utils.components.mok;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.HashSimpleDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.HashDTOUtil;

@Log
@Component
public class HashAccountDTOUtil extends HashSimpleDTO {

    @Autowired
    public HashAccountDTOUtil(HashDTOUtil hashDTOUtil) {
        super(hashDTOUtil);
    }

    public void signAccountDTO(AccountDTO dto) {
        if(dto != null) {
            super.hashDTO(dto);
            if(dto.getAuthenticationDataDTO() != null) {
                super.hashDTO(dto.getAuthenticationDataDTO());
                if(dto.getAuthenticationDataDTO().getForgotPasswordTokenDTO() != null) {
                    super.hashDTO(dto.getAuthenticationDataDTO().getForgotPasswordTokenDTO());
                }
            }
        }
    }
}
