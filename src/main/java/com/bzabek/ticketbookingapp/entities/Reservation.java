package com.bzabek.ticketbookingapp.entities;

import com.bzabek.ticketbookingapp.model.TicketType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.bzabek.ticketbookingapp.Configuration.RESERVATION_EXPIRE_TIME_MIN;


@Entity
public class Reservation {
    @Id
    @GeneratedValue
    private UUID id;

    private boolean paid;
    private boolean active;
    private LocalDateTime expirationTime;
    private String viewerName;
    private String viewerSurname;
    @OneToOne
    private SeatReserved seatReserved;
    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }


    public String getViewerName() {
        return viewerName;
    }

    public void setViewerName(String viewerName) {
        this.viewerName = viewerName;
    }

    public String getViewerSurname() {
        return viewerSurname;
    }

    public void setViewerSurname(String viewerSurname) {
        this.viewerSurname = viewerSurname;
    }

    public SeatReserved getSeatReserved() {
        return seatReserved;
    }

    public void setSeatReserved(SeatReserved seatReserved) {
        this.seatReserved = seatReserved;
    }


    public static ReservationBuilder builder(){
        return new ReservationBuilder();
    }

    public static class ReservationBuilder{
        private boolean paid;
        private boolean active;
        private LocalDateTime expirationTime;

        private String viewerName;
        private String viewerSurname;

        private SeatReserved seatReserved;
        private TicketType ticketType;

        public Reservation build(){
            Reservation reservation = new Reservation();
            reservation.paid = this.paid;
            reservation.active = this.active ;
            reservation.expirationTime = this.expirationTime;
            reservation.viewerName = this.viewerName;
            reservation.viewerSurname = this.viewerSurname;
            reservation.seatReserved = this.seatReserved;
            reservation.ticketType = this.ticketType;
            return reservation;
        }

        public ReservationBuilder setActive(boolean active){
            this.active = active;
            return this;
        }
        public ReservationBuilder setExpirationTime(){
            this.expirationTime = LocalDateTime.now().plusMinutes(RESERVATION_EXPIRE_TIME_MIN);
            return this;
        }
        public ReservationBuilder setViewerName(String viewerName){
            this.viewerName = viewerName;
            return this;
        }
        public ReservationBuilder setViewerSurname(String viewerSurname){
            this.viewerSurname = viewerSurname;
            return this;
        }
        public ReservationBuilder setSeatReserved(SeatReserved seatReserved){
            this.seatReserved = seatReserved;
            return this;
        }
        public ReservationBuilder setTicketType(TicketType ticketType){
            this.ticketType = ticketType;
            return this;
        }
    }
}
