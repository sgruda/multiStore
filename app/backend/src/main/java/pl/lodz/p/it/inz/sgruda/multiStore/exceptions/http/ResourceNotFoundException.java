package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.http;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
public class ResourceNotFoundException extends HttpBaseException {
    static final public HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
    static final public String KEY_HTTP_RESOURCE_NOT_FOUND = "error.http.not.found";
//    private String resourceName;
//    private String fieldName;
//    private Object fieldValue;

    public ResourceNotFoundException() {
        super(HTTP_STATUS, KEY_HTTP_RESOURCE_NOT_FOUND);
    }
    public ResourceNotFoundException(HttpStatus status) {
        super(status, KEY_HTTP_RESOURCE_NOT_FOUND);
    }

    public ResourceNotFoundException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public ResourceNotFoundException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

//    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
//        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
//        this.resourceName = resourceName;
//        this.fieldName = fieldName;
//        this.fieldValue = fieldValue;
//    }
}
