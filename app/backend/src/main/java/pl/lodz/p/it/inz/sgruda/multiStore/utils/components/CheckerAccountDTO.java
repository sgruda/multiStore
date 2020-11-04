package pl.lodz.p.it.inz.sgruda.multiStore.utils.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOSignatureException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOVersionException;

@Component
public class CheckerAccountDTO {
    private CheckerDTO checkerDTO;

    @Autowired
    public CheckerAccountDTO(CheckerDTO checkerDTO) {
        this.checkerDTO = checkerDTO;
    }

    public void checkIntegrity(AccountEntity entity, AccountDTO dto) throws DTOVersionException, DTOSignatureException {
        if(dto != null && entity != null) {
            checkerDTO.checkIntegrity(entity, dto);
            if(dto.getAuthenticationDataDTO() != null) {
               checkerDTO.checkIntegrity(entity.getAuthenticationDataEntity(), dto.getAuthenticationDataDTO());
                if(dto.getAuthenticationDataDTO().getForgotPasswordTokenDTO() != null) {
                    checkerDTO.checkIntegrity(entity.getAuthenticationDataEntity().getForgotPasswordTokenEntity(),
                            dto.getAuthenticationDataDTO().getForgotPasswordTokenDTO());
                }
            }
        }
    }

}
