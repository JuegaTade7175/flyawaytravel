package com.example.flyawaytravel.service;

import com.example.flyawaytravel.entity.Booking;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailConfirmationService {

    private final ObjectMapper objectMapper;

    public void saveConfirmationEmail(Booking booking) {
        String filename = "flight_booking_email_" + booking.getId() + ".txt";

        try {
            String bookingDate = booking.getBookingDate()
                .truncatedTo(java.time.temporal.ChronoUnit.MICROS)
                .toInstant().toString();
            String estDeparture = booking.getFlight().getEstDepartureTime().toInstant().toString();
            String estArrival   = booking.getFlight().getEstArrivalTime().toInstant().toString();

            String customerFirstName = booking.getUser().getFirstName();
            String customerLastName  = booking.getUser().getLastName();
            String flightNumber      = booking.getFlight().getFlightNumber();

            String content = String.format(
                "Hello %s %s,\n\n" +
                "Your booking was successful! \n\n" +
                "The booking is for flight %s with departure date of %s and arrival date of %s.\n\n" +
                "The booking was registered at %s.\n\n" +
                "Bon Voyage!\n" +
                "Fly Away Travel\n",
                customerFirstName, customerLastName,
                flightNumber,
                estDeparture,
                estArrival,
                bookingDate
            );

            try (FileWriter writer = new FileWriter(filename)) {
                writer.write(content);
                log.info("Confirmation email saved: {}", filename);
            }

        } catch (IOException e) {
            log.error("Error saving confirmation email for booking {}: {}", booking.getId(), e.getMessage());
        }
    }
}