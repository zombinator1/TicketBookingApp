package com.bzabek.ticketbookingapp.model;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReservationCreated {

    private BigDecimal totalPrice;
    private LocalDateTime expirationTime;

    public ReservationCreated(BigDecimal totalPrice, LocalDateTime expirationTime) {
        this.totalPrice = totalPrice;
        this.expirationTime = expirationTime;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }


}
