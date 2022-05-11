package com.bzabek.ticketbookingapp.repository;


import com.bzabek.ticketbookingapp.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface SeatSpringDataRepository extends JpaRepository<Seat, UUID> {
    List<Seat> findAllByRoomId(Long roomId);




}
