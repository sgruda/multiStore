package pl.lodz.p.it.inz.sgruda.multiStore.utils.components.mop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mop.ProductDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOSignatureException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOVersionException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.SignatureDTOUtil;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.SignatureVerifiability;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.VersionGetter;

@Component
public class CheckerProductDTO {
    private SignatureDTOUtil signatureDTOUtil;

    @Autowired
    public CheckerProductDTO(SignatureDTOUtil signatureDTOUtil) {
        this.signatureDTOUtil = signatureDTOUtil;
    }

    public void checkSignature(ProductDTO dto) throws DTOSignatureException {
        if(dto != null) {
            this.checkSignatureSingleDTO(dto);
        }
    }
    public void checkVersion(ProductEntity entity, ProductDTO dto) throws DTOVersionException {
        if(dto != null && entity != null) {
            this.checkVersionSingleDTO(entity, dto);
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
