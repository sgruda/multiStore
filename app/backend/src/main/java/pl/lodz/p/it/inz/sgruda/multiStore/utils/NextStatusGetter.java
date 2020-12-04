package pl.lodz.p.it.inz.sgruda.multiStore.utils;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.CurrentOrderStatusIsFinalException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.StatusName;

public class NextStatusGetter {
    public StatusName getNextStatusName(StatusName presentStatusName) throws CurrentOrderStatusIsFinalException {
        return switch(presentStatusName) {
            case submitted -> StatusName.prepared;
            case prepared -> StatusName.send;
            case send -> StatusName.delivered;
            case delivered -> throw new CurrentOrderStatusIsFinalException();
        };
    }
}
