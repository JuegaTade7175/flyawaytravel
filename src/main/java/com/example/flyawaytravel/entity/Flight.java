package com.example.flyawaytravel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "flights")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 6)
    @Pattern(regexp = "^[A-Z0-9]{1,6}$", message = "Flight number must contain only uppercase letters and numbers, max 6 characters")
    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @Column(nullable = false)
    @NotBlank(message = "Airline name is required")
    private String airline;

    @Column(nullable = false)
    @NotNull(message = "Departure time is required")
    private LocalDateTime departureTime;

    @Column(nullable = false)
    @NotNull(message = "Arrival time is required")
    private LocalDateTime arrivalTime;

    @Column(nullable = false)
    @Min(value = 1, message = "Available seats must be greater than 0")
    private Integer availableSeats;

    @Column(nullable = false)
    @NotBlank(message = "Origin is required")
    private String origin;

    @Column(nullable = false)
    @NotBlank(message = "Destination is required")
    private String destination;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    @PreUpdate
    private void validateTimes() {
        if (departureTime != null && arrivalTime != null && !departureTime.isBefore(arrivalTime)) {
            throw new IllegalArgumentException("Departure time must be before arrival time");
        }
    }
}