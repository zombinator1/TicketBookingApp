package com.bzabek.ticketbookingapp.repository;

import com.bzabek.ticketbookingapp.entities.SeatReserved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SeatReservedSpringDataRepository extends JpaRepository<SeatReserved, UUID> {
    @Query(nativeQuery = true, value = "SELECT * FROM SEAT_RESERVED where RESERVED = false and SCREENING_ID = :screeningId")
    List<SeatReserved> findFreeSeats(UUID screeningId);

    @Query(nativeQuery = true, value = "SELECT * FROM SEAT_RESERVED WHERE SCREENING_ID = :screeningId AND ROW_NR = :rows AND RESERVED = TRUE")
    List<SeatReserved> findSeatsInTheSameRows(UUID screeningId, List<Long> rows);





}
