package pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.PromotionEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop.PromotionNotExistsException;

public interface PromotionActivityService {
    PromotionEntity getPromotionByName(String name) throws PromotionNotExistsException;
    void blockPromotion(PromotionEntity promotionEntity);
    void unblockPromotion(PromotionEntity promotionEntity);
}
