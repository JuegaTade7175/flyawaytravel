package com.example.flyawaytravel.repository;

import com.example.flyawaytravel.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    Optional<Flight> findByFlightNumber(String flightNumber);

    boolean existsByFlightNumber(String flightNumber);

    @Query("SELECT f FROM Flight f WHERE " +
           "(:flightNumber IS NULL OR LOWER(f.flightNumber) LIKE LOWER(CONCAT('%', CAST(:flightNumber AS string), '%'))) AND " +
           "(:airline IS NULL OR LOWER(f.airline) LIKE LOWER(CONCAT('%', CAST(:airline AS string), '%'))) AND " +
           "(:startDate IS NULL OR f.departureTime >= :startDate) AND " +
           "(:endDate IS NULL OR f.departureTime <= :endDate) AND " +
           "f.availableSeats > 0")
    List<Flight> searchFlights(@Param("flightNumber") String flightNumber,
                               @Param("airline") String airline,
                               @Param("startDate") LocalDateTime startDate,
                               @Param("endDate") LocalDateTime endDate);

    @Query("SELECT f FROM Flight f WHERE f.departureTime > :now AND f.availableSeats > 0")
    List<Flight> findAvailableFutureFlights(@Param("now") LocalDateTime now);
}