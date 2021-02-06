package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class DTOHashException extends AppBaseException {
    static final public String KEY_DTO_SIGNATURE = "error.dto.signature";

    public DTOHashException() {
        super(KEY_DTO_SIGNATURE);
    }

}
