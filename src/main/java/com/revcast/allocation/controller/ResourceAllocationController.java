package com.revcast.allocation.controller;

import com.revcast.allocation.dto.AllocationCreateRequest;
import com.revcast.allocation.dto.AllocationResponse;
import com.revcast.allocation.service.ResourceAllocationService;
import com.revcast.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Resource Allocation Controller
 */
@Slf4j
@RestController
@RequestMapping("/allocations")
@Tag(name = "Resource Management", description = "Resource allocation endpoints")
public class ResourceAllocationController {

    @Autowired
    private ResourceAllocationService allocationService;

    /**
     * Allocate employee to project
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    @Operation(summary = "Allocate employee", description = "Allocate an employee to a project")
    public ResponseEntity<ApiResponse<AllocationResponse>> allocateEmployee(
            @Valid @RequestBody AllocationCreateRequest request) {
        log.info("Allocate employee to project");
        AllocationResponse response = allocationService.allocateEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Employee allocated successfully"));
    }

    /**
     * Get allocation by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'DELIVERY_HEAD')")
    @Operation(summary = "Get allocation by ID", description = "Retrieve allocation details")
    public ResponseEntity<ApiResponse<AllocationResponse>> getAllocationById(@PathVariable Long id) {
        log.info("Get allocation by ID: {}", id);
        AllocationResponse response = allocationService.getAllocationById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get allocations by employee
     */
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'DELIVERY_HEAD')")
    @Operation(summary = "Get allocations by employee", description = "Get all allocations for an employee")
    public ResponseEntity<ApiResponse<List<AllocationResponse>>> getAllocationsByEmployee(
            @PathVariable Long employeeId) {
        log.info("Get allocations for employee: {}", employeeId);
        List<AllocationResponse> responses = allocationService.getAllocationsByEmployee(employeeId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * Get allocations by project
     */
    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'DELIVERY_HEAD')")
    @Operation(summary = "Get allocations by project", description = "Get all allocations for a project")
    public ResponseEntity<ApiResponse<List<AllocationResponse>>> getAllocationsByProject(
            @PathVariable Long projectId) {
        log.info("Get allocations for project: {}", projectId);
        List<AllocationResponse> responses = allocationService.getAllocationsByProject(projectId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * Get active allocations for employee on a date
     */
    @GetMapping("/employee/{employeeId}/date/{date}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    @Operation(summary = "Get active allocations on date", description = "Get allocations active on a specific date")
    public ResponseEntity<ApiResponse<List<AllocationResponse>>> getActiveAllocationsOnDate(
            @PathVariable Long employeeId,
            @PathVariable String date) {
        log.info("Get active allocations for employee {} on date {}", employeeId, date);
        List<AllocationResponse> responses = allocationService.getActiveAllocationsForEmployeeOnDate(
                employeeId, LocalDate.parse(date));
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * Update allocation
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    @Operation(summary = "Update allocation", description = "Update allocation details")
    public ResponseEntity<ApiResponse<AllocationResponse>> updateAllocation(
            @PathVariable Long id,
            @Valid @RequestBody AllocationCreateRequest request) {
        log.info("Update allocation: {}", id);
        AllocationResponse response = allocationService.updateAllocation(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Allocation updated successfully"));
    }

    /**
     * End allocation
     */
    @PutMapping("/{id}/end")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    @Operation(summary = "End allocation", description = "End an allocation on a specific date")
    public ResponseEntity<ApiResponse<AllocationResponse>> endAllocation(
            @PathVariable Long id,
            @RequestParam String endDate) {
        log.info("End allocation: {}", id);
        AllocationResponse response = allocationService.endAllocation(id, LocalDate.parse(endDate));
        return ResponseEntity.ok(ApiResponse.success(response, "Allocation ended successfully"));
    }
}

