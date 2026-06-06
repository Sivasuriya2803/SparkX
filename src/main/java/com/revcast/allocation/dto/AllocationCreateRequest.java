package com.revcast.allocation.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Resource Allocation Create Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllocationCreateRequest {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "Allocation percentage is required")
    @DecimalMin(value = "0.01", message = "Allocation percentage must be greater than 0")
    @DecimalMax(value = "100", message = "Allocation percentage must not exceed 100")
    private BigDecimal allocationPercentage;

    private BigDecimal billingRate;

    @DecimalMin(value = "1", message = "Hours per day must be at least 1")
    @DecimalMax(value = "24", message = "Hours per day must not exceed 24")
    private BigDecimal hoursPerDay;
}

