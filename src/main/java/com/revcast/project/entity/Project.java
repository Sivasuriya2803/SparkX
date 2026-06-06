package com.revcast.project.entity;

import com.revcast.account.entity.Segment;
import com.revcast.common.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Project entity - represents projects within segments
 */
@Entity
@Table(name = "projects", indexes = {
        @Index(name = "idx_project_code", columnList = "project_code"),
        @Index(name = "idx_segment_id", columnList = "segment_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"createdAt", "updatedAt", "segment"})
public class Project extends BaseEntity {

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name = "project_code", nullable = false, unique = true)
    private String projectCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "segment_id", nullable = false)
    private Segment segment;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive = true;
}

