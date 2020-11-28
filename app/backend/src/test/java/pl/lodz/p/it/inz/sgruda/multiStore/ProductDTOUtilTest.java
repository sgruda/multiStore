package pl.lodz.p.it.inz.sgruda.multiStore;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mop.ProductDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOSignatureException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.mop.CheckerProductDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.mop.SignProductDTOUtil;


@Log
@SpringBootTest
public class ProductDTOUtilTest {
    private @Autowired CheckerProductDTO checkerProductDTO;
    private @Autowired SignProductDTOUtil signProductDTOUtil;

    @Test
    void test() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setIdHash("123");
        productDTO.setTitle("title");
        productDTO.setDescription("desc");
        productDTO.setInStore(123);
        productDTO.setPrice(9.99);
        productDTO.setType("movie");
        productDTO.setCategory("action");
        productDTO.setVersion(0);
        signProductDTOUtil.signProductDTO(productDTO);

        boolean catched = false;
        productDTO.setVersion(1);
        try {
            checkerProductDTO.checkSignature(productDTO);
        } catch (DTOSignatureException e) {
            catched = true;
        }
        Assertions.assertEquals(true, catched);
        productDTO.setVersion(0);

        catched = false;
        productDTO.setTitle("title2");
        try {
            checkerProductDTO.checkSignature(productDTO);
        } catch (DTOSignatureException e) {
            catched = true;
        }
        Assertions.assertEquals(true, catched);
    }
}
