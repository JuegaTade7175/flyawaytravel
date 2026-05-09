package com.example.flyawaytravel.service;

import com.example.flyawaytravel.dto.request.FlightCreateRequest;
import com.example.flyawaytravel.dto.response.FlightResponse;
import com.example.flyawaytravel.entity.Flight;
import com.example.flyawaytravel.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final ModelMapper modelMapper;

    public FlightResponse createFlight(FlightCreateRequest request) {
        if (flightRepository.existsByFlightNumber(request.getFlightNumber())) {
            throw new IllegalArgumentException("El número de vuelo ya existe: " + request.getFlightNumber());
        }
        Flight flight = modelMapper.map(request, Flight.class);
        Flight savedFlight = flightRepository.save(flight);
        return modelMapper.map(savedFlight, FlightResponse.class);
    }

    public List<FlightResponse> searchFlights(String flightNumber, String airline,
                                              LocalDateTime startDate, LocalDateTime endDate) {
        List<Flight> flights;

        boolean hasFlightNumber = flightNumber != null && !flightNumber.isBlank();
        boolean hasAirline = airline != null && !airline.isBlank();

        if (hasFlightNumber && hasAirline) {
            flights = flightRepository.findByBothContaining(flightNumber, airline);
        } else if (hasFlightNumber) {
            flights = flightRepository.findByFlightNumberContaining(flightNumber);
        } else if (hasAirline) {
            flights = flightRepository.findByAirlineContaining(airline);
        } else {
            flights = flightRepository.findAllAvailable();
        }

        return flights.stream()
                .filter(f -> startDate == null || !f.getDepartureTime().isBefore(startDate))
                .filter(f -> endDate == null || !f.getDepartureTime().isAfter(endDate))
                .map(f -> modelMapper.map(f, FlightResponse.class))
                .collect(Collectors.toList());
    }

    public FlightResponse getFlightById(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vuelo no encontrado con el id: " + id));
        return modelMapper.map(flight, FlightResponse.class);
    }

    public List<FlightResponse> getAvailableFutureFlights() {
        return flightRepository.findAvailableFutureFlights(LocalDateTime.now())
                .stream()
                .map(f -> modelMapper.map(f, FlightResponse.class))
                .collect(Collectors.toList());
    }
}