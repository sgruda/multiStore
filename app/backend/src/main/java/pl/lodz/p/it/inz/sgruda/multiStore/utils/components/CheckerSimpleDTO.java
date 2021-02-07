package pl.lodz.p.it.inz.sgruda.multiStore.utils.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.OptimisticLockAppException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOHashException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.HashVerifiability;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.VersionGetter;

@Component
public class CheckerSimpleDTO {
    private SignatureDTOUtil signatureDTOUtil;

    @Autowired
    public CheckerSimpleDTO(SignatureDTOUtil signatureDTOUtil) {
        this.signatureDTOUtil = signatureDTOUtil;
    }

    public void checkSignature(HashVerifiability dto) throws DTOHashException {
        if(dto != null) {
            this.checkSignatureSingleDTO(dto);
        }
    }
    public void checkVersion(VersionGetter entity, HashVerifiability dto) throws OptimisticLockAppException {
        if(dto != null && entity != null) {
            this.checkVersionSingleDTO(entity, dto);
        }
    }
    protected void checkSignatureSingleDTO(HashVerifiability dto) throws DTOHashException {
        if(!signatureDTOUtil.checkSignatureDTO(dto)) {
            throw new DTOHashException();
        }
    }
    protected void checkVersionSingleDTO(VersionGetter entity, HashVerifiability dto) throws  OptimisticLockAppException {
        if(entity.getVersion() != dto.getVersion()) {
            throw new OptimisticLockAppException();
        }
    }
}
