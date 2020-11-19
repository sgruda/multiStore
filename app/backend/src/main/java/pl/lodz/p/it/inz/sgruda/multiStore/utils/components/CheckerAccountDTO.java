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

    public void checkSignature(AccountDTO dto) throws DTOSignatureException {
        if(dto != null) {
            this.checkSignatureSingleDTO(dto);
            if(dto.getAuthenticationDataDTO() != null) {
                this.checkSignatureSingleDTO(dto.getAuthenticationDataDTO());
                if(dto.getAuthenticationDataDTO().getForgotPasswordTokenDTO() != null) {
                    this.checkSignatureSingleDTO(dto.getAuthenticationDataDTO().getForgotPasswordTokenDTO());
                }
            }
        }
    }
    public void checkVersion(AccountEntity entity, AccountDTO dto) throws DTOVersionException {
        if(dto != null && entity != null) {
            this.checkVersionSingleDTO(entity, dto);
            if(dto.getAuthenticationDataDTO() != null) {
                this.checkVersionSingleDTO(entity.getAuthenticationDataEntity(), dto.getAuthenticationDataDTO());
                if(dto.getAuthenticationDataDTO().getForgotPasswordTokenDTO() != null) {
                    this.checkVersionSingleDTO(entity.getAuthenticationDataEntity().getForgotPasswordTokenEntity(),
                            dto.getAuthenticationDataDTO().getForgotPasswordTokenDTO());
                }
            }
        }
    }
    private void checkSignatureSingleDTO(SignatureVerifiability dto) throws DTOSignatureException {
        if(!signatureDTOUtil.checkSignatureDTO(dto)) {
            throw new DTOSignatureException();
        }
    }
    private void checkVersionSingleDTO(VersionGetter entity, SignatureVerifiability dto) throws  DTOVersionException {
        if(entity.getVersion() != dto.getVersion()) {
            throw new DTOVersionException();
        }
    }

}
