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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Transactional
    public BookingResponse createBooking(BookingRequest request, Long userId) {
        User user = userService.findById(userId);
        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new IllegalArgumentException("Flight not found with id: " + request.getFlightId()));

        if (flight.getAvailableSeats() <= 0) {
            throw new IllegalArgumentException("No available seats for this flight");
        }

        if (flight.getDepartureTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot book past flights");
        }

        int currentBookings = bookingRepository.countBookingsByFlightId(flight.getId());
        if (currentBookings >= flight.getAvailableSeats()) {
            throw new IllegalArgumentException("Flight is fully booked");
        }

        List<Booking> conflictingBookings = bookingRepository.findConflictingBookings(
                user,
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getDepartureTime(),
                flight.getArrivalTime()
        );

        if (!conflictingBookings.isEmpty()) {
            throw new IllegalArgumentException("You have a conflicting booking with this flight schedule");
        }

        Booking booking = new Booking();
        booking.setFlight(flight);
        booking.setUser(user);
        booking.setCustomerName(user.getFullName());
        booking.setBookingDate(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);

        BookingResponse response = modelMapper.map(savedBooking, BookingResponse.class);
        response.setFlight(modelMapper.map(flight, FlightResponse.class));

        return response;
    }

    public BookingResponse getBookingById(Long id, Long userId) {
        Booking booking = bookingRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));

        BookingResponse response = modelMapper.map(booking, BookingResponse.class);
        response.setFlight(modelMapper.map(booking.getFlight(), FlightResponse.class));

        return response;
    }

    public List<BookingResponse> getUserBookings(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        return bookings.stream()
                .map(booking -> {
                    BookingResponse response = modelMapper.map(booking, BookingResponse.class);
                    response.setFlight(modelMapper.map(booking.getFlight(), FlightResponse.class));
                    return response;
                })
                .collect(Collectors.toList());
    }
}