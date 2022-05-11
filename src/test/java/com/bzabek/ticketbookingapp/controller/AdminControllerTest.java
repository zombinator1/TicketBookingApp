package com.bzabek.ticketbookingapp.controller;

import com.bzabek.ticketbookingapp.TicketBookingAppApplication;
import com.bzabek.ticketbookingapp.entities.Room;
import com.bzabek.ticketbookingapp.entities.Screening;
import com.bzabek.ticketbookingapp.repository.RoomSpringDataRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketBookingAppApplication.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminControllerTest {

    @LocalServerPort
    int port;
    @Autowired
    private RoomSpringDataRepository roomRepository;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void addScreeningTest(){

        String baseUrl = "http://localhost:" + port;
        String path = "/admin/add_screening";
        String url = baseUrl + path;

        Room room = roomRepository.findById(1L).get();

        Screening requestBody = new Screening();
            requestBody.setAdultTicketPrice(new BigDecimal(25));
            requestBody.setStudentTicketPrice(new BigDecimal(18));
            requestBody.setChildTicketPrice(new BigDecimal(12.50));
            requestBody.setRoom(room);
            requestBody.setMovieName("Rambo");
            requestBody.setScreeningStartTime(LocalDateTime.now().plusDays(1));
            requestBody.setScreeningEndTime(LocalDateTime.now().plusDays(1).plusHours(3));

         ResponseEntity<HttpStatus> response = restTemplate.postForEntity(url, requestBody, HttpStatus.class);
        assertEquals(response.getStatusCode(),HttpStatus.CREATED);
    }

}
