package pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.OptimisticLockAppException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop.ProductIsActiveException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop.ProductNotExistsException;

public interface ProductEditService {
    void editProduct(ProductEntity productEntity) throws ProductIsActiveException, OptimisticLockAppException;
    ProductEntity getProductByTitle(String title) throws ProductNotExistsException;
}
