package com.bzabek.ticketbookingapp.exceptions;

public class SinglePlaceLeftOverException extends RuntimeException{
    public SinglePlaceLeftOverException() {
        super("Pleas don't leave single places between the seats");
    }

}
