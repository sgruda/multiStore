package pl.lodz.p.it.inz.sgruda.multiStore.utils.components.moz;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.BasketDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.OrderDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.OrderedItemDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOHashException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.CheckerSimpleDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.HashDTOUtil;
@Log
@Component
public class CheckerMozDTO extends CheckerSimpleDTO {
    @Autowired
    public CheckerMozDTO(HashDTOUtil hashDTOUtil) {
        super(hashDTOUtil);
    }

    public void checkOrderDTOHash(OrderDTO dto) throws DTOHashException {
        if(dto != null) {
            for(OrderedItemDTO item : dto.getOrderedItemDTOS()) {
                super.checkHashSingleDTO(item);
                super.checkHashSingleDTO(item.getOrderedProduct());
            }
            super.checkHashSingleDTO(dto);
        }
    }

    public void checkBasketDTOHash(BasketDTO dto) throws DTOHashException {
        if(dto != null) {
            for(OrderedItemDTO item : dto.getOrderedItemDTOS()) {
                super.checkHashSingleDTO(item);
                super.checkHashSingleDTO(item.getOrderedProduct());
            }
            super.checkHashSingleDTO(dto);
        }
    }

    public void checkOrderedItemDTOHash(OrderedItemDTO dto) throws DTOHashException {
        if(dto != null) {
            super.checkHashSingleDTO(dto);
            super.checkHashSingleDTO(dto.getOrderedProduct());
        }
    }
}
