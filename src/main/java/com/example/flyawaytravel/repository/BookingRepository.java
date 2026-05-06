package com.example.flyawaytravel.repository;

import com.example.flyawaytravel.entity.Booking;
import com.example.flyawaytravel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    Optional<Booking> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.flight.id = :flightId")
    Integer countBookingsByFlightId(@Param("flightId") Long flightId);

    @Query("SELECT b FROM Booking b JOIN b.flight f WHERE b.user = :user AND " +
           "((f.departureTime BETWEEN :start1 AND :end1) OR (f.arrivalTime BETWEEN :start2 AND :end2))")
    List<Booking> findConflictingBookings(@Param("user") User user,
                                         @Param("start1") LocalDateTime start1,
                                         @Param("end1") LocalDateTime end1,
                                         @Param("start2") LocalDateTime start2,
                                         @Param("end2") LocalDateTime end2);
}