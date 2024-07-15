package com.hhovhann.vacation.exception;

import java.io.Serial;

public class EmployeeValidationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5433425339505749410L;

    public EmployeeValidationException() {
        super();
    }

    public EmployeeValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmployeeValidationException(String message) {
        super(message);
    }

    public EmployeeValidationException(Throwable cause) {
        super(cause);
    }
}