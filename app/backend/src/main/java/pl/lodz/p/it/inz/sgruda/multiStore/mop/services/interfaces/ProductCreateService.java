package pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces;


import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.CategoryName;

public interface ProductCreateService {
    void createProduct(ProductEntity productEntity, CategoryName categoryName) throws AppBaseException;
}
