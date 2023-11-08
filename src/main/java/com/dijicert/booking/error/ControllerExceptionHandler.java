package com.dijicert.booking.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictingReservationException.class)
    public Map<String, Object> handleConflictingReservationException(ConflictingReservationException ex) {
        return new HashMap<>() {{
            put("title", "Conflicting Reservation");
            put("status", 409);
            put("detail", ex.getMessage());
        }};
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public Map<String, Object> handleBadRequestException(BadRequestException ex) {
        return new HashMap<>() {{
            put("title", "Bad Request");
            put("status", 400);
            put("detail", ex.getMessage());
        }};
    }
}
