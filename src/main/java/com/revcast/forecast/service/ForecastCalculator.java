package com.revcast.forecast.service;

import com.revcast.allocation.entity.ResourceAllocation;
import com.revcast.backfill.entity.STBOPosition;
import com.revcast.common.constants.AppConstants;
import com.revcast.common.util.CalendarUtil;
import com.revcast.forecast.entity.Forecast;
import com.revcast.forecast.entity.ForecastDetail;
import com.revcast.holiday.entity.Holiday;
import com.revcast.holiday.repository.HolidayRepository;
import com.revcast.leave.entity.LeaveRecord;
import com.revcast.leave.repository.LeaveRecordRepository;
import com.revcast.resignation.entity.Resignation;
import com.revcast.resignation.repository.ResignationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Component
public class ForecastCalculator {

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private LeaveRecordRepository leaveRecordRepository;

    @Autowired
    private ResignationRepository resignationRepository;

    public ForecastDetail calculateSBLRevenue(ResourceAllocation allocation, LocalDate forecastStartDate, LocalDate forecastEndDate) {
        log.debug("Calculating SBL revenue for allocation: {}", allocation.getId());

        // Determine the date range for this allocation within forecast period
        LocalDate allocationStart = allocation.getStartDate().isBefore(forecastStartDate)
                ? forecastStartDate : allocation.getStartDate();
        LocalDate allocationEnd = allocation.getEndDate() == null ? forecastEndDate :
                (allocation.getEndDate().isAfter(forecastEndDate) ? forecastEndDate : allocation.getEndDate());

        // Check if allocation is within forecast period
        if (allocationStart.isAfter(forecastEndDate) || allocationEnd.isBefore(forecastStartDate)) {
            return null; // Allocation not in forecast period
        }

        Resignation resignation = resignationRepository.findByEmployeeId(allocation.getEmployee().getId())
                .orElse(null);

        if (resignation != null && resignation.getLastWorkingDay().isBefore(allocationStart)) {
            return null;
        }

        if (resignation != null && resignation.getLastWorkingDay().isBefore(allocationEnd)) {
            allocationEnd = resignation.getLastWorkingDay();
        }

        // Get holidays for the project
        List<Holiday> holidays = holidayRepository.findHolidaysForProjectInDateRange(
                allocation.getProject().getId(),
                allocationStart,
                allocationEnd
        );

        // Get leaves for the employee
        List<LeaveRecord> leaves = leaveRecordRepository.findLeavesForEmployeeInDateRange(
                allocation.getEmployee().getId(),
                allocationStart,
                allocationEnd
        );

        // Calculate billable days
        Integer billableDays = CalendarUtil.calculateBillableDays(allocationStart, allocationEnd, holidays, leaves);

        // Apply allocation percentage
        BigDecimal adjustedBillableDays = BigDecimal.valueOf(billableDays)
                .multiply(allocation.getAllocationPercentage())
                .divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);

        // Calculate billable hours
        BigDecimal billableHours = CalendarUtil.calculateBillableHours(
                adjustedBillableDays.intValue(),
                allocation.getHoursPerDay().intValue()
        ).setScale(2, java.math.RoundingMode.HALF_UP);

        // Calculate revenue
        BigDecimal revenue = billableHours
                .multiply(allocation.getBillingRate())
                .setScale(2, java.math.RoundingMode.HALF_UP);

        return ForecastDetail.builder()
                .allocation(allocation)
                .employee(allocation.getEmployee())
                .project(allocation.getProject())
                .segment(allocation.getProject().getSegment())
                .account(allocation.getProject().getSegment().getAccount())
                .billableDays(adjustedBillableDays.intValue())
                .billableHours(billableHours)
                .billingRate(allocation.getBillingRate())
                .revenue(revenue)
                .revenueType(AppConstants.REVENUE_TYPE_SBL)
                .build();
    }

    /**
     * Calculate STBO Revenue
     */
    public ForecastDetail calculateSTBORevenue(STBOPosition stboPosition, LocalDate forecastStartDate, LocalDate forecastEndDate) {
        log.debug("Calculating STBO revenue for position: {}", stboPosition.getId());

        // STBO starts from start date or expected fill date
        LocalDate stboStart = stboPosition.getStartDate().isBefore(forecastStartDate)
                ? forecastStartDate : stboPosition.getStartDate();
        LocalDate stboEnd = forecastEndDate;

        // If position is filled, STBO ends when employee joins (should have converted to SBL)
        if (stboPosition.getFilledEmployee() != null) {
            return null; // Should use allocation instead
        }

        // Check if position status allows revenue recognition
        if (!AppConstants.STBO_STATUS_OPEN.equals(stboPosition.getStatus()) &&
            !AppConstants.STBO_STATUS_ON_HOLD.equals(stboPosition.getStatus())) {
            return null;
        }

        // Get holidays
        List<Holiday> holidays = holidayRepository.findHolidaysForProjectInDateRange(
                stboPosition.getProject().getId(),
                stboStart,
                stboEnd
        );

        // Calculate billable days (no leaves for unfilled position)
        Integer billableDays = CalendarUtil.calculateBillableDays(stboStart, stboEnd, holidays, new ArrayList<>());

        // Calculate billable hours
        BigDecimal billableHours = CalendarUtil.calculateBillableHours(billableDays, AppConstants.HOURS_PER_DAY)
                .setScale(2, java.math.RoundingMode.HALF_UP);

        // Calculate revenue
        BigDecimal revenue = billableHours
                .multiply(stboPosition.getBillingRate())
                .setScale(2, java.math.RoundingMode.HALF_UP);

        return ForecastDetail.builder()
                .stboPosition(stboPosition)
                .project(stboPosition.getProject())
                .segment(stboPosition.getProject().getSegment())
                .account(stboPosition.getProject().getSegment().getAccount())
                .billableDays(billableDays)
                .billableHours(billableHours)
                .billingRate(stboPosition.getBillingRate())
                .revenue(revenue)
                .revenueType(AppConstants.REVENUE_TYPE_STBO)
                .build();
    }

    /**
     * Aggregate forecast details
     */
    public Forecast aggregateForecastDetails(List<ForecastDetail> details, Forecast forecast) {
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal sblRevenue = BigDecimal.ZERO;
        BigDecimal stboRevenue = BigDecimal.ZERO;

        for (ForecastDetail detail : details) {
            if (detail.getRevenue() != null) {
                totalRevenue = totalRevenue.add(detail.getRevenue());

                if (AppConstants.REVENUE_TYPE_SBL.equals(detail.getRevenueType())) {
                    sblRevenue = sblRevenue.add(detail.getRevenue());
                } else if (AppConstants.REVENUE_TYPE_STBO.equals(detail.getRevenueType())) {
                    stboRevenue = stboRevenue.add(detail.getRevenue());
                }
            }
        }

        forecast.setTotalRevenue(totalRevenue);
        forecast.setSblRevenue(sblRevenue);
        forecast.setStboRevenue(stboRevenue);

        return forecast;
    }
}

