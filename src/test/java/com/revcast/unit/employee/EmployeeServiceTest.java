package com.revcast.employee.service;

import com.revcast.common.exception.ResourceNotFoundException;
import com.revcast.common.exception.ValidationException;
import com.revcast.employee.dto.EmployeeCreateRequest;
import com.revcast.employee.dto.EmployeeResponse;
import com.revcast.employee.entity.Employee;
import com.revcast.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Employee Service Unit Tests
 */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeCreateRequest createRequest;
    private Employee employee;

    @BeforeEach
    void setUp() {
        createRequest = EmployeeCreateRequest.builder()
                .employeeCode("EMP001")
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .dateOfJoining(LocalDate.now())
                .billingRate(BigDecimal.valueOf(100))
                .build();

        employee = Employee.builder()
                .id(1L)
                .employeeCode("EMP001")
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .dateOfJoining(LocalDate.now())
                .billingRate(BigDecimal.valueOf(100))
                .isActive(true)
                .build();
    }

    @Test
    void testCreateEmployee_Success() {
        when(employeeRepository.existsByEmployeeCode(anyString())).thenReturn(false);
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(employeeRepository.save(any())).thenReturn(employee);

        EmployeeResponse response = employeeService.createEmployee(createRequest);

        assertNotNull(response);
        assertEquals("EMP001", response.getEmployeeCode());
        assertEquals("John", response.getFirstName());
        verify(employeeRepository, times(1)).save(any());
    }

    @Test
    void testCreateEmployee_DuplicateCode() {
        when(employeeRepository.existsByEmployeeCode(anyString())).thenReturn(true);

        assertThrows(ValidationException.class, () -> employeeService.createEmployee(createRequest));
    }

    @Test
    void testCreateEmployee_DuplicateEmail() {
        when(employeeRepository.existsByEmployeeCode(anyString())).thenReturn(false);
        when(employeeRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(ValidationException.class, () -> employeeService.createEmployee(createRequest));
    }

    @Test
    void testGetEmployeeById_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeResponse response = employeeService.getEmployeeById(1L);

        assertNotNull(response);
        assertEquals("EMP001", response.getEmployeeCode());
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById(1L));
    }

    @Test
    void testGetEmployeeByCode_Success() {
        when(employeeRepository.findByEmployeeCode("EMP001")).thenReturn(Optional.of(employee));

        EmployeeResponse response = employeeService.getEmployeeByCode("EMP001");

        assertNotNull(response);
        assertEquals("EMP001", response.getEmployeeCode());
    }

    @Test
    void testUpdateEmployee_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any())).thenReturn(employee);

        EmployeeResponse response = employeeService.updateEmployee(1L, createRequest);

        assertNotNull(response);
        verify(employeeRepository, times(1)).save(any());
    }

    @Test
    void testDeactivateEmployee_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any())).thenReturn(employee);

        EmployeeResponse response = employeeService.deactivateEmployee(1L);

        assertNotNull(response);
        verify(employeeRepository, times(1)).save(any());
    }
}

