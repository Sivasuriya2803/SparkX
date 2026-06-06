package com.revcast.backfill.entity;

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
 * STBOPosition entity - Sold To Be Offered positions
 * Represents revenue from positions sold but not yet filled
 */
@Entity
@Table(name = "stbo_positions", indexes = {
        @Index(name = "idx_project_id", columnList = "project_id"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_filled_employee_id", columnList = "filled_employee_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"createdAt", "updatedAt", "project", "filledEmployee"})
public class STBOPosition extends BaseEntity {

    @Column(name = "position_code", nullable = false, unique = true)
    private String positionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "designation")
    private String designation;

    @Column(name = "billing_rate", nullable = false, precision = 10, scale = 2)
    private BigDecimal billingRate;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "expected_fill_date")
    private LocalDate expectedFillDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filled_employee_id")
    private Employee filledEmployee;

    @Column(name = "status", nullable = false)
    private String status; // OPEN, FILLED, CLOSED, ON_HOLD

    @Column(name = "reason_for_backfill")
    private String reasonForBackfill;
}

