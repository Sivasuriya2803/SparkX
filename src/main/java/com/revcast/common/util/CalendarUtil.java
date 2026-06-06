package com.revcast.common.util;

import com.revcast.holiday.entity.Holiday;
import com.revcast.leave.entity.LeaveRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Utility class for calendar and billable days calculations
 */
@Slf4j
@Component
public class CalendarUtil {

    private static final int HOURS_PER_DAY = 8;

    /**
     * Calculate billable days between start and end date
     * Excludes weekends, company holidays, and employee leaves
     */
    public static Integer calculateBillableDays(LocalDate startDate, LocalDate endDate,
                                                 List<Holiday> companyHolidays,
                                                 List<LeaveRecord> employeeLeaves) {
        int billableDays = 0;
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            if (isWorkingDay(currentDate, companyHolidays, employeeLeaves)) {
                billableDays++;
            }
            currentDate = currentDate.plusDays(1);
        }

        return billableDays;
    }

    /**
     * Check if a date is a working day
     */
    public static boolean isWorkingDay(LocalDate date, List<Holiday> companyHolidays,
                                       List<LeaveRecord> employeeLeaves) {
        // Check if weekend
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return false;
        }

        // Check if company holiday
        if (isCompanyHoliday(date, companyHolidays)) {
            return false;
        }

        // Check if employee leave
        if (isEmployeeLeave(date, employeeLeaves)) {
            return false;
        }

        return true;
    }

    /**
     * Check if date is a company holiday
     */
    public static boolean isCompanyHoliday(LocalDate date, List<Holiday> companyHolidays) {
        if (companyHolidays == null || companyHolidays.isEmpty()) {
            return false;
        }
        return companyHolidays.stream()
                .anyMatch(holiday -> holiday.getHolidayDate().equals(date));
    }

    /**
     * Check if employee is on leave
     */
    public static boolean isEmployeeLeave(LocalDate date, List<LeaveRecord> employeeLeaves) {
        if (employeeLeaves == null || employeeLeaves.isEmpty()) {
            return false;
        }
        return employeeLeaves.stream()
                .anyMatch(leave -> !date.isBefore(leave.getStartDate()) && !date.isAfter(leave.getEndDate()));
    }

    /**
     * Calculate billable hours
     */
    public static BigDecimal calculateBillableHours(Integer billableDays, Integer hoursPerDay) {
        if (billableDays == null || billableDays < 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf (billableDays * hoursPerDay);
    }

    /**
     * Get working days in a month (excluding weekends and holidays)
     */
    public static Integer getWorkingDaysInMonth(int year, int month, List<Holiday> holidays) {
        int workingDays = 0;
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1);

        LocalDate currentDate = firstDayOfMonth;
        while (!currentDate.isAfter(lastDayOfMonth)) {
            if (isWorkingDay(currentDate, holidays, null)) {
                workingDays++;
            }
            currentDate = currentDate.plusDays(1);
        }

        return workingDays;
    }

    /**
     * Check if date falls within allocation period
     */
    public static boolean isDateInAllocationPeriod(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return !date.isBefore(startDate) && (endDate == null || !date.isAfter(endDate));
    }

    /**
     * Count days between two dates (inclusive)
     */
    public static long countDaysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
}

