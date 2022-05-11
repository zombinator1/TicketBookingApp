package com.bzabek.ticketbookingapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Room {

    @Id
    private Long id;
    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<Screening> screenings = new ArrayList<>();
    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<Seat> seats = new ArrayList<>();

    public Room() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Screening> getScreenings() {
        return screenings;
    }

    public void setScreenings(List<Screening> screenings) {
        this.screenings = screenings;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}
