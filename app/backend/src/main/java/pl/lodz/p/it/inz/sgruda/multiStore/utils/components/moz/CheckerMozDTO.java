package pl.lodz.p.it.inz.sgruda.multiStore.utils.components.moz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.BasketDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.OrderDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.OrderedItemsDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.BasketEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderedItemsEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOSignatureException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOVersionException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.CheckerSimpleDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.SignatureDTOUtil;

import java.util.Set;

@Component
public class CheckerMozDTO extends CheckerSimpleDTO {
    @Autowired
    public CheckerMozDTO(SignatureDTOUtil signatureDTOUtil) {
        super(signatureDTOUtil);
    }

    public void checkOrderDTOSignature(OrderDTO dto) throws DTOSignatureException {
        if(dto != null) {
            for(OrderedItemsDTO item : dto.getOrderedItemsDTOS()) {
                super.checkSignatureSingleDTO(item);
            }
            super.checkSignatureSingleDTO(dto);
        }
    }

    public void checkBasketDTOSignature(BasketDTO dto) throws DTOSignatureException {
        if(dto != null) {
            for(OrderedItemsDTO item : dto.getOrderedItemsDTOS()) {
                super.checkSignatureSingleDTO(item);
            }
            super.checkSignatureSingleDTO(dto);
        }
    }

    public void checkOrderDTOVersion(OrderEntity entity, OrderDTO dto) throws DTOVersionException {
        if(dto != null && entity != null) {
            for(OrderedItemsDTO item : dto.getOrderedItemsDTOS()) {
                for(OrderedItemsEntity itemsEntityTemp : entity.getOrderedItemsEntities())
                    if(itemsEntityTemp.getIdentifier().equals(item.getIdentifier()))
                        super.checkVersionSingleDTO(itemsEntityTemp, item);
            }
            super.checkVersionSingleDTO(entity, dto);
        }
    }
    public void checkBasketDTOVersion(BasketEntity entity, BasketDTO dto) throws DTOVersionException {
        if(dto != null && entity != null) {
            for(OrderedItemsDTO item : dto.getOrderedItemsDTOS()) {
                for(OrderedItemsEntity itemsEntityTemp : entity.getOrderedItemsEntities())
                    if(itemsEntityTemp.getIdentifier().equals(item.getIdentifier()))
                        super.checkVersionSingleDTO(itemsEntityTemp, item);
            }
            super.checkVersionSingleDTO(entity, dto);
        }
    }
}
