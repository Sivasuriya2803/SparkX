package com.revcast.allocation.service;

import com.revcast.allocation.dto.AllocationCreateRequest;
import com.revcast.allocation.dto.AllocationResponse;
import com.revcast.allocation.entity.ResourceAllocation;
import com.revcast.allocation.repository.ResourceAllocationRepository;
import com.revcast.common.exception.ResourceNotFoundException;
import com.revcast.common.exception.ValidationException;
import com.revcast.employee.entity.Employee;
import com.revcast.employee.repository.EmployeeRepository;
import com.revcast.project.entity.Project;
import com.revcast.project.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Resource Allocation Service
 */
@Slf4j
@Service
public class ResourceAllocationService {

    @Autowired
    private ResourceAllocationRepository allocationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * Allocate employee to project
     */
    @Transactional
    public AllocationResponse allocateEmployee(AllocationCreateRequest request) {
        log.info("Allocating employee {} to project {}", request.getEmployeeId(), request.getProjectId());

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", request.getEmployeeId()));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", request.getProjectId()));

        // Validation: Check if employee is still active
        if (!employee.getIsActive()) {
            throw new ValidationException("Cannot allocate inactive employee");
        }

        // Validation: Check allocation dates
        if (request.getEndDate() != null && request.getEndDate().isBefore(request.getStartDate())) {
            throw new ValidationException("End date cannot be before start date");
        }

        ResourceAllocation allocation = ResourceAllocation.builder()
                .employee(employee)
                .project(project)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .allocationPercentage(request.getAllocationPercentage())
                .billingRate(request.getBillingRate() != null ? request.getBillingRate() : employee.getBillingRate())
                .hoursPerDay(request.getHoursPerDay())
                .allocationStatus("ACTIVE")
                .build();

        ResourceAllocation savedAllocation = allocationRepository.save(allocation);
        log.info("Employee allocated successfully: {}", savedAllocation.getId());

        return mapToResponse(savedAllocation);
    }

    /**
     * Get allocation by ID
     */
    @Transactional(readOnly = true)
    public AllocationResponse getAllocationById(Long id) {
        ResourceAllocation allocation = allocationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ResourceAllocation", "id", id));
        return mapToResponse(allocation);
    }

    /**
     * Get all allocations for employee
     */
    @Transactional(readOnly = true)
    public List<AllocationResponse> getAllocationsByEmployee(Long employeeId) {
        return allocationRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all allocations for project
     */
    @Transactional(readOnly = true)
    public List<AllocationResponse> getAllocationsByProject(Long projectId) {
        return allocationRepository.findByProjectId(projectId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get active allocations for employee on a specific date
     */
    @Transactional(readOnly = true)
    public List<AllocationResponse> getActiveAllocationsForEmployeeOnDate(Long employeeId, LocalDate date) {
        return allocationRepository.findActiveAllocationsForEmployeeOnDate(employeeId, date)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update allocation
     */
    @Transactional
    public AllocationResponse updateAllocation(Long id, AllocationCreateRequest request) {
        log.info("Updating allocation: {}", id);

        ResourceAllocation allocation = allocationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ResourceAllocation", "id", id));

        allocation.setEndDate(request.getEndDate());
        allocation.setAllocationPercentage(request.getAllocationPercentage());
        allocation.setBillingRate(request.getBillingRate());

        ResourceAllocation updatedAllocation = allocationRepository.save(allocation);
        log.info("Allocation updated: {}", id);

        return mapToResponse(updatedAllocation);
    }

    /**
     * End allocation
     */
    @Transactional
    public AllocationResponse endAllocation(Long id, LocalDate endDate) {
        log.info("Ending allocation: {} with end date: {}", id, endDate);

        ResourceAllocation allocation = allocationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ResourceAllocation", "id", id));

        allocation.setEndDate(endDate);
        allocation.setAllocationStatus("INACTIVE");

        ResourceAllocation updatedAllocation = allocationRepository.save(allocation);
        log.info("Allocation ended: {}", id);

        return mapToResponse(updatedAllocation);
    }

    /**
     * Map to response DTO
     */
    private AllocationResponse mapToResponse(ResourceAllocation allocation) {
        return AllocationResponse.builder()
                .id(allocation.getId())
                .employeeId(allocation.getEmployee().getId())
                .employeeName(allocation.getEmployee().getFirstName() + " " + allocation.getEmployee().getLastName())
                .projectId(allocation.getProject().getId())
                .projectName(allocation.getProject().getProjectName())
                .startDate(allocation.getStartDate())
                .endDate(allocation.getEndDate())
                .allocationPercentage(allocation.getAllocationPercentage())
                .billingRate(allocation.getBillingRate())
                .hoursPerDay(allocation.getHoursPerDay())
                .allocationStatus(allocation.getAllocationStatus())
                .build();
    }
}

