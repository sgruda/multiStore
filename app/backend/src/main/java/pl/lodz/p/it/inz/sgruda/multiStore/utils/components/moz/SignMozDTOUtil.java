package pl.lodz.p.it.inz.sgruda.multiStore.utils.components.moz;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.BasketDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.OrderDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.OrderedItemDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.SignSimpleDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.SignatureDTOUtil;

import java.util.List;
import java.util.Set;
@Log
@Component
public class SignMozDTOUtil extends SignSimpleDTO {
    @Autowired
    public SignMozDTOUtil(SignatureDTOUtil signatureDTOUtil) {
        super(signatureDTOUtil);
    }

    public void signOrderDTO(OrderDTO dto) {
        if(dto != null) {
            Set<OrderedItemDTO> items = dto.getOrderedItemDTOS();
            if(items.size() > 0) {
                items.forEach(item -> {
                    super.signDTO(item);
                    super.signDTO(item.getOrderedProduct());
                });
            }
            super.signDTO(dto);
        }
    }
    public void signBasketDTO(BasketDTO dto) {
        if(dto != null) {
            dto.getOrderedItemDTOS().forEach(item -> {
                super.signDTO(item);
                super.signDTO(item.getOrderedProduct());
            });
            super.signDTO(dto);
        }
    }
}
