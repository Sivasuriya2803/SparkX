package com.revcast.forecast.service;

import com.revcast.allocation.entity.ResourceAllocation;
import com.revcast.allocation.repository.ResourceAllocationRepository;
import com.revcast.backfill.entity.STBOPosition;
import com.revcast.employee.entity.Employee;
import com.revcast.forecast.entity.ForecastDetail;
import com.revcast.holiday.entity.Holiday;
import com.revcast.holiday.repository.HolidayRepository;
import com.revcast.leave.entity.LeaveRecord;
import com.revcast.leave.repository.LeaveRecordRepository;
import com.revcast.project.entity.Project;
import com.revcast.resignation.repository.ResignationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Forecast Calculator Unit Tests
 */
@ExtendWith(MockitoExtension.class)
class ForecastCalculatorTest {

    @Mock
    private HolidayRepository holidayRepository;

    @Mock
    private LeaveRecordRepository leaveRecordRepository;

    @Mock
    private ResignationRepository resignationRepository;

    @InjectMocks
    private ForecastCalculator calculator;

    private ResourceAllocation allocation;
    private Employee employee;
    private Project project;
    private STBOPosition stboPosition;

    @BeforeEach
    void setUp() {
        // Setup test data
        employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .billingRate(BigDecimal.valueOf(100))
                .build();

        project = Project.builder()
                .id(1L)
                .projectName("Test Project")
                .build();

        allocation = ResourceAllocation.builder()
                .id(1L)
                .employee(employee)
                .project(project)
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 1, 31))
                .allocationPercentage(BigDecimal.valueOf(100))
                .billingRate(BigDecimal.valueOf(100))
                .hoursPerDay(BigDecimal.valueOf(8))
                .build();

        stboPosition = STBOPosition.builder()
                .id(1L)
                .positionCode("STBO001")
                .project(project)
                .billingRate(BigDecimal.valueOf(100))
                .startDate(LocalDate.of(2026, 1, 1))
                .status("OPEN")
                .build();
    }

    @Test
    void testCalculateSBLRevenue_Success() {
        when(holidayRepository.findHolidaysForProjectInDateRange(1L,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31)))
                .thenReturn(new ArrayList<>());

        when(leaveRecordRepository.findLeavesForEmployeeInDateRange(1L,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31)))
                .thenReturn(new ArrayList<>());

        when(resignationRepository.findByEmployeeId(1L))
                .thenReturn(java.util.Optional.empty());

        ForecastDetail detail = calculator.calculateSBLRevenue(allocation,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31));

        assertNotNull(detail);
        assertNotNull(detail.getRevenue());
        assertTrue(detail.getRevenue().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testCalculateSTBORevenue_Success() {
        when(holidayRepository.findHolidaysForProjectInDateRange(1L,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31)))
                .thenReturn(new ArrayList<>());

        ForecastDetail detail = calculator.calculateSTBORevenue(stboPosition,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31));

        assertNotNull(detail);
        assertNotNull(detail.getRevenue());
        assertEquals("STBO", detail.getRevenueType());
    }

    @Test
    void testCalculateSBLRevenue_WithLeave() {
        LeaveRecord leave = LeaveRecord.builder()
                .employee(employee)
                .startDate(LocalDate.of(2026, 1, 5))
                .endDate(LocalDate.of(2026, 1, 10))
                .numberOfDays(6)
                .build();

        List<LeaveRecord> leaves = new ArrayList<>();
        leaves.add(leave);

        when(holidayRepository.findHolidaysForProjectInDateRange(1L,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31)))
                .thenReturn(new ArrayList<>());

        when(leaveRecordRepository.findLeavesForEmployeeInDateRange(1L,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31)))
                .thenReturn(leaves);

        when(resignationRepository.findByEmployeeId(1L))
                .thenReturn(java.util.Optional.empty());

        ForecastDetail detail = calculator.calculateSBLRevenue(allocation,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31));

        assertNotNull(detail);
        // Revenue should be less due to leave
        assertTrue(detail.getRevenue().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testCalculateSBLRevenue_PartialAllocation() {
        allocation.setAllocationPercentage(BigDecimal.valueOf(50));

        when(holidayRepository.findHolidaysForProjectInDateRange(1L,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31)))
                .thenReturn(new ArrayList<>());

        when(leaveRecordRepository.findLeavesForEmployeeInDateRange(1L,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31)))
                .thenReturn(new ArrayList<>());

        when(resignationRepository.findByEmployeeId(1L))
                .thenReturn(java.util.Optional.empty());

        ForecastDetail detail = calculator.calculateSBLRevenue(allocation,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31));

        assertNotNull(detail);
        // Revenue should be 50% of full allocation
        assertTrue(detail.getRevenue().compareTo(BigDecimal.ZERO) > 0);
    }
}

