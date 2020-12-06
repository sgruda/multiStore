package pl.lodz.p.it.inz.sgruda.multiStore.mop.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.PromotionEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop.PromotionNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.repositories.PromotionRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces.PromotionActivityService;

@Log
@Service
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        transactionManager = "mopTransactionManager",
        timeout = 5
)
public class PromotionActivityServiceImpl implements PromotionActivityService {
    private PromotionRepository promotionRepository;

    @Autowired
    public PromotionActivityServiceImpl(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public PromotionEntity getPromotionByName(String name) throws PromotionNotExistsException {
        return promotionRepository.findByName(name)
                .orElseThrow(() -> new PromotionNotExistsException());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public void blockPromotion(PromotionEntity promotionEntity) {
        promotionEntity.setActive(false);
        promotionRepository.saveAndFlush(promotionEntity);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public void unblockPromotion(PromotionEntity promotionEntity) {
        promotionEntity.setActive(true);
        promotionRepository.saveAndFlush(promotionEntity);
    }
}