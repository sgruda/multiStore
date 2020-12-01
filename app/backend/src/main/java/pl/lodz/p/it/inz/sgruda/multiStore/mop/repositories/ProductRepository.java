package pl.lodz.p.it.inz.sgruda.multiStore.mop.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.ProductType;

import java.util.Optional;

@Repository
@Transactional(
        propagation = Propagation.MANDATORY,
        transactionManager = "mopTransactionManager"
)
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Boolean existsByTitle(String title);

    Optional<ProductEntity> findByTitle(String title);

    Page<ProductEntity> findAllByActiveEqualsAndTypeEquals(Pageable pageable, boolean active, ProductType productType);
    Page<ProductEntity> findAllByTypeEquals(Pageable pageable, ProductType productType);

    @Query(value = "SELECT product FROM ProductEntity product " +
            "WHERE " +
            "product.type = :productType AND " +
            "(product.active = :active) AND " +
            "(UPPER(product.title) LIKE CONCAT('%', UPPER(:textToSearch), '%') " +
            "OR UPPER(product.description) LIKE CONCAT('%', UPPER(:textToSearch), '%')) ",

            countQuery = "SELECT count(product) FROM ProductEntity product " +
                    "WHERE " +
                    "product.type = :productType AND " +
                    "(product.active = :active) AND " +
                    "(UPPER(product.title) LIKE CONCAT('%', UPPER(:textToSearch), '%') " +
                    "OR UPPER(product.description) LIKE CONCAT('%', UPPER(:textToSearch), '%')) "
    )
    Page<ProductEntity> findByTextInTitleOrDescriptionAndFilteredByActiveAndType(String textToSearch, Pageable pageable, boolean active, ProductType productType);

    @Query(value = "SELECT product FROM ProductEntity product " +
            "WHERE " +
            "product.type = :productType AND " +
            "(UPPER(product.title) LIKE CONCAT('%', UPPER(:textToSearch), '%') " +
            "OR UPPER(product.description) LIKE CONCAT('%', UPPER(:textToSearch), '%')) ",

            countQuery = "SELECT count(product) FROM ProductEntity product " +
                    "WHERE " +
                    "product.type = :productType AND " +
                    "(UPPER(product.title) LIKE CONCAT('%', UPPER(:textToSearch), '%') " +
                    "OR UPPER(product.description) LIKE CONCAT('%', UPPER(:textToSearch), '%')) "
    )
    Page<ProductEntity> findByTextInTitleOrDescriptionAndFilteredByType(String textToSearch, Pageable pageable, ProductType productType);

    @Query(value = "SELECT product FROM ProductEntity product " +
            "WHERE " +
            "(UPPER(product.title) LIKE CONCAT('%', UPPER(:textToSearch), '%') " +
            "OR UPPER(product.description) LIKE CONCAT('%', UPPER(:textToSearch), '%'))",
            countQuery = "SELECT count(product) FROM ProductEntity product " +
                    "WHERE " +
                    "(UPPER(product.title) LIKE CONCAT('%', UPPER(:textToSearch), '%') " +
                    "OR UPPER(product.description) LIKE CONCAT('%', UPPER(:textToSearch), '%')) "
    )
    Page<ProductEntity> findByTextInTitleOrDescription(String textToSearch, Pageable pageable);

    @Query(value = "SELECT product FROM ProductEntity product " +
            "WHERE " +
            "product.active = :active AND " +
            "(UPPER(product.title) LIKE CONCAT('%', UPPER(:textToSearch), '%') " +
            "OR UPPER(product.description) LIKE CONCAT('%', UPPER(:textToSearch), '%')) ",

            countQuery = "SELECT count(product) FROM ProductEntity product " +
                    "WHERE " +
                    "product.active = :active AND " +
                    "(UPPER(product.title) LIKE CONCAT('%', UPPER(:textToSearch), '%') " +
                    "OR UPPER(product.description) LIKE CONCAT('%', UPPER(:textToSearch), '%')) "
    )
    Page<ProductEntity> findByTextInTitleOrDescriptionAndFilteredByActive(String textToSearch, Pageable pageable, boolean active);

    Page<ProductEntity> findAllByActiveEquals(Pageable pageable, boolean active);
}
