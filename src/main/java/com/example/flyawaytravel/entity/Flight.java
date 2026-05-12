package com.example.flyawaytravel.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "flights")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 10)
    private String flightNumber;

    @Column(name = "airline", nullable = false)
    private String airlineName;

    @Column(name = "departure_time", nullable = false)
    private OffsetDateTime estDepartureTime;

    @Column(name = "arrival_time", nullable = false)
    private OffsetDateTime estArrivalTime;

    @Column(nullable = false)
    private Integer availableSeats;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;
}