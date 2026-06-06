package com.revcast.variance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Variance Analysis Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VarianceResponse {

    private Long id;
    private Long currentForecastId;
    private Long previousForecastId;
    private BigDecimal varianceAmount;
    private BigDecimal variancePercentage;
    private String analysisStatus;
    private List<VarianceReasonResponse> reasons;
}

