package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class DTOSignatureException extends AppBaseException {
    static final public String KEY_DTO_SIGNATURE = "error.dto.signature";

    public DTOSignatureException() {
        super(KEY_DTO_SIGNATURE);
    }

    public DTOSignatureException(String message) {
        super(message);
    }

    public DTOSignatureException(Throwable cause) {
        super(KEY_DTO_SIGNATURE, cause);
    }
}
