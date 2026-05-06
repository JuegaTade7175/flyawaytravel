package com.example.flyawaytravel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Column(nullable = false)
    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "First name must start with uppercase letter and contain only letters")
    @NotBlank(message = "First name is required")
    private String firstName;

    @Column(nullable = false)
    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "Last name must start with uppercase letter and contain only letters")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Column(nullable = false)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$", message = "Password must be at least 8 characters long and contain at least 1 letter and 1 number")
    @NotBlank(message = "Password is required")
    private String password;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}