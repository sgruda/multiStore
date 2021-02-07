package pl.lodz.p.it.inz.sgruda.multiStore.utils.components.moz;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.BasketDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.OrderDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.OrderedItemDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.BasketEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderedItemEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.OptimisticLockAppException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOHashException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.CheckerSimpleDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.SignatureDTOUtil;
@Log
@Component
public class CheckerMozDTO extends CheckerSimpleDTO {
    @Autowired
    public CheckerMozDTO(SignatureDTOUtil signatureDTOUtil) {
        super(signatureDTOUtil);
    }

    public void checkOrderDTOSignature(OrderDTO dto) throws DTOHashException {
        if(dto != null) {
            for(OrderedItemDTO item : dto.getOrderedItemDTOS()) {
                super.checkSignatureSingleDTO(item);
                super.checkSignatureSingleDTO(item.getOrderedProduct());
            }
            super.checkSignatureSingleDTO(dto);
        }
    }

    public void checkBasketDTOSignature(BasketDTO dto) throws DTOHashException {
        if(dto != null) {
            for(OrderedItemDTO item : dto.getOrderedItemDTOS()) {
                super.checkSignatureSingleDTO(item);
                super.checkSignatureSingleDTO(item.getOrderedProduct());
            }
            super.checkSignatureSingleDTO(dto);
        }
    }

    public void checkOrderedItemDTOSignature(OrderedItemDTO dto) throws DTOHashException {
        if(dto != null) {
            super.checkSignatureSingleDTO(dto);
            super.checkSignatureSingleDTO(dto.getOrderedProduct());
        }
    }

    public void checkOrderDTOVersion(OrderEntity entity, OrderDTO dto) throws OptimisticLockAppException {
        if(dto != null && entity != null) {
            for(OrderedItemDTO item : dto.getOrderedItemDTOS()) {
                for(OrderedItemEntity itemsEntityTemp : entity.getOrderedItemEntities())
                    if(itemsEntityTemp.getIdentifier().equals(item.getIdentifier())) {
                        super.checkVersionSingleDTO(itemsEntityTemp, item);
                        super.checkVersionSingleDTO(itemsEntityTemp.getProductEntity(), item.getOrderedProduct());
                    }
            }
            super.checkVersionSingleDTO(entity, dto);
        }
    }
    public void checkBasketDTOVersion(BasketEntity entity, BasketDTO dto) throws OptimisticLockAppException {
        if(dto != null && entity != null) {
            for(OrderedItemDTO item : dto.getOrderedItemDTOS()) {
                for(OrderedItemEntity itemsEntityTemp : entity.getOrderedItemEntities())
                    if(itemsEntityTemp.getIdentifier().equals(item.getIdentifier())) {
                        super.checkVersionSingleDTO(itemsEntityTemp, item);
                        super.checkVersionSingleDTO(itemsEntityTemp.getProductEntity(), item.getOrderedProduct());
                    }
            }
            super.checkVersionSingleDTO(entity, dto);
        }
    }
    public void checkOrderedItemDTOVersion(OrderedItemEntity entity, OrderedItemDTO dto) throws OptimisticLockAppException {
        if(dto != null && entity != null) {
                super.checkVersionSingleDTO(entity, dto);
                super.checkVersionSingleDTO(entity.getProductEntity(), dto.getOrderedProduct());
        }
    }
}
