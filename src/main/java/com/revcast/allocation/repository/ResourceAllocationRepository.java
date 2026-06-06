package com.revcast.allocation.repository;

import com.revcast.allocation.entity.ResourceAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Resource Allocation Repository
 */
@Repository
public interface ResourceAllocationRepository extends JpaRepository<ResourceAllocation, Long> {

    List<ResourceAllocation> findByEmployeeId(Long employeeId);

    List<ResourceAllocation> findByProjectId(Long projectId);

    @Query("SELECT ra FROM ResourceAllocation ra WHERE " +
            "ra.employee.id = :employeeId AND " +
            "ra.startDate <= :date AND (ra.endDate IS NULL OR ra.endDate >= :date)")
    List<ResourceAllocation> findActiveAllocationsForEmployeeOnDate(
            @Param("employeeId") Long employeeId,
            @Param("date") LocalDate date);

    @Query("SELECT ra FROM ResourceAllocation ra WHERE " +
            "ra.project.id = :projectId AND " +
            "ra.startDate <= :date AND (ra.endDate IS NULL OR ra.endDate >= :date)")
    List<ResourceAllocation> findActiveAllocationsForProjectOnDate(
            @Param("projectId") Long projectId,
            @Param("date") LocalDate date);

    @Query("SELECT ra FROM ResourceAllocation ra WHERE " +
            "ra.startDate BETWEEN :startDate AND :endDate OR " +
            "(ra.endDate IS NOT NULL AND ra.endDate BETWEEN :startDate AND :endDate)")
    List<ResourceAllocation> findAllocationsInDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}

