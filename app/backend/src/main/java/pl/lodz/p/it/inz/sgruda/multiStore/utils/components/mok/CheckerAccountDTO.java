package pl.lodz.p.it.inz.sgruda.multiStore.utils.components.mok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOHashException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.CheckerSimpleDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.HashDTOUtil;

@Component
public class CheckerAccountDTO extends CheckerSimpleDTO {

    @Autowired
    public CheckerAccountDTO(HashDTOUtil hashDTOUtil) {
        super(hashDTOUtil);
    }

    public void checkAccountDTOHash(AccountDTO dto) throws DTOHashException {
        if(dto != null) {
            super.checkHashSingleDTO(dto);
            if(dto.getAuthenticationDataDTO() != null) {
                super.checkHashSingleDTO(dto.getAuthenticationDataDTO());
                if(dto.getAuthenticationDataDTO().getForgotPasswordTokenDTO() != null) {
                    super.checkHashSingleDTO(dto.getAuthenticationDataDTO().getForgotPasswordTokenDTO());
                }
            }
        }
    }
}
