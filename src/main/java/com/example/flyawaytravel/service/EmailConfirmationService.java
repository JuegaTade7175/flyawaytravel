package com.example.flyawaytravel.service;

import com.example.flyawaytravel.entity.Booking;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class EmailConfirmationService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public void saveConfirmationEmail(Booking booking) {
        String filename = "flight_booking_email_" + booking.getId() + ".txt";

        String customerFirstName = booking.getUser().getFirstName();
        String customerLastName  = booking.getUser().getLastName();
        String flightNumber      = booking.getFlight().getFlightNumber();
        String estDeparture      = booking.getFlight().getEstDepartureTime().format(FORMATTER);
        String estArrival        = booking.getFlight().getEstArrivalTime().format(FORMATTER);
        String bookingDate       = booking.getBookingDate().format(FORMATTER);

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
        } catch (IOException e) {
            log.error("Error saving confirmation email for booking {}: {}", booking.getId(), e.getMessage());
        }
    }
}