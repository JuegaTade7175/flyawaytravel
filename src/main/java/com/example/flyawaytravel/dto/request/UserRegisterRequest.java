package com.example.flyawaytravel.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "First name must start with uppercase letter and contain only letters")
    @NotBlank(message = "First name is required")
    private String firstName;

    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "Last name must start with uppercase letter and contain only letters")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$", message = "Password must be at least 8 characters long and contain at least 1 letter and 1 number")
    @NotBlank(message = "Password is required")
    private String password;
}