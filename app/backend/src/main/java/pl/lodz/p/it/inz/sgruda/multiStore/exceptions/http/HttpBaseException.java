package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class HttpBaseException extends ResponseStatusException {
    static final public HttpStatus HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    static final public String KEY_HTTP_DEFAULT = "error.http.default";

    public HttpBaseException(String reason) {
        super(HTTP_STATUS, reason);
    }
    public HttpBaseException(HttpStatus status, String reason) {
        super(status, reason);
    }
}