package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class DTOVersionException extends AppBaseException {
    static final public String KEY_DTO_VERSION = "error.dto.version";

    public DTOVersionException() {
        super(KEY_DTO_VERSION);
    }

}
