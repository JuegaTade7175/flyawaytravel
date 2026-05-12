package com.example.flyawaytravel.service;

import com.example.flyawaytravel.dto.request.FlightCreateRequest;
import com.example.flyawaytravel.dto.response.FlightResponse;
import com.example.flyawaytravel.dto.response.FlightSearchResponse;
import com.example.flyawaytravel.entity.Flight;
import com.example.flyawaytravel.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    @Transactional
    public FlightResponse createFlight(FlightCreateRequest request) {
        if (!request.getEstDepartureTime().isBefore(request.getEstArrivalTime())) {
            throw new IllegalArgumentException("estDepartureTime must be before estArrivalTime");
        }

        if (flightRepository.existsByFlightNumber(request.getFlightNumber())) {
            throw new IllegalArgumentException("Flight number already exists: " + request.getFlightNumber());
        }

        Flight flight = new Flight();
        flight.setFlightNumber(request.getFlightNumber());
        flight.setAirlineName(request.getAirlineName());
        flight.setEstDepartureTime(request.getEstDepartureTime());
        flight.setEstArrivalTime(request.getEstArrivalTime());
        flight.setAvailableSeats(request.getAvailableSeats());

        Flight saved = flightRepository.save(flight);
        return toResponse(saved);
    }

    @Async
    @Transactional
    public void createFlightAsync(FlightCreateRequest request) {
        try {
            createFlight(request);
        } catch (Exception e) {
        }
    }

    public FlightSearchResponse searchFlights(String flightNumber, String airlineName,
                                              String estDepartureTimeFrom, String estDepartureTimeTo) {
        List<Flight> flights;

        boolean hasFN = flightNumber != null && !flightNumber.isBlank();
        boolean hasAN = airlineName != null && !airlineName.isBlank();

        if (hasFN && hasAN) {
            flights = flightRepository.findByBothContaining(flightNumber, airlineName);
        } else if (hasFN) {
            flights = flightRepository.findByFlightNumberContaining(flightNumber);
        } else if (hasAN) {
            flights = flightRepository.findByAirlineNameContaining(airlineName);
        } else {
            flights = flightRepository.findAll();
        }

        if (estDepartureTimeFrom != null && !estDepartureTimeFrom.isBlank()) {
            OffsetDateTime from = OffsetDateTime.parse(estDepartureTimeFrom);
            flights = flights.stream()
                    .filter(f -> !f.getEstDepartureTime().isBefore(from))
                    .collect(Collectors.toList());
        }
        if (estDepartureTimeTo != null && !estDepartureTimeTo.isBlank()) {
            OffsetDateTime to = OffsetDateTime.parse(estDepartureTimeTo);
            flights = flights.stream()
                    .filter(f -> !f.getEstDepartureTime().isAfter(to))
                    .collect(Collectors.toList());
        }

        List<FlightResponse> items = flights.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new FlightSearchResponse(items);
    }

    public FlightResponse getFlightById(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found: " + id));
        return toResponse(flight);
    }

    public FlightResponse toResponse(Flight flight) {
        FlightResponse r = new FlightResponse();
        r.setId(flight.getId());
        r.setFlightNumber(flight.getFlightNumber());
        r.setAirlineName(flight.getAirlineName());
        r.setEstDepartureTime(flight.getEstDepartureTime());
        r.setEstArrivalTime(flight.getEstArrivalTime());
        r.setAvailableSeats(flight.getAvailableSeats());
        return r;
    }
}