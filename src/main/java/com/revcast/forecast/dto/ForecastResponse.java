package com.revcast.forecast.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Forecast Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ForecastResponse {

    private Long id;
    private String forecastCode;
    private String forecastType;
    private String forecastPeriod;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalRevenue;
    private BigDecimal sblRevenue;
    private BigDecimal stboRevenue;
    private String status;
    private List<ForecastDetailResponse> details;
}

