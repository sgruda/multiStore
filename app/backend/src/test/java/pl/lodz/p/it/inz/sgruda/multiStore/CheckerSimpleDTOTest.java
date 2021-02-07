package pl.lodz.p.it.inz.sgruda.multiStore;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mop.ProductDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOHashException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.CheckerSimpleDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.SignSimpleDTO;


@Log
@SpringBootTest
public class CheckerSimpleDTOTest {
    private @Autowired CheckerSimpleDTO checkerSimpleDTO;
    private @Autowired SignSimpleDTO signSimpleDTO;

    @Test
    void test() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(123);
        productDTO.setTitle("title");
        productDTO.setDescription("desc");
        productDTO.setInStore(123);
        productDTO.setPrice(9.99);
        productDTO.setType("ebook");
        productDTO.setCategory("action");
        productDTO.setVersion(0);
        signSimpleDTO.signDTO(productDTO);

        boolean catched = false;
        productDTO.setVersion(1);
        try {
            checkerSimpleDTO.checkSignature(productDTO);
        } catch (DTOHashException e) {
            catched = true;
        }
        Assertions.assertEquals(true, catched);
        productDTO.setVersion(0);

        catched = false;
        productDTO.setTitle("title2");
        try {
            checkerSimpleDTO.checkSignature(productDTO);
        } catch (DTOHashException e) {
            catched = true;
        }
        Assertions.assertEquals(true, catched);
    }
}
