package com.revcast.variance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Variance Reason Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VarianceReasonResponse {

    private String reason;
    private BigDecimal impact;
    private String description;
}

