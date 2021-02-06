package pl.lodz.p.it.inz.sgruda.multiStore.utils.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOHashException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOVersionException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.SignatureVerifiability;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.VersionGetter;

@Component
public class CheckerSimpleDTO {
    private SignatureDTOUtil signatureDTOUtil;

    @Autowired
    public CheckerSimpleDTO(SignatureDTOUtil signatureDTOUtil) {
        this.signatureDTOUtil = signatureDTOUtil;
    }

    public void checkSignature(SignatureVerifiability dto) throws DTOHashException {
        if(dto != null) {
            this.checkSignatureSingleDTO(dto);
        }
    }
    public void checkVersion(VersionGetter entity, SignatureVerifiability dto) throws DTOVersionException {
        if(dto != null && entity != null) {
            this.checkVersionSingleDTO(entity, dto);
        }
    }
    protected void checkSignatureSingleDTO(SignatureVerifiability dto) throws DTOHashException {
        if(!signatureDTOUtil.checkSignatureDTO(dto)) {
            throw new DTOHashException();
        }
    }
    protected void checkVersionSingleDTO(VersionGetter entity, SignatureVerifiability dto) throws  DTOVersionException {
        if(entity.getVersion() != dto.getVersion()) {
            throw new DTOVersionException();
        }
    }
}
