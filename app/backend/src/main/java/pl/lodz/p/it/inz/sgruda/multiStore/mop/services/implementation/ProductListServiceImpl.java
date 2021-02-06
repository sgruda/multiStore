package pl.lodz.p.it.inz.sgruda.multiStore.mop.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.OptimisticLockAppException;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.repositories.ProductRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces.ProductListService;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.ProductType;

@Log
@Service
@Retryable(
        maxAttempts = 5,
        backoff = @Backoff(delay = 500)
)
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        transactionManager = "mopTransactionManager",
        timeout = 5,
        rollbackFor = {OptimisticLockAppException.class}
)
public class ProductListServiceImpl implements ProductListService {
    private ProductRepository productRepository;

    @Autowired
    public ProductListServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<ProductEntity> getFilteredProducts(String textToSearch, Pageable pageable, Boolean active, ProductType productType) {
        if(textToSearch != null) {
            if(active != null && productType == null)
                return productRepository.findByTextInTitleOrDescriptionAndFilteredByActive(textToSearch, pageable, active.booleanValue());
            else if(active == null && productType != null)
                return productRepository.findByTextInTitleOrDescriptionAndFilteredByType(textToSearch, pageable, productType);
            else if(active != null && productType != null)
                return productRepository.findByTextInTitleOrDescriptionAndFilteredByActiveAndType(textToSearch, pageable, active.booleanValue(), productType);
            else
                return productRepository.findByTextInTitleOrDescription(textToSearch, pageable);
        } else {
            if(active != null && productType == null)
                return productRepository.findAllByActiveEquals(pageable, active.booleanValue());
            else if(active == null && productType != null)
                return productRepository.findAllByTypeEquals(pageable, productType);
            else if(active != null && productType != null)
                return productRepository.findAllByActiveEqualsAndTypeEquals(pageable, active.booleanValue(), productType);
            else
                return productRepository.findAll(pageable);
        }
    }
}
