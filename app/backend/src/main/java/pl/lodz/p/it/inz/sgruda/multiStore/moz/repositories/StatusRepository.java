package pl.lodz.p.it.inz.sgruda.multiStore.moz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.StatusEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.StatusName;

import java.util.Optional;

@Repository
@Transactional(
        propagation = Propagation.MANDATORY,
        transactionManager = "mozTransactionManager"
)
public interface StatusRepository extends JpaRepository<StatusEntity, Long> {
    Optional<StatusEntity> findByStatusName(StatusName statusName);
}
