package pl.lodz.p.it.inz.sgruda.multiStore.mop.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.OptimisticLockAppException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop.ProductNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.repositories.ProductRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces.ProductGetService;

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
        timeout = 5,
        rollbackFor = {OptimisticLockAppException.class}
)
public class ProductGetServiceImpl implements ProductGetService {
    private ProductRepository productRepository;

    @Autowired
    public ProductGetServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductEntity getProductByTitle(String title) throws ProductNotExistsException {
        return productRepository.findByTitle(title)
                .orElseThrow(() -> new ProductNotExistsException());
    }
}
