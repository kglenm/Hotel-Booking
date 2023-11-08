package com.dijicert.booking.repository;

import com.dijicert.booking.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    int countByRoomNumberAndStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(
            String roomNumber,
            Instant endDateTime,
            Instant startDateTime
    );
}
