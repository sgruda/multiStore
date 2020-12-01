package pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.PromotionEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.CategoryName;

public interface PromotionCreateService {
    void createPromotion(PromotionEntity productEntity, CategoryName categoryName) throws AppBaseException;

}
