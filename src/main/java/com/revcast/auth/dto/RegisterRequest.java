package com.revcast.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Schema(description = "Unique username for the user", example = "john.doe")
    private String username;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Schema(description = "Password for the user", example = "password123")
    private String password;

    @NotBlank(message = "First name cannot be empty")
    @Schema(description = "First name of the user", example = "John")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Schema(description = "Last name of the user", example = "Doe")
    private String lastName;

    @Schema(description = "Role name for the user", example = "USER")
    private String roleName; // Assuming a default role or specified during registration
}
