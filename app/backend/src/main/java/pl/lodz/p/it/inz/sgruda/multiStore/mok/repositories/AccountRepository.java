package pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByEmail(String email);
    Boolean existsByEmail(String email);

}
