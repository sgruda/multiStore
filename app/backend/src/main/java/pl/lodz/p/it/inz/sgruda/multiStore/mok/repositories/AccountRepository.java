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

    @Query("SELECT account FROM AccountEntity account " +
            "WHERE account.authenticationDataEntity.veryficationToken = :token")
    Optional<AccountEntity> findByVeryficationToken(@Param("token") String token);

    @Query("SELECT account FROM AccountEntity account " +
            "WHERE account.authenticationDataEntity.username = :username")
    Optional<AccountEntity> findByUsername(@Param("username") String username);

    @Query("SELECT CASE WHEN COUNT(account) > 0 " +
            "THEN true ELSE false END " +
            "FROM AccountEntity account " +
            "WHERE account.authenticationDataEntity.username = :username")
    Boolean existsByUsername(@Param("username") String username);

    Boolean existsByEmail(String email);


    @Query(value = "SELECT account FROM AccountEntity account " +
            "WHERE " +
                "(UPPER(account.firstName) LIKE CONCAT('%', UPPER(:textToSearch), '%') " +
                "OR UPPER(account.lastName) LIKE CONCAT('%', UPPER(:textToSearch), '%') " +
                "OR UPPER(account.email) LIKE CONCAT('%', UPPER(:textToSearch), '%'))",
            countQuery = "SELECT count(account) FROM AccountEntity account " +
                    "WHERE " +
                        "(UPPER(account.firstName) LIKE CONCAT('%', UPPER(:textToSearch), '%') " +
                        "OR UPPER(account.lastName) LIKE CONCAT('%', UPPER(:textToSearch), '%') " +
                        "OR UPPER(account.email) LIKE CONCAT('%', UPPER(:textToSearch), '%')) "
                    )
    Page<AccountEntity> findByTextInNameOrEmail(String textToSearch, Pageable pageable);

    @Query(value = "SELECT account FROM AccountEntity account " +
            "WHERE  " +
                "(UPPER(account.firstName) LIKE CONCAT('%', UPPER(:textToSearch), '%') " +
                "OR UPPER(account.lastName) LIKE CONCAT('%', UPPER(:textToSearch), '%') " +
                "OR UPPER(account.email) LIKE CONCAT('%', UPPER(:textToSearch), '%')) " +
            "ORDER BY :sort")
    List<AccountEntity> findByTextInNameOrEmail(String textToSearch, Sort sort);

    @Query(value = "SELECT account FROM AccountEntity account " +
            "WHERE " +
                "account.active = :active AND " +
                    "(UPPER(account.firstName) LIKE CONCAT('%', UPPER(:textToSearch), '%') " +
                    "OR UPPER(account.lastName) LIKE CONCAT('%', UPPER(:textToSearch), '%') " +
                    "OR UPPER(account.email) LIKE CONCAT('%', UPPER(:textToSearch), '%')) ",

            countQuery = "SELECT count(account) FROM AccountEntity account " +
                    "WHERE " +
                        "account.active = :active AND " +
                            "(UPPER(account.firstName) LIKE CONCAT('%', UPPER(:textToSearch), '%') " +
                            "OR UPPER(account.lastName) LIKE CONCAT('%', UPPER(:textToSearch), '%') " +
                            "OR UPPER(account.email) LIKE CONCAT('%', UPPER(:textToSearch), '%')) "
                    )
    Page<AccountEntity> findByTextInNameOrEmailAndFilteredByActive(String textToSearch, Pageable pageable, boolean active);

    @Query(value = "SELECT account FROM AccountEntity account " +
            "WHERE " +
                "account.active = :active AND " +
                    "(UPPER(account.firstName) LIKE CONCAT('%', UPPER(:textToSearch), '%') " +
                    "OR UPPER(account.lastName) LIKE CONCAT('%', UPPER(:textToSearch), '%') " +
                    "OR UPPER(account.email) LIKE CONCAT('%', UPPER(:textToSearch), '%'))" +
            " ORDER BY :sort")
    List<AccountEntity> findByTextInNameOrEmailAndFilteredByActive(String textToSearch, Sort sort, boolean active);

    Page<AccountEntity> findAllByActiveEquals(Pageable pageable, boolean active);
    List<AccountEntity> findAllByActiveEquals(Sort sort, boolean active);
}
