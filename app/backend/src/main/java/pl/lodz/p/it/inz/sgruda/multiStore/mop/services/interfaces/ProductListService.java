package pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;

public interface ProductListService {
    Page<ProductEntity> getFilteredProducts(String textToSearch, Pageable pageable, Boolean active, String productType);
}
