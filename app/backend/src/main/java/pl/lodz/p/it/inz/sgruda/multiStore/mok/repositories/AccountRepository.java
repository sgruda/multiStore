package pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(
        propagation = Propagation.MANDATORY
)
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByEmail(String email);

    @Query("select account from AccountEntity account " +
            "where account.authenticationDataEntity.veryficationToken = :token")
    Optional<AccountEntity> findByVeryficationToken(@Param("token") String token);

    @Query("select account from AccountEntity account " +
            "where account.authenticationDataEntity.username = :username")
    Optional<AccountEntity> findByUsername(@Param("username") String username);

    @Query("SELECT CASE WHEN COUNT(account) > 0 " +
            "THEN true ELSE false END " +
            "FROM AccountEntity account " +
            "WHERE account.authenticationDataEntity.username = :username")
    Boolean existsByUsername(@Param("username") String username);

    Boolean existsByEmail(String email);

    Page<AccountEntity> findByActive(boolean active, Pageable pageable);

    @Query(value = "SELECT account FROM AccountEntity account WHERE ((account.firstName LIKE %?1%) OR (account.lastName LIKE %?1%) OR (account.email LIKE %?1%))",
            countQuery = "SELECT count(account) FROM AccountEntity account WHERE ((account.firstName LIKE ?1) OR (account.lastName LIKE ?1) OR (account.email LIKE ?1))")
    Page<AccountEntity> findByTextInNameOrEmail(String textToSearch, Pageable pageable);

    @Query(value = "SELECT account FROM AccountEntity account WHERE ((account.firstName LIKE %?1%) OR (account.lastName LIKE %?1%) OR (account.email LIKE %?1%)) ORDER BY ?2")
    List<AccountEntity> findByTextInNameOrEmail(String textToSearch, Sort sort);
}
