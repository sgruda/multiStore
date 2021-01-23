package pl.lodz.p.it.inz.sgruda.multiStore.mop.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.CategoryEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.PromotionEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop.CategoryNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop.PromotionNameAlreadyExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.repositories.CategoryRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.repositories.PromotionRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces.PromotionCreateService;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.CategoryName;

import java.util.Optional;

@Log
@Service
@Retryable(
        maxAttempts = 5,
        backoff = @Backoff(delay = 500)
)
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        transactionManager = "mopTransactionManager",
        timeout = 5
)
public class PromotionCreateServiceImpl implements PromotionCreateService {
    private PromotionRepository promotionRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public PromotionCreateServiceImpl(PromotionRepository promotionRepository, CategoryRepository categoryRepository) {
        this.promotionRepository = promotionRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public void createPromotion(PromotionEntity promotionEntity, CategoryName categoryName) throws AppBaseException {
        if(promotionRepository.existsByName(promotionEntity.getName())) {
            throw new PromotionNameAlreadyExistsException();
        }
        Optional<CategoryEntity> optionalCategoryEntity = categoryRepository.findByCategoryName(categoryName);
        if(optionalCategoryEntity.isPresent()) {
            CategoryEntity categoryEntity = optionalCategoryEntity.get();
            promotionEntity.setCategoryEntity(categoryEntity);
            promotionRepository.saveAndFlush(promotionEntity);
        } else
            throw new CategoryNotExistsException();
    }
}
