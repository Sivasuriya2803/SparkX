package com.revcast.forecast.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Forecast Detail Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForecastDetailResponse {

    private Long id;
    private String employeeName;
    private String projectName;
    private String segmentName;
    private String accountName;
    private Integer billableDays;
    private BigDecimal billableHours;
    private BigDecimal billingRate;
    private BigDecimal revenue;
    private String revenueType; // SBL or STBO
}

