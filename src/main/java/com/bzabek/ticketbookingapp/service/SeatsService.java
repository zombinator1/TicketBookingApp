package com.bzabek.ticketbookingapp.service;

import com.bzabek.ticketbookingapp.entities.Screening;
import com.bzabek.ticketbookingapp.entities.Seat;
import com.bzabek.ticketbookingapp.entities.SeatReserved;
import com.bzabek.ticketbookingapp.repository.SeatReservedSpringDataRepository;
import com.bzabek.ticketbookingapp.repository.SeatSpringDataRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SeatsService {

    private final SeatReservedSpringDataRepository seatReservedRepository;
    private final SeatSpringDataRepository seatRepository;


    public SeatsService(SeatReservedSpringDataRepository seatReservedRepository, SeatSpringDataRepository seatRepository) {
        this.seatReservedRepository = seatReservedRepository;
        this.seatRepository = seatRepository;
    }

    public List<SeatReserved> freeSeats(UUID screeningId) {
        return seatReservedRepository.findFreeSeats(screeningId);
    }

    public void createNewSeatsToReserve(Screening screening) {
        List<SeatReserved> seatReservedList = new ArrayList<>();
        List<Seat> seatsInRoom = seatRepository.findAllByRoomId(screening.getRoom().getId());
        for (Seat seat : seatsInRoom) {
            seatReservedList.add(new SeatReserved(screening, seat));
        }
        seatReservedRepository.saveAll(seatReservedList);
    }

    public List<SeatReserved> findAllById(List<UUID> uuids) {
        return seatReservedRepository.findAllById(uuids);
    }

    public List<SeatReserved> findSeatsInTheSameRows(List<SeatReserved> newlyReservedSeats) {
        List<Long> rows = newlyReservedSeats.stream().map(SeatReserved::getRowNr).distinct().collect(Collectors.toList());
        Screening screening = newlyReservedSeats.get(0).getScreening();
        return seatReservedRepository.findSeatsInTheSameRows(screening.getId(), rows);
    }

    public void saveAll(List<SeatReserved> seatReservedList) {
        seatReservedRepository.saveAllAndFlush(seatReservedList);
    }

}
