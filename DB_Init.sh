#!/bin/bash

echo "DB is being initialized"

curl http://localhost:8080/admin/add_screening -H "content-type: application/json" -X POST -s -d '{
          "movieName": "Terminator",
          "screeningStartTime": "2022-08-21T17:00",
          "screeningEndTime": "2022-08-21T19:30",
          "adultTicketPrice": 25.00,
          "studentTicketPrice": 18.00,
          "childTicketPrice": 12.50,
          "room":{
          "id":"2"
          }
  }' | jq

curl http://localhost:8080/admin/add_screening -H "content-type: application/json" -X POST -s -d '{
          "movieName": "Terminator",
          "screeningStartTime": "2022-08-22T17:00",
          "screeningEndTime": "2022-08-22T19:30",
          "adultTicketPrice": 25.00,
          "studentTicketPrice": 18.00,
          "childTicketPrice": 12.50,
          "room":{
          "id":"2"
          }
  }' | jq

curl http://localhost:8080/admin/add_screening   -H "content-type: application/json" -X POST -s -d '{
          "movieName": "Rambo",
          "screeningStartTime": "2022-08-21T14:00",
          "screeningEndTime": "2022-08-21T17:00",
          "adultTicketPrice": 25.00,
          "studentTicketPrice": 18.00,
          "childTicketPrice": 12.50,
          "room":{
          "id":"1"
          }
  }' | jq

  curl http://localhost:8080/admin/add_screening   -H "content-type: application/json" -X POST -s -d '{
            "movieName": "Rambo",
            "screeningStartTime": "2022-08-22T14:00",
            "screeningEndTime": "2022-08-22T17:00",
            "adultTicketPrice": 25.00,
            "studentTicketPrice": 18.00,
            "childTicketPrice": 12.50,
            "room":{
            "id":"1"
            }
    }' | jq

  curl http://localhost:8080/admin/add_screening -H "content-type: application/json" -X POST -s -d '{
            "movieName": "LoTR",
            "screeningStartTime": "2022-08-21T10:00",
            "screeningEndTime": "2022-08-21T14:00",
            "adultTicketPrice": 25.00,
            "studentTicketPrice": 18.00,
            "childTicketPrice": 12.50,
            "room":{
            "id":"3"
            }
    }' | jq

      curl http://localhost:8080/admin/add_screening -H "content-type: application/json" -X POST -s -d '{
                "movieName": "LoTR",
                "screeningStartTime": "2022-08-22T10:00",
                "screeningEndTime": "2022-08-22T14:00",
                "adultTicketPrice": 25.00,
                "studentTicketPrice": 18.00,
                "childTicketPrice": 12.50,
                "room":{
                "id":"3"
                }
        }' | jq

echo "DB initialized. 6 screenings added."