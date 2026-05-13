package com.example.flyawaytravel.repository;

import com.example.flyawaytravel.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    Optional<Flight> findByFlightNumber(String flightNumber);

    boolean existsByFlightNumber(String flightNumber);

    @Query("SELECT f FROM Flight f WHERE UPPER(f.flightNumber) LIKE UPPER(CONCAT('%', :flightNumber, '%')) ORDER BY f.id ASC")
    List<Flight> findByFlightNumberContaining(@Param("flightNumber") String flightNumber);

    @Query("SELECT f FROM Flight f WHERE UPPER(f.airlineName) LIKE UPPER(CONCAT('%', :airlineName, '%')) ORDER BY f.id ASC")
    List<Flight> findByAirlineNameContaining(@Param("airlineName") String airlineName);

    @Query("SELECT f FROM Flight f WHERE UPPER(f.flightNumber) LIKE UPPER(CONCAT('%', :flightNumber, '%')) " +
           "AND UPPER(f.airlineName) LIKE UPPER(CONCAT('%', :airlineName, '%')) ORDER BY f.id ASC")
    List<Flight> findByBothContaining(@Param("flightNumber") String flightNumber,
                                      @Param("airlineName") String airlineName);

    @Query("SELECT f FROM Flight f WHERE f.estDepartureTime >= :from")
    List<Flight> findByDepartureTimeFrom(@Param("from") OffsetDateTime from);

    @Query("SELECT f FROM Flight f WHERE f.estDepartureTime <= :to")
    List<Flight> findByDepartureTimeTo(@Param("to") OffsetDateTime to);

    @Query("SELECT f FROM Flight f WHERE f.estDepartureTime >= :from AND f.estDepartureTime <= :to")
    List<Flight> findByDepartureTimeBetween(@Param("from") OffsetDateTime from,
                                            @Param("to") OffsetDateTime to);
}