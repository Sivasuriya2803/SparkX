package com.revcast.leave.repository;

import com.revcast.leave.entity.LeaveRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Leave Record Repository
 */
@Repository
public interface LeaveRecordRepository extends JpaRepository<LeaveRecord, Long> {

    List<LeaveRecord> findByEmployeeId(Long employeeId);

    List<LeaveRecord> findByEmployeeIdAndStatus(Long employeeId, String status);

    @Query("SELECT lr FROM LeaveRecord lr WHERE " +
            "lr.employee.id = :employeeId AND " +
            "lr.startDate BETWEEN :startDate AND :endDate")
    List<LeaveRecord> findLeavesForEmployeeInDateRange(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT lr FROM LeaveRecord lr WHERE " +
            "lr.startDate BETWEEN :startDate AND :endDate AND " +
            "lr.status = 'APPROVED'")
    List<LeaveRecord> findApprovedLeavesInDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<LeaveRecord> findByLeaveType(String leaveType);
}

