package pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;

public class EmployeeServingOwnOrderException extends AppBaseException {
    static final public String KEY_EMPLOYEE_SERVING_OWN_ORDER = "error.employee.serving.own.order";

    public EmployeeServingOwnOrderException() {
        super(KEY_EMPLOYEE_SERVING_OWN_ORDER);
    }
}
