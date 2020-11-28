package pl.lodz.p.it.inz.sgruda.multiStore;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mop.PromotionDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOSignatureException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.mop.CheckerPromotionDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.mop.SignPromotionDTOUtil;

@Log
@SpringBootTest
public class PromotionDTOUtilTest {
    private @Autowired
    CheckerPromotionDTO checkerPromotionDTO;
    private @Autowired
    SignPromotionDTOUtil signPromotionDTOUtil;

    @Test
    void test() {
        PromotionDTO promotionDTO = new PromotionDTO();
        promotionDTO.setIdHash("123");
        promotionDTO.setName("name");
        promotionDTO.setDiscount(12);
        promotionDTO.setOnCategory("action");
        promotionDTO.setVersion(0);
        signPromotionDTOUtil.signProductDTO(promotionDTO);

        boolean catched = false;
        promotionDTO.setVersion(1);
        try {
            checkerPromotionDTO.checkSignature(promotionDTO);
        } catch (DTOSignatureException e) {
            catched = true;
        }
        Assertions.assertEquals(true, catched);
        promotionDTO.setVersion(0);

        catched = false;
        promotionDTO.setName("name2");
        try {
            checkerPromotionDTO.checkSignature(promotionDTO);
        } catch (DTOSignatureException e) {
            catched = true;
        }
        Assertions.assertEquals(true, catched);
    }
}
