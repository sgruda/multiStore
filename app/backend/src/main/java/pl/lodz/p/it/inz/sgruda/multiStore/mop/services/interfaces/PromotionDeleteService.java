package pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.PromotionEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop.PromotionIsActiveException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop.PromotionNotExistsException;

public interface PromotionDeleteService {
    PromotionEntity getPromotionByName(String name) throws PromotionNotExistsException;
    void deletePromotion(PromotionEntity promotionEntity) throws PromotionIsActiveException;
}
