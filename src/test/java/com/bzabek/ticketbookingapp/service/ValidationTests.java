package com.bzabek.ticketbookingapp.service;

import com.bzabek.ticketbookingapp.entities.SeatReserved;
import com.bzabek.ticketbookingapp.exceptions.SeatIsAlreadyReservedException;
import com.bzabek.ticketbookingapp.exceptions.SinglePlaceLeftOverException;
import com.bzabek.ticketbookingapp.exceptions.NameSurnameException;
import com.bzabek.ticketbookingapp.repository.ReservationSpringDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ValidationTests {


    @Autowired
    ReservationSpringDataRepository reservationRepository;
    @Autowired
    ScreeningService screeningService;

    @Test
    public void nameAndSurnameValidationTest() {
        assertThrows(NameSurnameException.class, () -> NameAndSurnameValidator.validate("Jano Nowak Kowal"));
        assertThrows(NameSurnameException.class, () -> NameAndSurnameValidator.validate("Ja No"));
        assertThrows(NameSurnameException.class, () -> NameAndSurnameValidator.validate("Jan No"));
        assertThrows(NameSurnameException.class, () -> NameAndSurnameValidator.validate("Ja Nom"));
        assertThrows(NameSurnameException.class, () -> NameAndSurnameValidator.validate("Zi"));
        assertThrows(NameSurnameException.class, () -> NameAndSurnameValidator.validate("6Kowalski"));
        assertThrows(NameSurnameException.class, () -> NameAndSurnameValidator.validate("Kowalski123"));
        assertThrows(NameSurnameException.class, () -> NameAndSurnameValidator.validate("kowalski!"));
        assertThrows(NameSurnameException.class, () -> NameAndSurnameValidator.validate("(kowalski!)"));
        assertThrows(NameSurnameException.class, () -> NameAndSurnameValidator.validate("Kowal/Nowak"));
        assertThrows(NameSurnameException.class, () -> NameAndSurnameValidator.validate("kowal Nowak"));
        assertThrows(NameSurnameException.class, () -> NameAndSurnameValidator.validate("kowal kowak"));
        assertThrows(NameSurnameException.class, () -> NameAndSurnameValidator.validate("Jowal jowak"));
    }

    @Test
    public void singlePlaceBetweenRows() {
        //GIVEN
        List<SeatReserved> seatAlreadyReservedTest = new ArrayList<>();
        seatAlreadyReservedTest.add(new SeatReserved(true, 1L, 1L));

        List<SeatReserved> test1 = new ArrayList<>();
        test1.add(new SeatReserved(false, 1L, 3L));

        List<SeatReserved> test2 = new ArrayList<>();
        test2.add(new SeatReserved(false, 1L, 11L));
        test2.add(new SeatReserved(false, 1L, 12L));

        List<SeatReserved> twoRowsTest = new ArrayList<>();
        twoRowsTest.add(new SeatReserved(false, 1L, 3L));
        twoRowsTest.add(new SeatReserved(false, 1L, 4L));
        twoRowsTest.add(new SeatReserved(false, 2L, 7L));
        //return
        List<SeatReserved> seatsInRowNr1 = new ArrayList<>();
        seatsInRowNr1.add(new SeatReserved(true, 1L, 1L));
        seatsInRowNr1.add(new SeatReserved(true, 1L, 2L));
        seatsInRowNr1.add(new SeatReserved(true, 1L, 5L));
        seatsInRowNr1.add(new SeatReserved(true, 1L, 6L));
        seatsInRowNr1.add(new SeatReserved(true, 1L, 9L));

        List<SeatReserved> seatsInRowNr2 = new ArrayList<>();
        seatsInRowNr1.add(new SeatReserved(true, 2L, 3L));
        seatsInRowNr1.add(new SeatReserved(true, 2L, 4L));
        seatsInRowNr1.add(new SeatReserved(true, 2L, 5L));

        List<SeatReserved> twoRows = new ArrayList<>();
        twoRows.addAll(seatsInRowNr1);
        twoRows.addAll(seatsInRowNr2);

        SeatsService seatsServiceMock = mock(SeatsService.class);
        ReservationService reservationServiceMock = new ReservationService(reservationRepository, screeningService, seatsServiceMock);

        //WHEN
        when(seatsServiceMock.findSeatsInTheSameRows(seatAlreadyReservedTest)).thenReturn(seatsInRowNr1);
        when(seatsServiceMock.findSeatsInTheSameRows(test1)).thenReturn(seatsInRowNr1);
        when(seatsServiceMock.findSeatsInTheSameRows(test2)).thenReturn(seatsInRowNr1);
        when(seatsServiceMock.findSeatsInTheSameRows(twoRowsTest)).thenReturn(twoRows);

        //THEN
        assertThrows(SeatIsAlreadyReservedException.class, () -> reservationServiceMock.seatsValidation(seatAlreadyReservedTest));
        assertThrows(SinglePlaceLeftOverException.class, () -> reservationServiceMock.seatsValidation(test1));
        assertThrows(SinglePlaceLeftOverException.class, () -> reservationServiceMock.seatsValidation(test2));
        assertThrows(SinglePlaceLeftOverException.class, () -> reservationServiceMock.seatsValidation(twoRowsTest));

        when(seatsServiceMock.findSeatsInTheSameRows(anyList())).thenReturn(twoRows);
        assertDoesNotThrow(() -> reservationServiceMock.seatsValidation(List.of(
                new SeatReserved(false, 1L, 3L),
                new SeatReserved(false, 1L, 4L))));
        assertDoesNotThrow(() -> reservationServiceMock.seatsValidation(List.of(
                new SeatReserved(false, 2L, 2L),
                new SeatReserved(false, 2L, 6L))));
    }
}