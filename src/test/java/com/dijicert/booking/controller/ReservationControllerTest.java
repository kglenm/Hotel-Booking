package com.dijicert.booking.controller;

import com.dijicert.booking.IntegrationTest;
import com.dijicert.booking.domain.Hotel;
import com.dijicert.booking.domain.Reservation;
import com.dijicert.booking.domain.User;
import com.dijicert.booking.domain.enumeration.Status;
import com.dijicert.booking.repository.ReservationRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@IntegrationTest
class ReservationControllerTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReservationMockMvc;

    private Reservation reservation;
    private User user = new User();
    private Hotel hotel = new Hotel();

    public Reservation createReservation() {
        user.setId(1L);
        hotel.setId(1L);

        reservation = new Reservation();
        reservation.setStartDateTime(Instant.parse("2021-01-01T00:00:00Z"));
        reservation.setEndDateTime(Instant.parse("2021-01-02T00:00:00Z"));
        reservation.setRoomNumber("r101");
        reservation.setUser(user);
        reservation.setHotel(hotel);

        return reservationRepository.save(reservation);
    }

    @Test
    @Transactional
    void should_list_all_reservations() throws Exception {
        createReservation();
        restReservationMockMvc.perform(get("/api/reservation/list").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].startDateTime").value(hasItem("2021-01-01T00:00:00Z")))
                .andExpect(jsonPath("$.[*].endDateTime").value(hasItem("2021-01-02T00:00:00Z")));

        reservationRepository.delete(reservation);
    }

    @Test
    @Transactional
    void should_create_new_reservation() throws Exception {
        user.setId(2L);
        hotel.setId(2L);

        Reservation newReservation = new Reservation();
        newReservation.setStartDateTime(Instant.parse("2024-01-01T00:00:00Z"));
        newReservation.setEndDateTime(Instant.parse("2024-01-02T00:00:00Z"));
        newReservation.setRoomNumber("r103");
        newReservation.setUser(user);
        newReservation.setHotel(hotel);

        restReservationMockMvc.perform(post("/api/reservation/create").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(newReservation)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(Status.AWAITING_PAYMENT.name()))
                .andExpect(jsonPath("$.roomNumber").value("r103"))
                .andExpect(jsonPath("$.startDateTime").value("2024-01-01T00:00:00Z"))
                .andExpect(jsonPath("$.endDateTime").value("2024-01-02T00:00:00Z"));
    }

    @Test
    @Transactional
    void should_fail_to_create_reservation_with_id() throws Exception {
        user.setId(2L);
        hotel.setId(2L);

        Reservation newReservation = new Reservation();
        newReservation.setId(1L);
        newReservation.setStartDateTime(Instant.parse("2024-01-01T00:00:00Z"));
        newReservation.setEndDateTime(Instant.parse("2024-01-02T00:00:00Z"));
        newReservation.setRoomNumber("r103");
        newReservation.setUser(user);
        newReservation.setHotel(hotel);

        restReservationMockMvc.perform(post("/api/reservation/create").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(newReservation)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.detail").value("Id must be null for new creation of a reservation"));

        reservationRepository.delete(newReservation);
    }

    @Test
    @Transactional
    void should_create_conflicting_reservation() throws Exception {
        createReservation();

        user.setId(2L);
        hotel.setId(1L);

        Reservation conflictingReservation = new Reservation();
        conflictingReservation.setStartDateTime(Instant.parse("2021-01-01T00:00:00Z"));
        conflictingReservation.setEndDateTime(Instant.parse("2021-01-02T00:00:00Z"));
        conflictingReservation.setRoomNumber("r101");
        conflictingReservation.setUser(user);
        conflictingReservation.setHotel(hotel);

        restReservationMockMvc.perform(post("/api/reservation/create").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conflictingReservation)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.detail").value("Reservation conflicts with an existing reservation"));

        reservationRepository.deleteAll(Arrays.asList(reservation, conflictingReservation));
    }

    @Test
    @Transactional
    void should_update_reservation_date() throws Exception {
        createReservation();
        reservation.setEndDateTime(Instant.parse("2021-01-03T00:00:00Z"));

        restReservationMockMvc.perform(put("/api/reservation/update").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservation)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.startDateTime").value("2021-01-01T00:00:00Z"))
                .andExpect(jsonPath("$.endDateTime").value("2021-01-03T00:00:00Z"));

        reservationRepository.delete(reservation);
    }

    @Test
    @Transactional
    void should_delete_reservation() throws Exception {
        createReservation();

        restReservationMockMvc.perform(delete("/api/reservation/delete/" + reservation.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertTrue(reservationRepository.findById(reservation.getId()).isEmpty());
    }

}