package pl.lodz.p.it.inz.sgruda.multiStore.mop.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.PromotionEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.OptimisticLockAppException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop.PromotionIsActiveException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop.PromotionNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.repositories.PromotionRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces.PromotionDeleteService;

@Log
@Service
@Retryable(
        maxAttempts = 5,
        backoff = @Backoff(delay = 500),
        exclude = {AppBaseException.class}
)
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        transactionManager = "mopTransactionManager",
        timeout = 5,
        rollbackFor = {OptimisticLockAppException.class}
)
public class PromotionDeleteServiceImpl implements PromotionDeleteService {
    private PromotionRepository promotionRepository;

    @Autowired
    public PromotionDeleteServiceImpl(PromotionRepository promotionRepository) {
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
    public void deletePromotion(PromotionEntity promotionEntity) throws PromotionIsActiveException, OptimisticLockAppException {
        if(promotionEntity.isActive())
            throw new PromotionIsActiveException();
        try{
            promotionRepository.delete(promotionEntity);
        }
        catch(OptimisticLockingFailureException ex){
            throw new OptimisticLockAppException();
        }
    }
}
