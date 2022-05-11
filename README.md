
# Ticket booking app
The goal is to build a seat reservation system for a multiplex cinema.


## Business scenario (use case)
1. The user selects the day and the time when he/she would like to see the movie.
2. The system lists movies available in the given time interval - title and screening
times.
3. The user chooses a particular screening.
4. The system gives information regarding screening room and available seats.
5. The user chooses seats, and gives the name of the person doing the reservation
(name and surname).
6. The system gives back the total amount to pay and reservation expiration time.

<p align="center">
  <img 
    width="400"
    src="https://user-images.githubusercontent.com/98849597/167364617-93d41674-4b0f-4eb5-8799-b56c911edd90.png" 
  >
</p>

## Database model:
H2 database was used.
<p align="center">
  <img 
    width="800"
    src="https://user-images.githubusercontent.com/98849597/167352955-c1c1aec8-76c6-4aba-ad90-6615c352e0dc.png"
  >
</p>

**SEAT** table is a table containing list of seats which are inside a room. This table is used only to create seat_reserved record.\
**SEAT_RESERVED** - new list of records is created when admin adds new screening. Every list of seat_reserved is unique for a particular screening.\
**SCREENING** ticket prices may be different for every screening. 

## Endpoints
There are four endpoints.
1. admin/add_screening
2. reservation/screenings
3. reservation/free_seats
4. reservation/reserve_tickets

<p align="center">
  <img 
    width="400"
    src="https://user-images.githubusercontent.com/98849597/167455575-4b0ab9ba-cfd0-4e7f-8799-02f9de0d5525.png"
  >
</p>

**admin/add_screening** is a POST method endpoint. It is used to add new screening record to database. After this operation also a list of "SEAT_RESERVED" objects for a specified screening is being automatically created.\

**reservation/screenings** is a GET method endpoint. This endpoint combinet with two request parameters: "from" and "to" retrieves list of screenings. Parameter "from" and "to" defines in what period of time viewer wants to find screenings. Screenings which starts in 15 min will not be shown, becouse it's too late.\

**reservation/free_seats** also GET with request parameter "screeningId". User sends UUID of a selected screening and receives a list of free seats - SEAT_RESERVED table.\

**reservation/reserve_tickets** POST endpoint. Client sends a request model which contains surnames, names, ticket types, and ids of selected seats (from SEAT_RESERVED table).\

## How to run
To run application first run deploy.sh, then DB_Init.sh. Once you run these scripts you can run useCase.sh.
Application was build with maven 3.8.5 version.

