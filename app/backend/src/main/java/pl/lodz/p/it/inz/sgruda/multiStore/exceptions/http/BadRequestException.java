package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.http;

import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpBaseException {
    static final public HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    static final public String KEY_BAD_REQUEST = "error.http.400";

    public BadRequestException() {
        super(HTTP_STATUS, KEY_BAD_REQUEST);
    }
}
