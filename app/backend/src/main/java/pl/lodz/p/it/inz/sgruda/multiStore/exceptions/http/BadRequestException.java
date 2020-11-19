package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.http;

import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpBaseException {
    static final public HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    static final public String KEY_BAD_REQUEST = "error.request.bad";

    public BadRequestException() {
        super(HTTP_STATUS, KEY_BAD_REQUEST);
    }
    public BadRequestException(String reason) {
        super(HTTP_STATUS, reason);
    }
    public BadRequestException(HttpStatus status) {
        super(status);
    }

    public BadRequestException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public BadRequestException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
