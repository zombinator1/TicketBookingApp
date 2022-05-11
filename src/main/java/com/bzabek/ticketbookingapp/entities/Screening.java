package com.bzabek.ticketbookingapp.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Screening {

    @Id
    @GeneratedValue
    private UUID id;

    private String movieName;
    @ManyToOne
    private Room room;

    private LocalDateTime screeningStartTime;

    private LocalDateTime screeningEndTime;

    private BigDecimal adultTicketPrice;

    private BigDecimal studentTicketPrice;

    private BigDecimal childTicketPrice;

    @OneToMany(mappedBy = "screening")
    @JsonIgnore
    private List<SeatReserved> seatReservedList = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDateTime getScreeningStartTime() {
        return screeningStartTime;
    }

    public void setScreeningStartTime(LocalDateTime screeningStartTime) {
        this.screeningStartTime = screeningStartTime;
    }

    public LocalDateTime getScreeningEndTime() {
        return screeningEndTime;
    }

    public void setScreeningEndTime(LocalDateTime screeningEndTime) {
        this.screeningEndTime = screeningEndTime;
    }

    public BigDecimal getAdultTicketPrice() {
        return adultTicketPrice;
    }

    public void setAdultTicketPrice(BigDecimal adultTicketPrice) {
        this.adultTicketPrice = adultTicketPrice;
    }

    public BigDecimal getStudentTicketPrice() {
        return studentTicketPrice;
    }

    public void setStudentTicketPrice(BigDecimal studentTicketPrice) {
        this.studentTicketPrice = studentTicketPrice;
    }

    public BigDecimal getChildTicketPrice() {
        return childTicketPrice;
    }

    public void setChildTicketPrice(BigDecimal childTicketPrice) {
        this.childTicketPrice = childTicketPrice;
    }


    public List<SeatReserved> getSeatReservedList() {
        return seatReservedList;
    }

    public void setSeatReservedList(List<SeatReserved> seatReservedList) {
        this.seatReservedList = seatReservedList;
    }

}
