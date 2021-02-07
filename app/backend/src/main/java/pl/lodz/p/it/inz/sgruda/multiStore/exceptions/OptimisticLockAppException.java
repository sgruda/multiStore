package pl.lodz.p.it.inz.sgruda.multiStore.exceptions;

public class OptimisticLockAppException extends AppBaseException {
    static final public String KEY_OPTIMISTIC_LOCK_DTO_VERSION = "error.dto.version";

    public OptimisticLockAppException() {
        super(KEY_OPTIMISTIC_LOCK_DTO_VERSION);
    }

}
