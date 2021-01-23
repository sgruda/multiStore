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
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop.ProductNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.repositories.ProductRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces.ProductActivityService;

@Log
@Service
@Retryable(
        maxAttempts = 5,
        backoff = @Backoff(delay = 2500)
)
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        transactionManager = "mopTransactionManager",
        timeout = 5
)
public class ProductActivityServiceImpl implements ProductActivityService {
    private ProductRepository productRepository;

    @Autowired
    public ProductActivityServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ProductEntity getProductByTitle(String title) throws ProductNotExistsException {
        return productRepository.findByTitle(title)
                .orElseThrow(() -> new ProductNotExistsException());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public void blockProduct(ProductEntity productEntity) {
        productEntity.setActive(false);
        productRepository.saveAndFlush(productEntity);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public void unblockProduct(ProductEntity productEntity) {
        productEntity.setActive(true);
        productRepository.saveAndFlush(productEntity);
    }
}
