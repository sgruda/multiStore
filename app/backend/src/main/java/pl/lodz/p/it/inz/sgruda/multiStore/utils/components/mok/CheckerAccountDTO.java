package pl.lodz.p.it.inz.sgruda.multiStore.utils.components.mok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOHashException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOVersionException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.CheckerSimpleDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.SignatureDTOUtil;

@Component
public class CheckerAccountDTO extends CheckerSimpleDTO {

    @Autowired
    public CheckerAccountDTO(SignatureDTOUtil signatureDTOUtil) {
        super(signatureDTOUtil);
    }

    public void checkAccountDTOSignature(AccountDTO dto) throws DTOHashException {
        if(dto != null) {
            super.checkSignatureSingleDTO(dto);
            if(dto.getAuthenticationDataDTO() != null) {
                super.checkSignatureSingleDTO(dto.getAuthenticationDataDTO());
                if(dto.getAuthenticationDataDTO().getForgotPasswordTokenDTO() != null) {
                    super.checkSignatureSingleDTO(dto.getAuthenticationDataDTO().getForgotPasswordTokenDTO());
                }
            }
        }
    }

    public void checkAccountDTOVersion(AccountEntity entity, AccountDTO dto) throws DTOVersionException {
        if(dto != null && entity != null) {
            super.checkVersionSingleDTO(entity, dto);
            if(dto.getAuthenticationDataDTO() != null) {
                super.checkVersionSingleDTO(entity.getAuthenticationDataEntity(), dto.getAuthenticationDataDTO());
                if(dto.getAuthenticationDataDTO().getForgotPasswordTokenDTO() != null) {
                    super.checkVersionSingleDTO(entity.getAuthenticationDataEntity().getForgotPasswordTokenEntity(),
                            dto.getAuthenticationDataDTO().getForgotPasswordTokenDTO());
                }
            }
        }
    }
}
