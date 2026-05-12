package com.example.flyawaytravel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightResponse {
    private Long id;
    private String flightNumber;
    private String airlineName;
    private OffsetDateTime estDepartureTime;
    private OffsetDateTime estArrivalTime;
    private Integer availableSeats;
}