package pl.lodz.p.it.inz.sgruda.multiStore.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AppBaseException extends Exception {
    static final public String KEY_DEFAULT = "error.default";

    public AppBaseException() {
        super(KEY_DEFAULT);
    }

    public AppBaseException(String message) {
        super(message);
    }

    public AppBaseException(String message, Throwable cause) {
        super(message, cause);
    }

}
