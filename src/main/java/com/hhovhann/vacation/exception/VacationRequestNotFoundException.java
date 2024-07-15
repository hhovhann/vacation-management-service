package com.hhovhann.vacation.exception;

import java.io.Serial;

public class VacationRequestNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 6907734112021549712L;

    public VacationRequestNotFoundException() {
        super();
    }

    public VacationRequestNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public VacationRequestNotFoundException(String message) {
        super(message);
    }

    public VacationRequestNotFoundException(Throwable cause) {
        super(cause);
    }
}