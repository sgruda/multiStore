package pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.OptimisticLockAppException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop.ProductNotExistsException;

public interface ProductActivityService {
    ProductEntity getProductByTitle(String title) throws ProductNotExistsException;
    void blockProduct(ProductEntity productEntity) throws OptimisticLockAppException;
    void unblockProduct(ProductEntity productEntity) throws OptimisticLockAppException;
}
