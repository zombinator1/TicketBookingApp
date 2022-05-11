package com.bzabek.ticketbookingapp;

@org.springframework.context.annotation.Configuration
public class Configuration {

        public static int MINIMUM_TIME_BEFORE_SCREENING_MIN = 15;

        public static int RESERVATION_EXPIRE_TIME_MIN = 20;

        public static String LATE_RESERVATION_MESSAGE = "We are sorry. It's to late to reserve tickets";

}
