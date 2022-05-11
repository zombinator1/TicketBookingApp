package com.bzabek.ticketbookingapp.repository;

import com.bzabek.ticketbookingapp.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReservationSpringDataRepository extends JpaRepository<Reservation, UUID> {

}
