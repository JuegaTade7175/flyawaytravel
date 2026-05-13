package com.example.flyawaytravel.controller;

import com.example.flyawaytravel.dto.request.FlightCreateManyRequest;
import com.example.flyawaytravel.dto.request.FlightCreateRequest;
import com.example.flyawaytravel.dto.response.FlightCreateManyResponse;
import com.example.flyawaytravel.dto.response.FlightResponse;
import com.example.flyawaytravel.dto.response.FlightSearchResponse;
import com.example.flyawaytravel.dto.response.NewIdResponse;
import com.example.flyawaytravel.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping("/create")
    public ResponseEntity<NewIdResponse> createFlight(@Valid @RequestBody FlightCreateRequest request) {
        FlightResponse response = flightService.createFlight(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new NewIdResponse(response.getId()));
    }

    @PostMapping("/create-many")
    public ResponseEntity<FlightCreateManyResponse> createManyFlights(
            @Valid @RequestBody FlightCreateManyRequest request) {

        for (var flightReq : request.getInputs()) {
            flightService.createFlightAsync(flightReq);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new FlightCreateManyResponse(new ArrayList<>()));
    }

    @GetMapping("/search")
    public ResponseEntity<FlightSearchResponse> searchFlights(
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String airlineName,
            @RequestParam(required = false) String estDepartureTimeFrom,
            @RequestParam(required = false) String estDepartureTimeTo) {

        FlightSearchResponse response = flightService.searchFlights(
                flightNumber, airlineName, estDepartureTimeFrom, estDepartureTimeTo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponse> getFlightById(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getFlightById(id));
    }
}