package com.revcast.variance.entity;

import com.revcast.common.audit.BaseEntity;
import com.revcast.forecast.entity.Forecast;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * VarianceAnalysis entity - represents variance between forecasts
 */
@Entity
@Table(name = "variance_analysis", indexes = {
        @Index(name = "idx_current_forecast_id", columnList = "current_forecast_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"createdAt", "updatedAt", "currentForecast", "previousForecast", "reasonsList"})
public class VarianceAnalysis extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_forecast_id", nullable = false)
    private Forecast currentForecast;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_forecast_id")
    private Forecast previousForecast;

    @Column(name = "variance_amount", precision = 15, scale = 2)
    private BigDecimal varianceAmount;

    @Column(name = "variance_percentage", precision = 10, scale = 2)
    private BigDecimal variancePercentage;

    @Column(name = "analysis_status")
    private String analysisStatus; // COMPLETED, IN_PROGRESS, PENDING

    @OneToMany(mappedBy = "varianceAnalysis", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VarianceReason> reasonsList;
}

