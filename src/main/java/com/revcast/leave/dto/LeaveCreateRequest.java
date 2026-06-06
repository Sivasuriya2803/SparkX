package com.revcast.leave.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Leave Record Create Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveCreateRequest {

    @NotNull(message = "Leave type is required")
    private String leaveType;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Positive(message = "Number of days must be positive")
    private Integer numberOfDays;

    private String reason;
}

