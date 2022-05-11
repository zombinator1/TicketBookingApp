package com.bzabek.ticketbookingapp.repository;

import com.bzabek.ticketbookingapp.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomSpringDataRepository extends JpaRepository<Room,Long> {
    @Override
    Optional<Room> findById(Long aLong);
}
