package com.example.flyawaytravel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private OffsetDateTime bookingDate;
    private Long flightId;
    private String flightNumber;
    private Long customerId;
    private String customerFirstName;
    private String customerLastName;
    private OffsetDateTime estDepartureTime;
    private OffsetDateTime estArrivalTime;
}