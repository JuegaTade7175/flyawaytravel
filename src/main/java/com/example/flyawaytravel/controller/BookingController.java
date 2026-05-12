package com.example.flyawaytravel.controller;

import com.example.flyawaytravel.dto.request.BookingRequest;
import com.example.flyawaytravel.dto.response.BookingResponse;
import com.example.flyawaytravel.dto.response.NewIdResponse;
import com.example.flyawaytravel.entity.User;
import com.example.flyawaytravel.repository.UserRepository;
import com.example.flyawaytravel.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserRepository userRepository;

    @PostMapping("/flights/book")
    public ResponseEntity<NewIdResponse> bookFlight(@Valid @RequestBody BookingRequest request) {
        Long userId = getAuthenticatedUserId();
        BookingResponse response = bookingService.createBooking(request, userId);
        return ResponseEntity.ok(new NewIdResponse(response.getId()));
    }

    @GetMapping("/flights/book/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
        BookingResponse response = bookingService.getBookingById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingResponse>> getUserBookings() {
        Long userId = getAuthenticatedUserId();
        return ResponseEntity.ok(bookingService.getUserBookings(userId));
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    private Long getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getId();
    }
}