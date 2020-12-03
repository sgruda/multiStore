package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class StatusNotExistsException extends AppBaseException {
    static final public String KEY_STATUS_NOT_EXISTS = "error.status.not.exists";

    public StatusNotExistsException() {
        super(KEY_STATUS_NOT_EXISTS);
    }
}
