package com.bzabek.ticketbookingapp;

import com.bzabek.ticketbookingapp.entities.Room;
import com.bzabek.ticketbookingapp.entities.Screening;
import com.bzabek.ticketbookingapp.entities.SeatReserved;
import com.bzabek.ticketbookingapp.model.MakeReservation;
import com.bzabek.ticketbookingapp.model.ReservationCreated;
import com.bzabek.ticketbookingapp.model.TicketType;
import com.bzabek.ticketbookingapp.repository.ReservationSpringDataRepository;
import com.bzabek.ticketbookingapp.repository.RoomSpringDataRepository;
import com.bzabek.ticketbookingapp.repository.ScreeningSpringDataRepository;
import com.bzabek.ticketbookingapp.repository.SeatReservedSpringDataRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.bzabek.ticketbookingapp.Configuration.MINIMUM_TIME_BEFORE_SCREENING_MIN;
import static com.bzabek.ticketbookingapp.Configuration.RESERVATION_EXPIRE_TIME_MIN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketBookingAppApplication.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class E2ETest {

    @LocalServerPort
    int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private RoomSpringDataRepository roomRepository;
    @Autowired
    private SeatReservedSpringDataRepository seatReservedRepository;
    @Autowired
    private ScreeningSpringDataRepository screeningRepository;
    @Autowired
    private ReservationSpringDataRepository reservationRepository;

    String getScreeningsPath = "/reservation/screenings";
    String getFreeSeatsPath = "/reservation/free_seats";
    String reserveTicketsPath = "/reservation/reserve_tickets";

    @Before
    public void dbInit(){
        String baseUrl = "http://localhost:" + port;
        String path = "/admin/add_screening";
        String url = baseUrl + path;

        Room room = roomRepository.findById(1L).get();

        Screening newRamboScreening = new Screening();
            newRamboScreening.setAdultTicketPrice(new BigDecimal(25));
            newRamboScreening.setStudentTicketPrice(new BigDecimal(18));
            newRamboScreening.setChildTicketPrice(new BigDecimal(12.50));
            newRamboScreening.setRoom(room);
            newRamboScreening.setMovieName("Rambo");
            newRamboScreening.setScreeningStartTime(LocalDateTime.now().plusDays(199).plusHours(1));
            newRamboScreening.setScreeningEndTime(LocalDateTime.now().plusDays(199).plusHours(3));

        Screening newLoTRScreening = new Screening();
            newLoTRScreening.setAdultTicketPrice(new BigDecimal(25));
            newLoTRScreening.setStudentTicketPrice(new BigDecimal(18));
            newLoTRScreening.setChildTicketPrice(new BigDecimal(12.50));
            newLoTRScreening.setRoom(room);
            newLoTRScreening.setMovieName("LoTR");
            newLoTRScreening.setScreeningStartTime(LocalDateTime.now().plusDays(199).plusHours(3));
            newLoTRScreening.setScreeningEndTime(LocalDateTime.now().plusDays(199).plusHours(6));

      restTemplate.postForObject(url, newRamboScreening, Screening.class);
      restTemplate.postForObject(url, newLoTRScreening, Screening.class);
    }

    @Test
    public void useCaseScenarioTest(){
        System.out.println("test1");
        String baseUrl = "http://localhost:" + port;

        //USER SENDS REQUEST TO FIND SCREENINGS IN A GIVEN PERIOD OF TIME:
         LocalDateTime from = LocalDateTime.now().plusDays(199);
         LocalDateTime to = LocalDateTime.now().plusDays(199).plusHours(4);
         String getScreeningsUrl =String.format("%s%s?from=%s&to=%s",baseUrl,getScreeningsPath,from,to);

        Screening[] screeningResponse = restTemplate.getForObject(getScreeningsUrl,Screening[].class);
        List<Screening> screenings = Arrays.stream(screeningResponse).collect(Collectors.toList());

        assertEquals(2,screenings.size());
        //USER POSTS CHOSEN SCREENING. API INFORMS USER ABOUT FREE SEATS:
        Screening chosenScreening = screenings.get(0);
        String byScreeningId = chosenScreening.getId().toString();
        String freeSeatsUrl = String.format("%s%s?screeningId=%s",baseUrl,getFreeSeatsPath,byScreeningId);
        SeatReserved[] freeSeats = restTemplate.getForObject(freeSeatsUrl, SeatReserved[].class);
        List<SeatReserved> freeSeatsList = Arrays.stream(freeSeats).collect(Collectors.toList());

        assertTrue(freeSeatsList.size() > 0);
        assertTrue(freeSeatsList.stream().filter(SeatReserved::isReserved).collect(Collectors.toList()).isEmpty());

        //USER HAS CHOSEN SEATS AND NOW POSTS RESERVATION DATA
        MakeReservation reservationRequest = new MakeReservation(
                freeSeatsList.get(0).getId(),
                "Ździsław",
                "Brzęczkowski Woźniak",
                TicketType.ADULT
        );
        MakeReservation reservationRequest2 = new MakeReservation(
                freeSeatsList.get(1).getId(),
                "Łucja",
                "Brzęczkowska Woźniak",
                TicketType.CHILD
        );
        List<MakeReservation> reservationBody = List.of(reservationRequest,reservationRequest2);
        String reservationUrl = String.format("%s%s",baseUrl,reserveTicketsPath);

        ReservationCreated reservationResponse = restTemplate.postForObject(reservationUrl,reservationBody, ReservationCreated.class);
        System.out.println(reservationResponse.toString());

        assertEquals(reservationResponse.getTotalPrice(), screenings.get(0).getAdultTicketPrice().add(screenings.get(0).getChildTicketPrice()));
        assertTrue(reservationResponse.getExpirationTime().isAfter(LocalDateTime.now().plusMinutes(RESERVATION_EXPIRE_TIME_MIN - 1)) &&
                            reservationResponse.getExpirationTime().isBefore(LocalDateTime.now().plusMinutes(RESERVATION_EXPIRE_TIME_MIN + 1)));
    }

    @Test
    public void dontShowScreeningThatStartsIn15min(){
        String baseUrl = "http://localhost:" + port;

        //USER SENDS REQUEST TO FIND SCREENINGS IN A GIVEN PERIOD OF TIME:
        LocalDateTime from = LocalDateTime.now().plusDays(199).plusHours(1).minusMinutes(MINIMUM_TIME_BEFORE_SCREENING_MIN).plusMinutes(1);
        LocalDateTime to = LocalDateTime.now().plusDays(199).plusHours(4);
        String getScreeningsUrl =String.format("%s%s?from=%s&to=%s",baseUrl,getScreeningsPath,from,to);

        Screening[] screeningResponse = restTemplate.getForObject(getScreeningsUrl,Screening[].class);
        List<Screening> screenings = Arrays.stream(screeningResponse).collect(Collectors.toList());
        assertTrue(screenings.size() == 1);
    }

    @After
    public void clearDB() {
        reservationRepository.deleteAll();
        seatReservedRepository.deleteAll();
        screeningRepository.deleteAll();
    }
}
