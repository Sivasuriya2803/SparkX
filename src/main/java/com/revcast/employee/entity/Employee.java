package com.revcast.employee.entity;

import com.revcast.common.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Employee entity - represents employees in the organization
 */
@Entity
@Table(name = "employees", indexes = {
        @Index(name = "idx_employee_code", columnList = "employee_code"),
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_date_of_joining", columnList = "date_of_joining")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"createdAt", "updatedAt"})
public class Employee extends BaseEntity {

    @Column(name = "employee_code", nullable = false, unique = true)
    private String employeeCode;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "date_of_joining", nullable = false)
    private LocalDate dateOfJoining;

    @Column(name = "last_working_day")
    private LocalDate lastWorkingDay;

    @Column(name = "designation")
    private String designation;

    @Column(name = "billing_rate", precision = 10, scale = 2)
    private BigDecimal billingRate;

    @Column(name = "is_active")
    private Boolean isActive = true;
}

