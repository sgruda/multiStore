package pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.ForgotPasswordTokenEntity;

@Repository
@Transactional(
        propagation = Propagation.MANDATORY
)
public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordTokenEntity, Long> {
}
