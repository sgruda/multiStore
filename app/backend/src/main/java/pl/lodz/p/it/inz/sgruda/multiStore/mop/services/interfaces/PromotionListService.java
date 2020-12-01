package pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.PromotionEntity;

import java.util.List;

public interface PromotionListService {
    List<PromotionEntity> getAllPromotions();
}
