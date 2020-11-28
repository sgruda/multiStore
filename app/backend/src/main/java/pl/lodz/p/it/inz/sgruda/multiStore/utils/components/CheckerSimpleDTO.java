package pl.lodz.p.it.inz.sgruda.multiStore.utils.components;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOSignatureException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOVersionException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.DTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.SignatureVerifiability;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.VersionGetter;

@Component
public class CheckerSimpleDTO {
    private SignatureDTOUtil signatureDTOUtil;

    @Autowired
    public CheckerSimpleDTO(SignatureDTOUtil signatureDTOUtil) {
        this.signatureDTOUtil = signatureDTOUtil;
    }

    public void checkSignature(SignatureVerifiability dto) throws DTOSignatureException {
        if(dto != null) {
            this.checkSignatureSingleDTO(dto);
        }
    }
    public void checkVersion(VersionGetter entity, SignatureVerifiability dto) throws DTOVersionException {
        if(dto != null && entity != null) {
            this.checkVersionSingleDTO(entity, dto);
        }
    }
    protected void checkSignatureSingleDTO(SignatureVerifiability dto) throws DTOSignatureException {
        if(!signatureDTOUtil.checkSignatureDTO(dto)) {
            throw new DTOSignatureException();
        }
    }
    protected void checkVersionSingleDTO(VersionGetter entity, SignatureVerifiability dto) throws  DTOVersionException {
        if(entity.getVersion() != dto.getVersion()) {
            throw new DTOVersionException();
        }
    }
}
