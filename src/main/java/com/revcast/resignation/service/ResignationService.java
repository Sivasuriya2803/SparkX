package com.revcast.resignation.service;

import com.revcast.backfill.entity.BackfillRequest;
import com.revcast.backfill.entity.STBOPosition;
import com.revcast.backfill.repository.BackfillRequestRepository;
import com.revcast.backfill.repository.STBOPositionRepository;
import com.revcast.common.constants.AppConstants;
import com.revcast.common.exception.ResourceNotFoundException;
import com.revcast.employee.entity.Employee;
import com.revcast.employee.repository.EmployeeRepository;
import com.revcast.resignation.entity.Resignation;
import com.revcast.resignation.repository.ResignationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Resignation Service
 */
@Slf4j
@Service
public class ResignationService {

    @Autowired
    private ResignationRepository resignationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private STBOPositionRepository stboPositionRepository;

    @Autowired
    private BackfillRequestRepository backfillRepository;

    /**
     * Record employee resignation
     */
    @Transactional
    public Resignation recordResignation(Long employeeId, LocalDate resignationDate, LocalDate lastWorkingDay) {
        log.info("Recording resignation for employee: {} with LWD: {}", employeeId, lastWorkingDay);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

        // Create resignation record
        Resignation resignation = Resignation.builder()
                .employee(employee)
                .resignationDate(resignationDate)
                .lastWorkingDay(lastWorkingDay)
                .status(AppConstants.RESIGNATION_STATUS_PENDING)
                .build();

        Resignation savedResignation = resignationRepository.save(resignation);

        // Update employee
        employee.setLastWorkingDay(lastWorkingDay);
        employeeRepository.save(employee);

        // Create backfill request
        createBackfillRequest(savedResignation, employee);

        log.info("Resignation recorded: {}", savedResignation.getId());
        return savedResignation;
    }

    /**
     * Approve resignation
     */
    @Transactional
    public Resignation approveResignation(Long resignationId) {
        log.info("Approving resignation: {}", resignationId);

        Resignation resignation = resignationRepository.findById(resignationId)
                .orElseThrow(() -> new ResourceNotFoundException("Resignation", "id", resignationId));

        resignation.setStatus(AppConstants.RESIGNATION_STATUS_APPROVED);
        return resignationRepository.save(resignation);
    }

    /**
     * Create backfill request for resigned employee
     */
    private void createBackfillRequest(Resignation resignation, Employee employee) {
        log.info("Creating backfill for resigned employee: {}", employee.getId());

        // Create STBO position for backfill
        // Get employee's current project allocation (ideally the latest one)
        // For simplicity, we'll create a generic STBO position

        STBOPosition stboPosition = STBOPosition.builder()
                .positionCode("BACKFILL-" + resignation.getId())
                .designation(employee.getDesignation())
                .billingRate(employee.getBillingRate())
                .startDate(resignation.getLastWorkingDay().plusDays(1))
                .status(AppConstants.STBO_STATUS_OPEN)
                .reasonForBackfill("Replacement for " + employee.getFirstName() + " " + employee.getLastName())
                .build();

        // Note: In a real scenario, you'd get the actual project from current allocation
        // STBOPosition would have proper project reference

        // Create backfill request
        BackfillRequest backfillRequest = BackfillRequest.builder()
                .resignation(resignation)
                .requestedDate(LocalDate.now())
                .priority(AppConstants.BACKFILL_STATUS_OPEN)
                .status(AppConstants.BACKFILL_STATUS_OPEN)
                .build();

        backfillRepository.save(backfillRequest);
        log.info("Backfill request created for resignation: {}", resignation.getId());
    }

    /**
     * Get resignations in date range
     */
    @Transactional(readOnly = true)
    public List<Resignation> getResignationsInDateRange(LocalDate startDate, LocalDate endDate) {
        return resignationRepository.findByLastWorkingDayAfter(startDate);
    }

    /**
     * Get pending resignations
     */
    @Transactional(readOnly = true)
    public List<Resignation> getPendingResignations() {
        return resignationRepository.findByStatus(AppConstants.RESIGNATION_STATUS_PENDING);
    }
}

