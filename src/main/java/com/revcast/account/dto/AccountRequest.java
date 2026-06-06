package com.revcast.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class AccountRequest {

    @NotBlank(message = "Account name cannot be empty")
    @Size(max = 255, message = "Account name cannot exceed 255 characters")
    @Schema(description = "Name of the account", example = "Virtusa Corp")
    private String accountName;

    @NotBlank(message = "Account code cannot be empty")
    @Size(max = 50, message = "Account code cannot exceed 50 characters")
    @Schema(description = "Unique code for the account", example = "VIRTUSA001")
    private String accountCode;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Schema(description = "Description of the account", example = "Primary account for Virtusa Corporation")
    private String description;

    @Schema(description = "Is the account currently active?", example = "true")
    private Boolean isActive = true;
}
