package pl.lodz.p.it.inz.sgruda.multiStore.utils.components.mop;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mop.ProductDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mop.PromotionDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.SignatureDTOUtil;

@Log
@Component
public class SignPromotionDTOUtil {
    private SignatureDTOUtil signatureDTOUtil;

    @Autowired
    public SignPromotionDTOUtil(SignatureDTOUtil signatureDTOUtil) {
        this.signatureDTOUtil = signatureDTOUtil;
    }

    public void signProductDTO(PromotionDTO dto) {
        if (dto != null) {
            signatureDTOUtil.signDTO(dto);
        }
    }
}
