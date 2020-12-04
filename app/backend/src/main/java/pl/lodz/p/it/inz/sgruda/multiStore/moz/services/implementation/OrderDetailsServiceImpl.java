package pl.lodz.p.it.inz.sgruda.multiStore.moz.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.OrderNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.repositories.OrderRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces.OrderDetailsService;


@Log
@Service
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        transactionManager = "mozTransactionManager",
        timeout = 5
)
public class OrderDetailsServiceImpl implements OrderDetailsService {
    private OrderRepository orderRepository;

    @Autowired
    public OrderDetailsServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_EMPLOYEE')")
    public OrderEntity getOrderByIdentifier(String identifier) throws OrderNotExistsException {
        return orderRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new OrderNotExistsException());
    }
}
