package com.revcast.employee.controller;

import com.revcast.common.response.ApiResponse;
import com.revcast.employee.dto.EmployeeCreateRequest;
import com.revcast.employee.dto.EmployeeResponse;
import com.revcast.employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Employee Controller
 */
@Slf4j
@RestController
@RequestMapping("/employees")
@Tag(name = "Employee Management", description = "Employee management endpoints")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Create new employee
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Operation(summary = "Create employee", description = "Create a new employee record")
    public ResponseEntity<ApiResponse<EmployeeResponse>> createEmployee(
            @Valid @RequestBody EmployeeCreateRequest request) {
        log.info("Create employee request");
        EmployeeResponse response = employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Employee created successfully"));
    }

    /**
     * Get employee by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'DELIVERY_HEAD')")
    @Operation(summary = "Get employee by ID", description = "Retrieve employee details by ID")
    public ResponseEntity<ApiResponse<EmployeeResponse>> getEmployeeById(@PathVariable Long id) {
        log.info("Get employee by ID: {}", id);
        EmployeeResponse response = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get employee by code
     */
    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    @Operation(summary = "Get employee by code", description = "Retrieve employee by employee code")
    public ResponseEntity<ApiResponse<EmployeeResponse>> getEmployeeByCode(@PathVariable String code) {
        log.info("Get employee by code: {}", code);
        EmployeeResponse response = employeeService.getEmployeeByCode(code);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get all active employees
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'DELIVERY_HEAD')")
    @Operation(summary = "Get all active employees", description = "Retrieve all active employees")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getAllActiveEmployees() {
        log.info("Get all active employees");
        List<EmployeeResponse> responses = employeeService.getAllActiveEmployees();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * Update employee
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Update employee", description = "Update employee details")
    public ResponseEntity<ApiResponse<EmployeeResponse>> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeCreateRequest request) {
        log.info("Update employee: {}", id);
        EmployeeResponse response = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Employee updated successfully"));
    }

    /**
     * Deactivate employee
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Deactivate employee", description = "Deactivate employee account")
    public ResponseEntity<ApiResponse<EmployeeResponse>> deactivateEmployee(@PathVariable Long id) {
        log.info("Deactivate employee: {}", id);
        EmployeeResponse response = employeeService.deactivateEmployee(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Employee deactivated successfully"));
    }
}

