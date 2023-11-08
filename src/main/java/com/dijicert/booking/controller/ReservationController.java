package com.dijicert.booking.controller;

import com.dijicert.booking.domain.Reservation;
import com.dijicert.booking.error.BadRequestException;
import com.dijicert.booking.error.ConflictingReservationException;
import com.dijicert.booking.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final Logger log = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;

    @GetMapping("/list")
    public ResponseEntity<List<Reservation>> list() {
        return new ResponseEntity<>(reservationService.list(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Reservation> create(@RequestBody Reservation reservation) throws ConflictingReservationException {
        log.info("Request to create reservation: {}", reservation);
        return new ResponseEntity<>(reservationService.create(reservation), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Reservation> update(@RequestBody Reservation reservation) {
        log.info("Request to update reservation: {}", reservation);
        return new ResponseEntity<>(reservationService.update(reservation), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Request to delete reservation with id: {}", id);
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
