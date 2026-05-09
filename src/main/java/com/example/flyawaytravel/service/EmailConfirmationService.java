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
            log.info("Correo de confirmación guardado en archivo: {}", filename);
        } catch (IOException e) {
            log.error("Error al guardar correo de confirmación para reserva {}: {}", booking.getId(), e.getMessage());
        }
    }

    private String buildEmailContent(Booking booking) {
        return "========================================\n" +
               "       CONFIRMACIÓN DE RESERVA DE VUELO      \n" +
               "========================================\n\n" +
               "ID de Reserva: " + booking.getId() + "\n" +
               "Nombre del Cliente: " + booking.getCustomerName() + "\n" +
               "Fecha de Reserva: " + booking.getBookingDate().format(ISO_FORMATTER) + "\n\n" +
               "--- Detalles del Vuelo ---\n" +
               "Número de Vuelo: " + booking.getFlight().getFlightNumber() + "\n" +
               "Aerolínea: " + booking.getFlight().getAirline() + "\n" +
               "Origen: " + booking.getFlight().getOrigin() + "\n" +
               "Destino: " + booking.getFlight().getDestination() + "\n" +
               "Hora de Salida: " + booking.getFlight().getDepartureTime().format(ISO_FORMATTER) + "\n" +
               "Hora de Llegada: " + booking.getFlight().getArrivalTime().format(ISO_FORMATTER) + "\n\n" +
               "========================================\n" +
               "¡Gracias por elegir Fly Away Travel!\n" +
               "========================================\n";
    }
}