package com.revcast.allocation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Resource Allocation Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllocationResponse {

    private Long id;
    private Long employeeId;
    private String employeeName;
    private Long projectId;
    private String projectName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal allocationPercentage;
    private BigDecimal billingRate;
    private BigDecimal hoursPerDay;
    private String allocationStatus;
}

