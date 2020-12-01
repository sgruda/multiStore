package pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.PromotionEntity;

public interface PromotionListService {
    Page<PromotionEntity> getAllPromotions(Pageable pageable);
}
