package com.example.flyawaytravel.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private FlightResponse flight;
    private String customerName;
    private LocalDateTime bookingDate;
}