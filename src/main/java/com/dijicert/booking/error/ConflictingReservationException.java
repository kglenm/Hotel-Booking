package com.dijicert.booking.error;

@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class ConflictingReservationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ConflictingReservationException() {
        super("Reservation conflicts with an existing reservation");
    }
}
