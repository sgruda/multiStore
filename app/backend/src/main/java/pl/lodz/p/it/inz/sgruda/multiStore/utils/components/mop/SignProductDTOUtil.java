package pl.lodz.p.it.inz.sgruda.multiStore.utils.components.mop;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mop.ProductDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.SignatureDTOUtil;

@Log
@Component
public class SignProductDTOUtil {
    private SignatureDTOUtil signatureDTOUtil;

    @Autowired
    public SignProductDTOUtil(SignatureDTOUtil signatureDTOUtil) {
        this.signatureDTOUtil = signatureDTOUtil;
    }

    public void signProductDTO(ProductDTO dto) {
        if (dto != null) {
            signatureDTOUtil.signDTO(dto);
        }
    }
}
