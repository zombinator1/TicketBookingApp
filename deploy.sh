#!/bin/bash

echo "project build with maven 3.8.5"
mvn clean package

cd target

java -jar TicketBookingApp-0.0.1-SNAPSHOT.jar






