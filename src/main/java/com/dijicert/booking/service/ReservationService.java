package com.dijicert.booking.service;

import com.dijicert.booking.controller.ReservationController;
import com.dijicert.booking.domain.Reservation;
import com.dijicert.booking.domain.enumeration.Status;
import com.dijicert.booking.error.BadRequestException;
import com.dijicert.booking.error.ConflictingReservationException;
import com.dijicert.booking.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.dijicert.booking.service.util.ReservationUtil.isOverlapping;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final Logger log = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;

    public List<Reservation> list() {
        return reservationRepository.findAll();
    }

    private Reservation createOrUpdate(Reservation reservation) throws ConflictingReservationException {
        if (isOverlapping(reservation, reservationRepository) && reservation.getId() == null) {
            log.error("Reservation is overlapping another reservation");
            throw new ConflictingReservationException();
        }

        // default status to awaiting payment
        reservation.setStatus(Status.AWAITING_PAYMENT);

        return reservationRepository.save(reservation);
    }

    public Reservation create(Reservation reservation) {
        if (reservation.getId() != null) {
            log.error("Id must be null for new creation of a reservation");
            throw new BadRequestException("Id must be null for new creation of a reservation");
        }
        log.info("Creating new reservation: {}", reservation);
        return createOrUpdate(reservation);

    }

    public Reservation update(Reservation reservation) {
        log.info("Updating reservation: {}", reservation);
        return createOrUpdate(reservation);
    }

    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }
}
