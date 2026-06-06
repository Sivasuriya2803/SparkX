package com.revcast.holiday.repository;

import com.revcast.holiday.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Holiday Repository
 */
@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    List<Holiday> findByIsCompanyWideTrue();

    List<Holiday> findByProjectId(Long projectId);

    List<Holiday> findByHolidayDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT h FROM Holiday h WHERE " +
            "(h.isCompanyWide = true OR h.project.id = :projectId) AND " +
            "h.holidayDate BETWEEN :startDate AND :endDate")
    List<Holiday> findHolidaysForProjectInDateRange(
            @Param("projectId") Long projectId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    Boolean existsByHolidayDateAndProjectId(LocalDate holidayDate, Long projectId);
}

