package pl.lodz.p.it.inz.sgruda.multiStore.moz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.CategoryEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.PromotionEntity;

import java.util.Collection;

@Repository
@Transactional(
        propagation = Propagation.MANDATORY,
        transactionManager = "mozTransactionManager"
)
public interface PromotionRepositoryMOZ extends JpaRepository<PromotionEntity, Long>  {
    Collection<PromotionEntity> findByCategoryEntity(CategoryEntity categoryEntity);
}
