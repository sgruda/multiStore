package pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces;

import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.BasketEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.BasketNotExistsException;

public interface BasketDetailsService {
    BasketEntity getBasketEntityByOwnerEmail(String email) throws BasketNotExistsException;
}
