package pl.lodz.p.it.inz.sgruda.multiStore.moz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;

import java.util.Optional;

@Repository
@Transactional(
        propagation = Propagation.MANDATORY,
        transactionManager = "mozTransactionManager"
)
public interface ProductRepositoryMOZ extends JpaRepository<ProductEntity, Long>  {
    Optional<ProductEntity> findByTitle(String title);
}
