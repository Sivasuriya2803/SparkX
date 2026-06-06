package com.revcast.allocation.entity;

import com.revcast.common.audit.BaseEntity;
import com.revcast.employee.entity.Employee;
import com.revcast.project.entity.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ResourceAllocation entity - represents employee allocation to projects
 */
@Entity
@Table(name = "resource_allocations", indexes = {
        @Index(name = "idx_employee_id", columnList = "employee_id"),
        @Index(name = "idx_project_id", columnList = "project_id"),
        @Index(name = "idx_start_date", columnList = "start_date")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_employee_project_dates", columnNames = {"employee_id", "project_id", "start_date"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"createdAt", "updatedAt", "employee", "project"})
public class ResourceAllocation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "allocation_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal allocationPercentage;

    @Column(name = "billing_rate", precision = 10, scale = 2)
    private BigDecimal billingRate;

    @Column(name = "hours_per_day", precision = 5, scale = 2)
    private BigDecimal hoursPerDay = new BigDecimal("8.0");

    @Column(name = "allocation_status")
    private String allocationStatus;
}

