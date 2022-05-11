package com.bzabek.ticketbookingapp.model;

import java.util.UUID;

public class MakeReservation {

    private UUID seatReservedId;
    private String name;
    private String surname;
    private TicketType ticketType;

    public MakeReservation() {
    }

    public MakeReservation(UUID seatReservedId, String name, String surname, TicketType ticketType) {
        this.seatReservedId = seatReservedId;
        this.name = name;
        this.surname = surname;
        this.ticketType = ticketType;
    }

    public void setSeatReservedId(UUID seatReservedId) {
        this.seatReservedId = seatReservedId;
    }

    public UUID getSeatReservedId() {
        return seatReservedId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public TicketType getTicketType() {
        return ticketType;
    }
}
