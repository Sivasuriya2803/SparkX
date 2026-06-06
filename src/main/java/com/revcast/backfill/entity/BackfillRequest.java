package com.revcast.backfill.entity;

import com.revcast.common.audit.BaseEntity;
import com.revcast.employee.entity.Employee;
import com.revcast.resignation.entity.Resignation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * BackfillRequest entity - represents backfill requests for resigned employees
 */
@Entity
@Table(name = "backfill_requests", indexes = {
        @Index(name = "idx_resignation_id", columnList = "resignation_id"),
        @Index(name = "idx_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"createdAt", "updatedAt", "resignation", "stboPosition", "backfillEmployee"})
public class BackfillRequest extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resignation_id", nullable = false)
    private Resignation resignation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stbo_position_id")
    private STBOPosition stboPosition;

    @Column(name = "requested_date", nullable = false)
    private LocalDate requestedDate;

    @Column(name = "priority")
    private String priority; // HIGH, MEDIUM, LOW

    @Column(name = "status")
    private String status; // OPEN, IN_PROGRESS, FILLED, CLOSED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "backfill_employee_id")
    private Employee backfillEmployee;

    @Column(name = "backfill_date")
    private LocalDate backfillDate;
}

