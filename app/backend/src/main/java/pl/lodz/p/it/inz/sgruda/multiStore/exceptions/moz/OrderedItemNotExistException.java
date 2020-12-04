package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class OrderedItemNotExistException extends AppBaseException {
    static final public String KEY_ORDERED_ITEM_NOT_EXISTS = "error.ordered.item.not.exists";

    public OrderedItemNotExistException() {
        super(KEY_ORDERED_ITEM_NOT_EXISTS);
    }
}
