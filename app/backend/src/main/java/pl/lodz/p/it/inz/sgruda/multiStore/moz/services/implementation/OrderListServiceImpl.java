package pl.lodz.p.it.inz.sgruda.multiStore.moz.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.repositories.OrderRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces.OrderListService;

@Log
@Service
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        transactionManager = "mozTransactionManager",
        timeout = 5
)
public class OrderListServiceImpl implements OrderListService {
    private OrderRepository orderRepository;

    @Autowired
    public OrderListServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public Page<OrderEntity> getOrderListPage(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public Page<OrderEntity> getOrderListPageForEmail(Pageable pageable, String email) {
        return orderRepository.findAllByAccountEntityEmail(pageable, email);
    }
}
