package com.revcast.account.entity;

import com.revcast.common.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Segment entity - represents business segments within accounts
 */
@Entity
@Table(name = "segments", indexes = {
        @Index(name = "idx_segment_code", columnList = "segment_code"),
        @Index(name = "idx_account_id", columnList = "account_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"createdAt", "updatedAt", "account"})
public class Segment extends BaseEntity {

    @Column(name = "segment_name", nullable = false, unique = true)
    private String segmentName;

    @Column(name = "segment_code", nullable = false, unique = true)
    private String segmentCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive = true;
}

