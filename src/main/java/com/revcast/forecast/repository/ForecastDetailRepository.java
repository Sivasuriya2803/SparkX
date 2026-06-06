package com.revcast.forecast.repository;

import com.revcast.forecast.entity.ForecastDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Forecast Detail Repository
 */
@Repository
public interface ForecastDetailRepository extends JpaRepository<ForecastDetail, Long> {

    List<ForecastDetail> findByForecastId(Long forecastId);

    List<ForecastDetail> findByEmployeeId(Long employeeId);

    List<ForecastDetail> findByProjectId(Long projectId);

    List<ForecastDetail> findBySegmentId(Long segmentId);

    List<ForecastDetail> findByAccountId(Long accountId);

    List<ForecastDetail> findByRevenueType(String revenueType);
}

