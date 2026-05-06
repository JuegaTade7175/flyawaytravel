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

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public void saveConfirmationEmail(Booking booking) {
        String filename = "flight_booking_email_" + booking.getId() + ".txt";

        String content = buildEmailContent(booking);

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(content);
            log.info("Confirmation email saved to file: {}", filename);
        } catch (IOException e) {
            log.error("Failed to save confirmation email for booking {}: {}", booking.getId(), e.getMessage());
        }
    }

    private String buildEmailContent(Booking booking) {
        return "========================================\n" +
               "       FLIGHT BOOKING CONFIRMATION      \n" +
               "========================================\n\n" +
               "Booking ID: " + booking.getId() + "\n" +
               "Customer Name: " + booking.getCustomerName() + "\n" +
               "Booking Date: " + booking.getBookingDate().format(ISO_FORMATTER) + "\n\n" +
               "--- Flight Details ---\n" +
               "Flight Number: " + booking.getFlight().getFlightNumber() + "\n" +
               "Airline: " + booking.getFlight().getAirline() + "\n" +
               "Origin: " + booking.getFlight().getOrigin() + "\n" +
               "Destination: " + booking.getFlight().getDestination() + "\n" +
               "Departure Time: " + booking.getFlight().getDepartureTime().format(ISO_FORMATTER) + "\n" +
               "Arrival Time: " + booking.getFlight().getArrivalTime().format(ISO_FORMATTER) + "\n\n" +
               "========================================\n" +
               "Thank you for choosing Fly Away Travel!\n" +
               "========================================\n";
    }
}