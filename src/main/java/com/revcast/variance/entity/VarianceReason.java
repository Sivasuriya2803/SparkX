package com.revcast.variance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * VarianceReason entity - represents individual reasons for variance
 */
@Entity
@Table(name = "variance_reasons", indexes = {
        @Index(name = "idx_variance_analysis_id", columnList = "variance_analysis_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VarianceReason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variance_analysis_id", nullable = false)
    private VarianceAnalysis varianceAnalysis;

    @Column(name = "reason_type", nullable = false)
    private String reasonType; // EMPLOYEE_JOINED, EMPLOYEE_RELEASED, EMPLOYEE_RESIGNED, etc.

    @Column(name = "impact_amount", precision = 15, scale = 2)
    private BigDecimal impactAmount;

    @Column(name = "description")
    private String description;
}

