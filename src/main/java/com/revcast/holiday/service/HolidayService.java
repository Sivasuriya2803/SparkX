package com.revcast.holiday.service;

import com.revcast.common.exception.ResourceNotFoundException;
import com.revcast.holiday.entity.Holiday;
import com.revcast.holiday.repository.HolidayRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Holiday Service
 */
@Slf4j
@Service
public class HolidayService {

    @Autowired
    private HolidayRepository holidayRepository;

    /**
     * Create holiday
     */
    @Transactional
    public Holiday createHoliday(Holiday holiday) {
        log.info("Creating holiday: {} on date: {}", holiday.getHolidayName(), holiday.getHolidayDate());
        return holidayRepository.save(holiday);
    }

    /**
     * Get company-wide holidays
     */
    @Transactional(readOnly = true)
    public List<Holiday> getCompanyHolidays() {
        return holidayRepository.findByIsCompanyWideTrue();
    }

    /**
     * Get holidays in date range
     */
    @Transactional(readOnly = true)
    public List<Holiday> getHolidaysInDateRange(LocalDate startDate, LocalDate endDate) {
        return holidayRepository.findByHolidayDateBetween(startDate, endDate);
    }

    /**
     * Get holidays for project
     */
    @Transactional(readOnly = true)
    public List<Holiday> getHolidaysForProject(Long projectId, LocalDate startDate, LocalDate endDate) {
        return holidayRepository.findHolidaysForProjectInDateRange(projectId, startDate, endDate);
    }

    /**
     * Delete holiday
     */
    @Transactional
    public void deleteHoliday(Long id) {
        log.info("Deleting holiday: {}", id);
        holidayRepository.deleteById(id);
    }
}

