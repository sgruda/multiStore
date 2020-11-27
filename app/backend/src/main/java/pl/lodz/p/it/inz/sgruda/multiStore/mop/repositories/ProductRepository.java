package pl.lodz.p.it.inz.sgruda.multiStore.mop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;

@Repository
@Transactional(
        propagation = Propagation.MANDATORY,
        transactionManager = "mopTransactionManager"
)
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
