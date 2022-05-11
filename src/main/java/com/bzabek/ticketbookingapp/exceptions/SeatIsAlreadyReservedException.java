package com.bzabek.ticketbookingapp.exceptions;

public class SeatIsAlreadyReservedException extends RuntimeException{
    public SeatIsAlreadyReservedException() {
        super("Seat is already reserved");
    }
}
