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
    @Pattern(regexp = "^[A-Z0-9]{1,6}$", message = "El número de vuelo debe contener solo letras mayúsculas y números, máximo 6 caracteres")
    @NotBlank(message = "El número de vuelo es requerido")
    private String flightNumber;

    @Column(nullable = false)
    @NotBlank(message = "El nombre de la aerolínea es requerido")
    private String airline;

    @Column(nullable = false)
    @NotNull(message = "La hora de salida es requerida")
    private LocalDateTime departureTime;

    @Column(nullable = false)
    @NotNull(message = "La hora de llegada es requerida")
    private LocalDateTime arrivalTime;

    @Column(nullable = false)
    @Min(value = 1, message = "Los asientos disponibles deben ser mayores a 0")
    private Integer availableSeats;

    @Column(nullable = false)
    @NotBlank(message = "El origen es requerido")
    private String origin;

    @Column(nullable = false)
    @NotBlank(message = "El destino es requerido")
    private String destination;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    @PreUpdate
    private void validateTimes() {
        if (departureTime != null && arrivalTime != null && !departureTime.isBefore(arrivalTime)) {
            throw new IllegalArgumentException("La hora de salida debe ser antes de la hora de llegada");
        }
    }
}