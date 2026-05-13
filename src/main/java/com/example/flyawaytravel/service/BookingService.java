package com.example.flyawaytravel.service;

import com.example.flyawaytravel.dto.request.BookingRequest;
import com.example.flyawaytravel.dto.response.BookingResponse;
import com.example.flyawaytravel.entity.Booking;
import com.example.flyawaytravel.entity.Flight;
import com.example.flyawaytravel.entity.User;
import com.example.flyawaytravel.repository.BookingRepository;
import com.example.flyawaytravel.repository.FlightRepository;
import com.example.flyawaytravel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    private final EmailConfirmationService emailConfirmationService;

    public BookingResponse createBooking(BookingRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new IllegalArgumentException("Flight not found: " + request.getFlightId()));

        OffsetDateTime now = OffsetDateTime.now()
            .truncatedTo(java.time.temporal.ChronoUnit.MICROS);
        if (!flight.getEstDepartureTime().isAfter(now)) {
            throw new IllegalArgumentException("Cannot book a past or in-transit flight");
        }

        int currentBookings = bookingRepository.countByFlightId(flight.getId());
        if (currentBookings >= flight.getAvailableSeats()) {
            throw new IllegalArgumentException("Flight is fully booked");
        }

        List<Booking> overlapping = bookingRepository.findOverlapping(
                userId,
                flight.getId(),
                flight.getEstDepartureTime(),
                flight.getEstArrivalTime()
        );
        if (!overlapping.isEmpty()) {
            throw new IllegalArgumentException("Booking overlaps with an existing booking");
        }

        Booking booking = new Booking();
        booking.setFlight(flight);
        booking.setUser(user);
        booking.setBookingDate(now);

        Booking saved = bookingRepository.save(booking);

        emailConfirmationService.saveConfirmationEmail(saved);

        return toResponse(saved);
    }

    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + id));
        return toResponse(booking);
    }

    public List<BookingResponse> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    private BookingResponse toResponse(Booking booking) {
        Flight f = booking.getFlight();
        User u = booking.getUser();
        BookingResponse r = new BookingResponse();
        r.setId(booking.getId());
        r.setBookingDate(booking.getBookingDate());
        r.setFlightId(f.getId());
        r.setFlightNumber(f.getFlightNumber());
        r.setCustomerId(u.getId());
        r.setCustomerFirstName(u.getFirstName());
        r.setCustomerLastName(u.getLastName());
        r.setEstDepartureTime(f.getEstDepartureTime());
        r.setEstArrivalTime(f.getEstArrivalTime());
        return r;
    }
}