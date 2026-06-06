package com.revcast.leave.service;

import com.revcast.common.constants.AppConstants;
import com.revcast.common.exception.ResourceNotFoundException;
import com.revcast.common.exception.ValidationException;
import com.revcast.employee.entity.Employee;
import com.revcast.employee.repository.EmployeeRepository;
import com.revcast.leave.entity.LeaveRecord;
import com.revcast.leave.repository.LeaveRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Leave Service
 */
@Slf4j
@Service
public class LeaveService {

    @Autowired
    private LeaveRecordRepository leaveRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Create leave record
     */
    @Transactional
    public LeaveRecord createLeave(Long employeeId, LeaveRecord leave) {
        log.info("Creating leave for employee: {}", employeeId);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

        // Validate leave dates
        if (leave.getEndDate().isBefore(leave.getStartDate())) {
            throw new ValidationException("End date cannot be before start date");
        }

        leave.setEmployee(employee);
        leave.setStatus(AppConstants.LEAVE_STATUS_PENDING);

        return leaveRepository.save(leave);
    }

    /**
     * Get leaves for employee
     */
    @Transactional(readOnly = true)
    public List<LeaveRecord> getLeavesForEmployee(Long employeeId) {
        return leaveRepository.findByEmployeeId(employeeId);
    }

    /**
     * Get approved leaves for employee
     */
    @Transactional(readOnly = true)
    public List<LeaveRecord> getApprovedLeavesForEmployee(Long employeeId) {
        return leaveRepository.findByEmployeeIdAndStatus(employeeId, AppConstants.LEAVE_STATUS_APPROVED);
    }

    /**
     * Approve leave
     */
    @Transactional
    public LeaveRecord approveLeave(Long leaveId) {
        log.info("Approving leave: {}", leaveId);

        LeaveRecord leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("LeaveRecord", "id", leaveId));

        leave.setStatus(AppConstants.LEAVE_STATUS_APPROVED);
        return leaveRepository.save(leave);
    }

    /**
     * Reject leave
     */
    @Transactional
    public LeaveRecord rejectLeave(Long leaveId) {
        log.info("Rejecting leave: {}", leaveId);

        LeaveRecord leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("LeaveRecord", "id", leaveId));

        leave.setStatus(AppConstants.LEAVE_STATUS_REJECTED);
        return leaveRepository.save(leave);
    }

    /**
     * Get leaves in date range
     */
    @Transactional(readOnly = true)
    public List<LeaveRecord> getLeavesInDateRange(LocalDate startDate, LocalDate endDate) {
        return leaveRepository.findApprovedLeavesInDateRange(startDate, endDate);
    }
}

