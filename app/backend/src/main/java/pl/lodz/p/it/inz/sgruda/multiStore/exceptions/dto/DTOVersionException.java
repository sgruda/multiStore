package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto;

public class DTOVersionException extends AppBaseException {
    static final public String KEY_DTO_VERSION = "error.dto.version";

    public DTOVersionException() {
        super(KEY_DTO_VERSION);
    }

    public DTOVersionException(String message) {
        super(message);
    }

    public DTOVersionException(Throwable cause) {
        super(KEY_DTO_VERSION, cause);
    }
}
