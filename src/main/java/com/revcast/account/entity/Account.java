package com.revcast.account.entity;

import com.revcast.common.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Account entity - represents customer accounts
 */
@Entity
@Table(name = "accounts", indexes = {
        @Index(name = "idx_account_code", columnList = "account_code")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"createdAt", "updatedAt"})
public class Account extends BaseEntity {

  Long id;


    @Column(name = "account_name", nullable = false, unique = true)
    private String accountName;

    @Column(name = "account_code", nullable = false, unique = true)
    private String accountCode;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive = true;
}

