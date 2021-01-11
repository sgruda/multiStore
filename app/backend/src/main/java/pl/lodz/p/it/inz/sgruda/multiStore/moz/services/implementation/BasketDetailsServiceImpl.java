package pl.lodz.p.it.inz.sgruda.multiStore.moz.services.implementation;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.BasketEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.BasketNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.repositories.BasketRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces.BasketDetailsService;

@Log
@Service
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW,
        transactionManager = "mozTransactionManager",
        timeout = 5
)
public class BasketDetailsServiceImpl implements BasketDetailsService {
    private BasketRepository basketRepository;

    @Autowired
    public BasketDetailsServiceImpl(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public BasketEntity getBasketEntityByOwnerEmail(String email) throws BasketNotExistsException {
        return basketRepository.findByAccountEntityEmail(email)
                .orElseThrow(() -> new BasketNotExistsException());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public int getBasketSizeByOwnerEmail(String email) throws BasketNotExistsException {
        return basketRepository.findByAccountEntityEmail(email)
                .orElseThrow(() -> new BasketNotExistsException())
                .getOrderedItemEntities()
                .size();
    }
}


