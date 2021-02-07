package pl.lodz.p.it.inz.sgruda.multiStore.utils.components.moz;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.BasketDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.OrderDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.OrderedItemDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.HashDTOUtil;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.HashSimpleDTO;

import java.util.Set;
@Log
@Component
public class HashMozDTOUtil extends HashSimpleDTO {
    @Autowired
    public HashMozDTOUtil(HashDTOUtil hashDTOUtil) {
        super(hashDTOUtil);
    }

    public void hashOrderDTO(OrderDTO dto) {
        if(dto != null) {
            Set<OrderedItemDTO> items = dto.getOrderedItemDTOS();
            if(items.size() > 0) {
                items.forEach(item -> {
                    super.hashDTO(item);
                    super.hashDTO(item.getOrderedProduct());
                });
            }
            super.hashDTO(dto);
        }
    }
    public void hashBasketDTO(BasketDTO dto) {
        if(dto != null) {
            dto.getOrderedItemDTOS().forEach(item -> {
                super.hashDTO(item);
                super.hashDTO(item.getOrderedProduct());
            });
            super.hashDTO(dto);
        }
    }
}
