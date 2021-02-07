package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class DTOHashException extends AppBaseException {
    static final public String KEY_DTO_SIGNATURE = "error.dto.hash";

    public DTOHashException() {
        super(KEY_DTO_SIGNATURE);
    }

}
