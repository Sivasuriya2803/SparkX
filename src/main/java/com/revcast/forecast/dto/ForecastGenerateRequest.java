package com.revcast.forecast.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Forecast Generate Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForecastGenerateRequest {

    @NotBlank(message = "Forecast type is required (WEEKLY, MONTHLY, QUARTERLY)")
    private String forecastType;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private Long accountId;

    private Long segmentId;

    private Long projectId;
}

