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

    @Query("SELECT f FROM Flight f WHERE f.availableSeats > 0")
    List<Flight> findAllAvailable();

    @Query("SELECT f FROM Flight f WHERE LOWER(f.flightNumber) LIKE LOWER(CONCAT('%', :flightNumber, '%')) AND f.availableSeats > 0")
    List<Flight> findByFlightNumberContaining(@Param("flightNumber") String flightNumber);

    @Query("SELECT f FROM Flight f WHERE LOWER(f.airline) LIKE LOWER(CONCAT('%', :airline, '%')) AND f.availableSeats > 0")
    List<Flight> findByAirlineContaining(@Param("airline") String airline);

    @Query("SELECT f FROM Flight f WHERE LOWER(f.flightNumber) LIKE LOWER(CONCAT('%', :flightNumber, '%')) AND LOWER(f.airline) LIKE LOWER(CONCAT('%', :airline, '%')) AND f.availableSeats > 0")
    List<Flight> findByBothContaining(@Param("flightNumber") String flightNumber, @Param("airline") String airline);

    @Query("SELECT f FROM Flight f WHERE f.departureTime > :now AND f.availableSeats > 0")
    List<Flight> findAvailableFutureFlights(@Param("now") LocalDateTime now);
}