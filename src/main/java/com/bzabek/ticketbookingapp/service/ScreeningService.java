package com.bzabek.ticketbookingapp.service;

import com.bzabek.ticketbookingapp.entities.Screening;
import com.bzabek.ticketbookingapp.repository.ScreeningSpringDataRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static com.bzabek.ticketbookingapp.Configuration.MINIMUM_TIME_BEFORE_SCREENING_MIN;


@Service
public class ScreeningService {

    private final ScreeningSpringDataRepository screeningRepository;
    private final SeatsService seatsService;

    public ScreeningService(ScreeningSpringDataRepository screeningRepository, SeatsService seatsService) {
        this.screeningRepository = screeningRepository;
        this.seatsService = seatsService;
    }

    public void save(Screening screening){
        screeningRepository.save(screening);
    }
    public List<Screening> findScreenings(String from, String to){
        LocalDateTime fromTime = LocalDateTime.parse(from).plusMinutes(MINIMUM_TIME_BEFORE_SCREENING_MIN);
        LocalDateTime toTime = LocalDateTime.parse(to);
        return screeningRepository.findScreenings(fromTime,toTime);
    }
    @Transactional
    public void addScreening(Screening screening){
        save(screening);
        seatsService.createNewSeatsToReserve(screening);
    }
}
