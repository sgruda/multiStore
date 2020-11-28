package pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.ProductType;

public interface ProductListService {
    Page<ProductEntity> getFilteredAccounts(String textToSearch, Pageable pageable, Boolean active, ProductType productType);
}
