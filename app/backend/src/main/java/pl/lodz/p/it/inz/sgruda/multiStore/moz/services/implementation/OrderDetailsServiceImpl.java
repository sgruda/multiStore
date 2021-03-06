package pl.lodz.p.it.inz.sgruda.multiStore.moz.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.OptimisticLockAppException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.OrderNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.repositories.OrderRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces.OrderDetailsService;


@Log
@Service
@Retryable(
        maxAttempts = 5,
        backoff = @Backoff(delay = 10000),
        exclude = {AppBaseException.class}
)
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        transactionManager = "mozTransactionManager",
        timeout = 5,
        rollbackFor = {OptimisticLockAppException.class}
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
