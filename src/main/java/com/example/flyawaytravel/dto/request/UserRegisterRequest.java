package com.example.flyawaytravel.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {

    @NotBlank(message = "email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "firstName is required")
    @Pattern(regexp = "^[A-Z].*$", message = "First name must start with uppercase letter A-Z")
    private String firstName;

    @NotBlank(message = "lastName is required")
    @Pattern(regexp = "^[A-Z].*$", message = "Last name must start with uppercase letter A-Z")
    private String lastName;

    @NotBlank(message = "password is required")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9]).{8,}$",
             message = "Password must be at least 8 characters with at least one letter and one number")
    private String password;
}