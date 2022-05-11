package com.bzabek.ticketbookingapp.exceptions;

import static com.bzabek.ticketbookingapp.Configuration.LATE_RESERVATION_MESSAGE;

public class LateReservationException extends RuntimeException{
    public LateReservationException() {
        super(LATE_RESERVATION_MESSAGE);
    }
}
