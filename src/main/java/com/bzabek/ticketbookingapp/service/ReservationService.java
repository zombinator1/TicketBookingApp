package com.bzabek.ticketbookingapp.service;

import com.bzabek.ticketbookingapp.entities.Reservation;
import com.bzabek.ticketbookingapp.entities.SeatReserved;
import com.bzabek.ticketbookingapp.exceptions.SeatIsAlreadyReservedException;
import com.bzabek.ticketbookingapp.exceptions.SinglePlaceLeftOverException;
import com.bzabek.ticketbookingapp.exceptions.LateReservationException;
import com.bzabek.ticketbookingapp.model.MakeReservation;
import com.bzabek.ticketbookingapp.model.ReservationCreated;
import com.bzabek.ticketbookingapp.model.TicketType;
import com.bzabek.ticketbookingapp.repository.ReservationSpringDataRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.bzabek.ticketbookingapp.Configuration.MINIMUM_TIME_BEFORE_SCREENING_MIN;

@Service
public class ReservationService {

    private ReservationSpringDataRepository reservationRepository;
    private ScreeningService screeningService;
    private SeatsService seatsService;

    public ReservationService(ReservationSpringDataRepository reservationRepository, ScreeningService screeningService, SeatsService seatsService) {
        this.reservationRepository = reservationRepository;
        this.screeningService = screeningService;
        this.seatsService = seatsService;
    }

    public List<SeatReserved> freeSeats(UUID screeningId) {
        return seatsService.freeSeats(screeningId);
    }

    public synchronized List<Reservation> reserveTickets(List<MakeReservation> makeReservations) {
        if (makeReservations.isEmpty()) {
            throw new RuntimeException("Empty reservation request");
        }
        List<Reservation> reservations = new ArrayList<>();

        List<UUID> newSeatsIds = makeReservations.stream().map(MakeReservation::getSeatReservedId).collect(Collectors.toList());
        List<SeatReserved> newSeats = seatsService.findAllById(newSeatsIds);
        //1 CHECK IF IT IS NOT TOO LATE
        isItToLate(newSeats);
        //2 VALIDATE SEATS, CHECk IF SEATS ARE ALREADY RESERVED AND FORBID TO LEAVE A SINGLE PLACE BETWEEN
        seatsValidation(newSeats);
        //3 VALIDATE NAMES AND SURNAMES
        for(MakeReservation makeReservation:makeReservations){
            NameAndSurnameValidator.validate(makeReservation.getName());
            NameAndSurnameValidator.validate(makeReservation.getSurname());
        }

        for (int i = 0; i < makeReservations.size(); i++) {
            reservations.add(Reservation.builder()
                    .setSeatReserved(newSeats.get(i))
                    .setViewerSurname(makeReservations.get(i).getSurname())
                    .setViewerName(makeReservations.get(i).getName())
                    .setTicketType(makeReservations.get(i).getTicketType())
                    .setActive(true)
                    .setExpirationTime()
                    .build()
            );
        }

        reservationRepository.saveAll(reservations);
        seatsService.saveAll(newSeats);
        return reservations;
    }

    @Transactional
    public ReservationCreated makeReservation(List<MakeReservation> request) {
       List<Reservation> reservations = reserveTickets(request);

        LocalDateTime expirationTime = reservations.get(0).getExpirationTime();
        BigDecimal totalPrice = calculateTotalPrice(reservations);
        return new ReservationCreated(totalPrice,expirationTime);
    }

    private BigDecimal calculateTotalPrice(List<Reservation> reservations) {
        return reservations
                .stream()
                .map(this::priceByTicket)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal priceByTicket(Reservation reservation) {
        TicketType ticketType = reservation.getTicketType();
        if (TicketType.ADULT.equals(ticketType)) {
            return reservation.getSeatReserved().getScreening().getAdultTicketPrice();
        } else if (TicketType.STUDENT.equals(ticketType)) {
            return reservation.getSeatReserved().getScreening().getStudentTicketPrice();
        } else if (TicketType.CHILD.equals(ticketType)) {
            return reservation.getSeatReserved().getScreening().getChildTicketPrice();
        } else {
            throw new RuntimeException("Incorrect ticket type");
        }
    }

    public void isItToLate(List<SeatReserved> newSeats) {
        if (newSeats.isEmpty()) {
            throw new RuntimeException("Empty reservation request");
        }
        LocalDateTime screeningStart = newSeats.get(0).getScreening().getScreeningStartTime();
        if (LocalDateTime.now().isAfter(screeningStart.minusMinutes(MINIMUM_TIME_BEFORE_SCREENING_MIN))) {
            throw new LateReservationException();
        }

    }

    public void isSeatReserved(List<SeatReserved> newSeats) {
        if (newSeats.isEmpty()) {
            throw new RuntimeException("Empty reservation request");
        }
        for (SeatReserved seat : newSeats) {
            if (seat.isReserved()) {
                throw new SeatIsAlreadyReservedException();
            }
        }
    }

    public void seatsValidation(List<SeatReserved> newSeats) {
        if (newSeats.isEmpty()) {
            throw new RuntimeException("Empty reservation request");
        }
        isSeatReserved(newSeats);

        for (SeatReserved seatReserved : newSeats) {
            seatReserved.setReserved(true);
        }
        List<Long> rowNumbers = newSeats.stream().map(SeatReserved::getRowNr).distinct().collect(Collectors.toList());

        List<SeatReserved> seatsInTheSameRows = seatsService.findSeatsInTheSameRows(newSeats);
        seatsInTheSameRows.addAll(newSeats);

        for (Long rowNr : rowNumbers) {
            List<SeatReserved> seatsFromOneRow = new ArrayList<>();
            for (SeatReserved seat : seatsInTheSameRows) {
                if (seat.getRowNr() == rowNr) {
                    seatsFromOneRow.add(seat);
                }
            }
            seatsFromOneRow = seatsFromOneRow.stream().sorted((s1, s2) -> Long.compare(s1.getNumber(), s2.getNumber())).collect(Collectors.toList());
            if (seatsFromOneRow.size() == 1) {
                return;
            } else if (seatsFromOneRow.size() == 2) {
                if (seatsFromOneRow.get(1).getNumber() - seatsFromOneRow.get(0).getNumber() == 2) {
                    throw new SinglePlaceLeftOverException();
                }
            } else {
                for (int i = 1; i < seatsFromOneRow.size(); i++) {
                    if (seatsFromOneRow.get(i).getNumber() - seatsFromOneRow.get(i - 1).getNumber() == 2) {
                        throw new SinglePlaceLeftOverException();
                    }
                }
            }
        }


    }




}
