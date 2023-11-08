package com.dijicert.booking.service.util;

import com.dijicert.booking.domain.Reservation;
import com.dijicert.booking.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ReservationUtilTest {

    @MockBean
    ReservationRepository reservationRepository;

    private final Reservation reservation = new Reservation();

    @BeforeEach
    public void setUp() {

        reservation.setStartDateTime(Instant.parse("2021-01-01T00:00:00Z"));
        reservation.setEndDateTime(Instant.parse("2021-01-02T00:00:00Z"));
        reservation.setRoomNumber("r101");

        when(reservationRepository.countByRoomNumberAndStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(
                reservation.getRoomNumber(),
                reservation.getEndDateTime(),
                reservation.getStartDateTime()
        )).thenReturn(1);

    }

    @Test
    void should_return_true_if_is_overlapping() {

        Reservation reservation = new Reservation();
        reservation.setStartDateTime(Instant.parse("2021-01-01T00:00:00Z"));
        reservation.setEndDateTime(Instant.parse("2021-01-02T00:00:00Z"));
        reservation.setRoomNumber("r101");

        assertTrue(ReservationUtil.isOverlapping(reservation, reservationRepository));
    }
}