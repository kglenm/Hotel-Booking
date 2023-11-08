package com.dijicert.booking.error;

@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BadRequestException(String message) {
        super(message);
    }
}
