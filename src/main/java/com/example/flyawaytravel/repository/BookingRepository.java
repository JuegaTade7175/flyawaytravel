package com.example.flyawaytravel.repository;

import com.example.flyawaytravel.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.flight.id = :flightId")
    int countByFlightId(@Param("flightId") Long flightId);

    @Query("SELECT b FROM Booking b JOIN b.flight f " +
           "WHERE b.user.id = :userId " +
           "AND f.id != :flightId " +
           "AND :newDeparture < f.estArrivalTime " +
           "AND :newArrival > f.estDepartureTime")
    List<Booking> findOverlapping(@Param("userId") Long userId,
                                  @Param("flightId") Long flightId,
                                  @Param("newDeparture") OffsetDateTime newDeparture,
                                  @Param("newArrival") OffsetDateTime newArrival);
}