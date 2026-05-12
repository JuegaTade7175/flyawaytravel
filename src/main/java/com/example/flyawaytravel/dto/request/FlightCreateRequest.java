package com.example.flyawaytravel.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightCreateRequest {

    @NotBlank(message = "airlineName is required")
    private String airlineName;

    @NotBlank(message = "flightNumber is required")
    @Pattern(regexp = "^[A-Z]{2,3}[0-9]{3}$",
             message = "Flight number must match ^[A-Z]{2,3}[0-9]{3}$")
    private String flightNumber;

    @NotNull(message = "estDepartureTime is required")
    private OffsetDateTime estDepartureTime;

    @NotNull(message = "estArrivalTime is required")
    private OffsetDateTime estArrivalTime;

    @NotNull(message = "availableSeats is required")
    @Min(value = 1, message = "availableSeats must be greater than 0")
    private Integer availableSeats;
}