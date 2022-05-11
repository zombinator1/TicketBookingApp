package com.bzabek.ticketbookingapp.entities;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class SeatReserved {

    @Id
    @GeneratedValue
    private UUID id;
    private boolean reserved;
    @OneToOne(mappedBy = "seatReserved")
    private Reservation reservation;
    @ManyToOne
    private Screening screening;
    private Long rowNr;
    private Long number;

    public SeatReserved() {
    }

    public SeatReserved(Screening screening, Seat seat) {
        this.screening = screening;
        this.rowNr = seat.getRowNr();
        this.number = seat.getNumber();
    }

    public SeatReserved(boolean reserved, Long rowNr, Long number) {
        this.reserved = reserved;
        this.rowNr = rowNr;
        this.number = number;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getRowNr() {
        return rowNr;
    }

    public void setRowNr(Long rowNr) {
        this.rowNr = rowNr;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Screening getScreening() {
        return screening;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
    }

}
