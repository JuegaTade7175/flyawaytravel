package com.example.flyawaytravel.service;

import com.example.flyawaytravel.dto.request.BookingRequest;
import com.example.flyawaytravel.dto.response.BookingResponse;
import com.example.flyawaytravel.dto.response.FlightResponse;
import com.example.flyawaytravel.entity.Booking;
import com.example.flyawaytravel.entity.Flight;
import com.example.flyawaytravel.entity.User;
import com.example.flyawaytravel.repository.BookingRepository;
import com.example.flyawaytravel.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final UserService userService;
    private final EmailConfirmationService emailConfirmationService;

    public BookingResponse createBooking(BookingRequest request, Long userId) {
        User user = userService.findById(userId);
        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new IllegalArgumentException("Vuelo no encontrado con el id: " + request.getFlightId()));

        LocalDateTime now = LocalDateTime.now();
        if (!flight.getDepartureTime().isAfter(now)) {
            throw new IllegalArgumentException("No se pueden reservar vuelos pasados o en tránsito");
        }
        if (flight.getAvailableSeats() <= 0) {
            throw new IllegalArgumentException("No hay asientos disponibles para este vuelo");
        }

        int currentBookings = bookingRepository.countBookingsByFlightId(flight.getId());
        if (currentBookings >= flight.getAvailableSeats()) {
            throw new IllegalArgumentException("El vuelo está completamente reservado");
        }

        List<Booking> conflictingBookings = bookingRepository.findConflictingBookings(
                user,
                flight.getDepartureTime(), flight.getArrivalTime(),
                flight.getDepartureTime(), flight.getArrivalTime()
        );
        if (!conflictingBookings.isEmpty()) {
            throw new IllegalArgumentException("Tienes una reserva conflictiva con este horario de vuelo");
        }

        Booking booking = new Booking();
        booking.setFlight(flight);
        booking.setUser(user);
        booking.setCustomerName(user.getFullName());
        booking.setBookingDate(now);

        Booking savedBooking = bookingRepository.save(booking);
        emailConfirmationService.saveConfirmationEmail(savedBooking);
        return toResponse(savedBooking);
    }

    public BookingResponse getBookingById(Long id, Long userId) {
        Booking booking = bookingRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con el id: " + id));
        return toResponse(booking);
    }

    public List<BookingResponse> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private BookingResponse toResponse(Booking booking) {
        Flight flight = booking.getFlight();
        FlightResponse flightResponse = new FlightResponse(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getAirline(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getAvailableSeats(),
                flight.getOrigin(),
                flight.getDestination(),
                flight.getCreatedAt()
        );
        return new BookingResponse(
                booking.getId(),
                flightResponse,
                booking.getCustomerName(),
                booking.getBookingDate()
        );
    }
}