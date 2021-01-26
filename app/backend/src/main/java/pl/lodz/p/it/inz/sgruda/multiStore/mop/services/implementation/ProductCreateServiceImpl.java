package pl.lodz.p.it.inz.sgruda.multiStore.mop.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.CategoryEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop.CategoryNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop.TitleAlreadyExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.repositories.CategoryRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.repositories.ProductRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces.ProductCreateService;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.CategoryName;

import java.util.Optional;

@Log
@Service
@Retryable(
        maxAttempts = 5,
        backoff = @Backoff(delay = 500),
        exclude = {AppBaseException.class}
)
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        transactionManager = "mopTransactionManager",
        timeout = 5
)
public class ProductCreateServiceImpl implements ProductCreateService {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public ProductCreateServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public void createProduct(ProductEntity productEntity, CategoryName categoryName) throws AppBaseException {
        if(productRepository.existsByTitle(productEntity.getTitle())) {
            throw new TitleAlreadyExistsException();
        }
        Optional<CategoryEntity> optionalCategoryEntity = categoryRepository.findByCategoryName(categoryName);
        if(optionalCategoryEntity.isPresent()) {
            CategoryEntity categoryEntity = optionalCategoryEntity.get();
            productEntity.setCategoryEntity(categoryEntity);
            productRepository.saveAndFlush(productEntity);
        } else
            throw new CategoryNotExistsException();
    }
}
