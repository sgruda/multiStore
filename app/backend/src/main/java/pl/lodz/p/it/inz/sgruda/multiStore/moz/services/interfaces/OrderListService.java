package pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderEntity;

public interface OrderListService {
    Page<OrderEntity> getOrderListPage(Pageable pageable);
}
