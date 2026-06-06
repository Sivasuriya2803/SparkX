package com.revcast.forecast.repository;

import com.revcast.forecast.entity.Forecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Forecast Repository
 */
@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    Optional<Forecast> findByForecastCode(String forecastCode);

    List<Forecast> findByForecastType(String forecastType);

    List<Forecast> findByStatus(String status);

    @Query("SELECT f FROM Forecast f WHERE " +
            "f.startDate BETWEEN :startDate AND :endDate " +
            "ORDER BY f.startDate DESC")
    List<Forecast> findForecastsInDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT f FROM Forecast f WHERE " +
            "f.forecastType = :forecastType AND " +
            "f.status = :status " +
            "ORDER BY f.createdAt DESC LIMIT 1")
    Optional<Forecast> findLatestForecastByTypeAndStatus(
            @Param("forecastType") String forecastType,
            @Param("status") String status);

    List<Forecast> findByForecastPeriod(String forecastPeriod);
}

