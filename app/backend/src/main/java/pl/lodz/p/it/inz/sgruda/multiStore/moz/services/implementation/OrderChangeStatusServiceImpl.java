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
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.StatusEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.OrderNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.StatusNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.repositories.OrderRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.repositories.StatusRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces.OrderChangeStatusService;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.NextStatusGetter;

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
        transactionManager = "mozTransactionManager",
        timeout = 5
)
public class OrderChangeStatusServiceImpl implements OrderChangeStatusService {
    private OrderRepository orderRepository;
    private StatusRepository statusRepository;

    @Autowired
    public OrderChangeStatusServiceImpl(OrderRepository orderRepository, StatusRepository statusRepository) {
        this.orderRepository = orderRepository;
        this.statusRepository = statusRepository;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public OrderEntity getOrderByIdentifier(String identifier) throws OrderNotExistsException {
        return orderRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new OrderNotExistsException());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public void changeStatus(OrderEntity orderEntity) throws AppBaseException {
        NextStatusGetter nextStatusGetter = new NextStatusGetter();
        StatusEntity statusEntity = statusRepository.findByStatusName(
                nextStatusGetter.getNextStatusName(
                        orderEntity.getStatusEntity().getStatusName()
                )
        )
        .orElseThrow(() -> new StatusNotExistsException());

        orderEntity.setStatusEntity(statusEntity);
        orderRepository.saveAndFlush(orderEntity);
    }
}
