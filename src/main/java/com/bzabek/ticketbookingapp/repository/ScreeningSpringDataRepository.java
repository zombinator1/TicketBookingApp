package com.bzabek.ticketbookingapp.repository;

import com.bzabek.ticketbookingapp.entities.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScreeningSpringDataRepository extends JpaRepository<Screening, UUID> {

    @Query(nativeQuery = true, value = "SELECT * FROM SCREENING WHERE SCREENING_START_TIME BETWEEN :from AND :to ORDER BY MOVIE_NAME, SCREENING_START_TIME ")
    List<Screening> findScreenings(LocalDateTime from, LocalDateTime to);


}
