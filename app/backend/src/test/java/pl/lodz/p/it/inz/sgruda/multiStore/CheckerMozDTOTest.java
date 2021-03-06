package pl.lodz.p.it.inz.sgruda.multiStore;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mop.ProductDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.OrderDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.OrderedItemDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOHashException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.HashSimpleDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.moz.CheckerMozDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.moz.HashMozDTOUtil;

import java.time.LocalDateTime;

@Log
@SpringBootTest
public class CheckerMozDTOTest {
    private @Autowired CheckerMozDTO checkerMozDTO;
    private @Autowired HashMozDTOUtil hashMozDTOUtil;
    private @Autowired HashSimpleDTO hashSimpleDTO;

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
        hashSimpleDTO.hashDTO(productDTO);

        OrderedItemDTO orderedItemDTO = new OrderedItemDTO();
        orderedItemDTO.setId(123456789);
        orderedItemDTO.setIdentifier("zxcvnm");
        orderedItemDTO.setOrderedNumber(2);
        orderedItemDTO.setOrderedProduct(productDTO);
        orderedItemDTO.setVersion(0);
        hashSimpleDTO.hashDTO((orderedItemDTO));

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(123);
        orderDTO.setIdentifier("3456");
        orderDTO.setOrderDate(LocalDateTime.now());
        orderDTO.setBuyerEmail("jan.kowalski@gmail.com");
        orderDTO.getOrderedItemDTOS().add(
                orderedItemDTO
        );
        orderDTO.setTotalPrice(123);
        orderDTO.setStatus("submitted");
        orderDTO.setAddress("Warszawa, Fajna 32/44");
        orderDTO.setVersion(0);
        hashMozDTOUtil.hashOrderDTO(orderDTO);

        boolean catched = false;

        try {
            checkerMozDTO.checkOrderDTOHash(orderDTO);
        } catch (DTOHashException e) {
            catched = true;
        }
        Assertions.assertEquals(false, catched);

        catched = false;
        orderDTO.setVersion(1);
        try {
            checkerMozDTO.checkOrderDTOHash(orderDTO);
        } catch (DTOHashException e) {
            catched = true;
        }
        Assertions.assertEquals(true, catched);
        orderDTO.setVersion(0);

        catched = false;
        productDTO.setTitle("title2");
        try {
            checkerMozDTO.checkOrderDTOHash(orderDTO);
        } catch (DTOHashException e) {
            catched = true;
        }
        Assertions.assertEquals(true, catched);
    }
}
