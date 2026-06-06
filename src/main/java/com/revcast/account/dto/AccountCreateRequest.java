package com.revcast.account.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Account Create Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountCreateRequest {

    @NotBlank(message = "Account name is required")
    private String accountName;

    @NotBlank(message = "Account code is required")
    private String accountCode;

    private String description;
}

