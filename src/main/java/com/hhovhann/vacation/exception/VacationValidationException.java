package com.hhovhann.vacation.exception;

import java.io.Serial;

public class VacationValidationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4622232861533843037L;

    public VacationValidationException() {
        super();
    }

    public VacationValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public VacationValidationException(String message) {
        super(message);
    }

    public VacationValidationException(Throwable cause) {
        super(cause);
    }
}