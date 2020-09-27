package pl.lodz.p.it.inz.sgruda.multiStore.exceptions;

public class DTOSignatureException extends AppBaseException {
    static final public String KEY_DEFAULT = "error.dto.signature";

    public DTOSignatureException() {
        super(KEY_DEFAULT);
    }

    public DTOSignatureException(String message) {
        super(message);
    }

    public DTOSignatureException(Throwable cause) {
        super(KEY_DEFAULT, cause);
    }
}
