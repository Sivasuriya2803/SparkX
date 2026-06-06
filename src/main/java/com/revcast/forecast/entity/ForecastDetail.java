package com.revcast.forecast.entity;

import com.revcast.account.entity.Account;
import com.revcast.account.entity.Segment;
import com.revcast.allocation.entity.ResourceAllocation;
import com.revcast.backfill.entity.STBOPosition;
import com.revcast.employee.entity.Employee;
import com.revcast.project.entity.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * ForecastDetail entity - detailed breakdown of forecast by allocation/STBO
 */
@Entity
@Table(name = "forecast_details", indexes = {
        @Index(name = "idx_forecast_id", columnList = "forecast_id"),
        @Index(name = "idx_employee_id", columnList = "employee_id"),
        @Index(name = "idx_project_id", columnList = "project_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForecastDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forecast_id", nullable = false)
    private Forecast forecast;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allocation_id")
    private ResourceAllocation allocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stbo_position_id")
    private STBOPosition stboPosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "segment_id")
    private Segment segment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "billable_days")
    private Integer billableDays;

    @Column(name = "billable_hours", precision = 10, scale = 2)
    private BigDecimal billableHours;

    @Column(name = "billing_rate", precision = 10, scale = 2)
    private BigDecimal billingRate;

    @Column(name = "revenue", precision = 15, scale = 2)
    private BigDecimal revenue;

    @Column(name = "revenue_type") // SBL, STBO
    private String revenueType;
}

