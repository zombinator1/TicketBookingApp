echo "launching use case scenario. (BEFORE YOU LAUNCH SCENARIO FOR THE FIRST TIME, PLEASE RUN run DB_Init.sh TO INITIALIZE DB)"

echo "1. To show list of screenings in a stated period of time, client sends request like this:"
echo "http://localhost:8080/reservation/screenings?from=2022-08-21T00:34:16.144930009&to=2022-08-21T22:34:16.144961548"
echo "Press any key to continue..."
read -s -n 1 key

curl "http://localhost:8080/reservation/screenings?from=2022-08-21T00:34:16.144930009&to=2022-08-21T22:34:16.144961548" -s | jq

echo "2. Client sends id of a selected screening and receives a list of free seats"
echo "Give me an id of a selected screening, in format like in this example:  3b92a0bc-ca2b-4eb9-b7f9-66821a35b571"

read screeningId
curl http://localhost:8080/reservation/free_seats?screeningId=$screeningId -s | jq

echo "3. User wants to reserve 2 seats, so following model is send to API:"
cat << requestModel
[
{
"seatReservedId": "${1st seat Id}",
"name": "Łukasz",
  "surname": "Kowalski",
"ticketType": "ADULT"
},
{
"seatReservedId": ""${2nd seat Id}"",
"name": "Ździsława",
  "surname": "Kowalska Nowak",
"ticketType": "CHILD"
}
]
requestModel
echo "Please copy and paste Id (UUID) of a first seat you want to reserve"
read firstSeatId
echo "Please copy and paste Id (UUID) of a second seat"
read secondSeatId

curl http://localhost:8080/reservation/reserve_tickets -H "content-type: application/json" -X POST -s -d '[
{"seatReservedId":"'"$firstSeatId"'","name": "Łukasz","surname": "Kowalski","ticketType": "ADULT"},
{"seatReservedId":"'"$secondSeatId"'","name": "Ździsława","surname":"Kowalska Nowak","ticketType":"CHILD"}
]' | jq

