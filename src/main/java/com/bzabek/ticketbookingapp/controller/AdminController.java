package com.bzabek.ticketbookingapp.controller;

import com.bzabek.ticketbookingapp.entities.Screening;
import com.bzabek.ticketbookingapp.service.ScreeningService;
import com.bzabek.ticketbookingapp.service.SeatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
public class AdminController {

    private final SeatsService seatsService;
    private final ScreeningService screeningService;


    public AdminController(SeatsService seatsService, ScreeningService screeningService) {
        this.seatsService = seatsService;
        this.screeningService = screeningService;

    }

    @PostMapping("/add_screening")
    public ResponseEntity<HttpStatus> addScreening(@RequestBody Screening screening){
        screeningService.addScreening(screening);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
