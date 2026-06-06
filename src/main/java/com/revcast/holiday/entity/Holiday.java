package com.revcast.holiday.entity;

import com.revcast.common.audit.BaseEntity;
import com.revcast.project.entity.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Holiday entity - represents company and project-specific holidays
 */
@Entity
@Table(name = "holidays", indexes = {
        @Index(name = "idx_holiday_date", columnList = "holiday_date"),
        @Index(name = "idx_is_company_wide", columnList = "is_company_wide")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_holiday_date_project", columnNames = {"holiday_date", "project_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"createdAt", "updatedAt", "project"})
public class Holiday extends BaseEntity {

    @Column(name = "holiday_name", nullable = false)
    private String holidayName;

    @Column(name = "holiday_date", nullable = false)
    private LocalDate holidayDate;

    @Column(name = "holiday_type")
    private String holidayType; // COMPANY, CLIENT, REGIONAL, etc.

    @Column(name = "is_company_wide")
    private Boolean isCompanyWide = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "description")
    private String description;
}

