package pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccessLevelEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.RoleName;

import java.util.Optional;

@Repository
public interface AccessLevelRepository  extends JpaRepository<AccessLevelEntity, Long> {
    Optional<AccessLevelEntity> findByName(RoleName roleName);
}