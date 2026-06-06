package com.revcast.forecast.entity;

import com.revcast.common.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Forecast entity - represents revenue forecasts
 */
@Entity
@Table(name = "forecasts", indexes = {
        @Index(name = "idx_forecast_type", columnList = "forecast_type"),
        @Index(name = "idx_start_date", columnList = "start_date"),
        @Index(name = "idx_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"createdAt", "updatedAt"})
public class Forecast extends BaseEntity {

    @Column(name = "forecast_code", nullable = false, unique = true)
    private String forecastCode;

    @Column(name = "forecast_type", nullable = false)
    private String forecastType; // WEEKLY, MONTHLY, QUARTERLY

    @Column(name = "forecast_period", nullable = false)
    private String forecastPeriod; // e.g., "2026-W01", "2026-01", "2026-Q1"

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "total_revenue", precision = 15, scale = 2)
    private BigDecimal totalRevenue;

    @Column(name = "sbl_revenue", precision = 15, scale = 2)
    private BigDecimal sblRevenue;

    @Column(name = "stbo_revenue", precision = 15, scale = 2)
    private BigDecimal stboRevenue;

    @Column(name = "status")
    private String status; // DRAFT, FINALIZED, PUBLISHED, ARCHIVED
}

