package pl.lodz.p.it.inz.sgruda.multiStore.utils.components;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.HashVerifiability;


@Log
@Component
public class SignSimpleDTO {
    private SignatureDTOUtil signatureDTOUtil;

    @Autowired
    public SignSimpleDTO(SignatureDTOUtil signatureDTOUtil) {
        this.signatureDTOUtil = signatureDTOUtil;
    }

    public void signDTO(HashVerifiability dto) {
        if (dto != null) {
            signatureDTOUtil.signDTO(dto);
        }
    }
}