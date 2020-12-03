package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.http;

import org.springframework.http.HttpStatus;

public class UnauthorizedRequestException extends HttpBaseException {
    static final public HttpStatus HTTP_STATUS = HttpStatus.UNAUTHORIZED;
    static final public String KEY_HTTP_UNAUTHORIZED_REQUEST = "error.http.401";

    public UnauthorizedRequestException() {
        super(HTTP_STATUS, KEY_HTTP_UNAUTHORIZED_REQUEST);
    }
}
