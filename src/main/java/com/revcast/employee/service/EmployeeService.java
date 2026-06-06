package com.revcast.employee.service;

import com.revcast.common.exception.ResourceNotFoundException;
import com.revcast.common.exception.ValidationException;
import com.revcast.employee.dto.EmployeeCreateRequest;
import com.revcast.employee.dto.EmployeeResponse;
import com.revcast.employee.entity.Employee;
import com.revcast.employee.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Employee Service
 */
@Slf4j
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Create new employee
     */
    @Transactional
    public EmployeeResponse createEmployee(EmployeeCreateRequest request) {
        log.info("Creating new employee: {}", request.getEmployeeCode());

        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new ValidationException("Employee with code " + request.getEmployeeCode() + " already exists");
        }

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("Employee with email " + request.getEmail() + " already exists");
        }

        Employee employee = Employee.builder()
                .employeeCode(request.getEmployeeCode())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .dateOfJoining(request.getDateOfJoining())
                .designation(request.getDesignation())
                .billingRate(request.getBillingRate())
                .isActive(true)
                .build();

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully: {}", savedEmployee.getId());

        return mapToResponse(savedEmployee);
    }

    /**
     * Get employee by ID
     */
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        return mapToResponse(employee);
    }

    /**
     * Get employee by code
     */
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeByCode(String employeeCode) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeCode", employeeCode));
        return mapToResponse(employee);
    }

    /**
     * Get all active employees
     */
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllActiveEmployees() {
        return employeeRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update employee
     */
    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeCreateRequest request) {
        log.info("Updating employee: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setPhone(request.getPhone());
        employee.setDesignation(request.getDesignation());
        employee.setBillingRate(request.getBillingRate());

        Employee updatedEmployee = employeeRepository.save(employee);
        log.info("Employee updated successfully: {}", id);

        return mapToResponse(updatedEmployee);
    }

    /**
     * Deactivate employee
     */
    @Transactional
    public EmployeeResponse deactivateEmployee(Long id) {
        log.info("Deactivating employee: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        employee.setIsActive(false);
        Employee updatedEmployee = employeeRepository.save(employee);

        log.info("Employee deactivated: {}", id);
        return mapToResponse(updatedEmployee);
    }

    /**
     * Record employee resignation
     */
    @Transactional
    public void recordEmployeeResignation(Long employeeId, LocalDate lastWorkingDay) {
        log.info("Recording resignation for employee: {} with LWD: {}", employeeId, lastWorkingDay);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

        employee.setLastWorkingDay(lastWorkingDay);
        employeeRepository.save(employee);

        log.info("Employee resignation recorded: {}", employeeId);
    }

    /**
     * Map Employee entity to response DTO
     */
    private EmployeeResponse mapToResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .employeeCode(employee.getEmployeeCode())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .dateOfBirth(employee.getDateOfBirth())
                .dateOfJoining(employee.getDateOfJoining())
                .lastWorkingDay(employee.getLastWorkingDay())
                .designation(employee.getDesignation())
                .billingRate(employee.getBillingRate())
                .isActive(employee.getIsActive())
                .build();
    }
}

