package com.bzabek.ticketbookingapp.controller;

import com.bzabek.ticketbookingapp.entities.Screening;
import com.bzabek.ticketbookingapp.entities.SeatReserved;
import com.bzabek.ticketbookingapp.model.MakeReservation;
import com.bzabek.ticketbookingapp.model.ReservationCreated;
import com.bzabek.ticketbookingapp.service.ReservationService;
import com.bzabek.ticketbookingapp.service.ScreeningService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("reservation")
public class ReservationController {

    private ReservationService reservationService;
    private ScreeningService screeningService;

    public ReservationController(ReservationService reservationService, ScreeningService screeningService) {
        this.reservationService = reservationService;
        this.screeningService = screeningService;
    }


    @GetMapping(path = "/screenings")
    public ResponseEntity<List<Screening>> getAllScreenings(@RequestParam String from, @RequestParam String to){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(screeningService.findScreenings(from, to));
    }

    @GetMapping("/free_seats")
    public ResponseEntity<List<SeatReserved>> freeSeats(@RequestParam UUID screeningId){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationService.freeSeats(screeningId));
    }

    @PostMapping("/reserve_tickets")
    public ResponseEntity<ReservationCreated> reserveTickets(@RequestBody List<MakeReservation> makeReservations){
       try {
          return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationService.makeReservation(makeReservations));
       } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,e.getMessage(),e);
           }

    }
}
