package pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;

import java.util.Optional;

@Repository
@Transactional(
        propagation = Propagation.MANDATORY
)
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByEmail(String email);

//    Optional<AccountEntity> findByUsernameOrEmail(String username, String email);

//    List<AccountEntity> findByIdIn(List<Long> userIds);
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

}
