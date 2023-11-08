package com.dijicert.booking.service.util;

import com.dijicert.booking.domain.Reservation;
import com.dijicert.booking.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationUtil {
    private final static Logger log = LoggerFactory.getLogger(ReservationUtil.class);
    public static boolean isOverlapping(Reservation reservation, ReservationRepository reservationRepository) {
        log.info("Checking if reservation is overlapping: {}", reservation);
        return reservationRepository.countByRoomNumberAndStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(
                reservation.getRoomNumber(),
                reservation.getEndDateTime(),
                reservation.getStartDateTime()
        ) > 0;
    }
}
