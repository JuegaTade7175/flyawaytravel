package com.example.flyawaytravel.controller;

import com.example.flyawaytravel.repository.BookingRepository;
import com.example.flyawaytravel.repository.FlightRepository;
import com.example.flyawaytravel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CleanupController {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;

    @DeleteMapping("/cleanup")
    @Transactional
    public ResponseEntity<Void> cleanup() {
        bookingRepository.deleteAll();
        flightRepository.deleteAll();
        userRepository.deleteAll();
        return ResponseEntity.ok().build();
    }
}