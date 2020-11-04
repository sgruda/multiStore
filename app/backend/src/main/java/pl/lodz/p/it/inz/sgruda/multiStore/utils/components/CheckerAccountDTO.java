package pl.lodz.p.it.inz.sgruda.multiStore.utils.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOSignatureException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOVersionException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.SignatureVerifiability;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.VersionGetter;

@Component
public class CheckerAccountDTO {
    private SignatureDTOUtil signatureDTOUtil;

    @Autowired
    public CheckerAccountDTO(SignatureDTOUtil signatureDTOUtil) {
        this.signatureDTOUtil = signatureDTOUtil;
    }

    public void checkIntegrity(AccountEntity entity, AccountDTO dto) throws DTOVersionException, DTOSignatureException {
        if(dto != null && entity != null) {
            this.checkSignatureAndVersion(entity, dto);
            if(dto.getAuthenticationDataDTO() != null) {
                this.checkSignatureAndVersion(entity.getAuthenticationDataEntity(), dto.getAuthenticationDataDTO());
                if(dto.getAuthenticationDataDTO().getForgotPasswordTokenDTO() != null) {
                    this.checkSignatureAndVersion(entity.getAuthenticationDataEntity().getForgotPasswordTokenEntity(),
                            dto.getAuthenticationDataDTO().getForgotPasswordTokenDTO());
                }
            }
        }
    }
    private void checkSignatureAndVersion(VersionGetter entity, SignatureVerifiability dto) throws DTOSignatureException, DTOVersionException {
        if(!signatureDTOUtil.checkSignatureDTO(dto)) {
            throw new DTOSignatureException();
        }
        if(entity.getVersion() != dto.getVersion()) {
            throw new DTOVersionException();
        }
    }

}
