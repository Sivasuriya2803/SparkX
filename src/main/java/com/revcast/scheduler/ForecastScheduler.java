package com.revcast.scheduler;

import com.revcast.common.constants.AppConstants;
import com.revcast.forecast.dto.ForecastGenerateRequest;
import com.revcast.forecast.service.ForecastService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * Forecast Scheduler - Scheduled jobs for forecast generation
 */
@Slf4j
@Component
public class ForecastScheduler {

    @Autowired
    private ForecastService forecastService;

    /**
     * Generate weekly forecast - Every Monday at 02:00 AM
     */
    @Scheduled(cron = "0 * 2  * * MON", zone = "UTC")
    public void generateWeeklyForecast() {
        log.info("Starting scheduled weekly forecast generation");
        try {
            LocalDate startDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate endDate = startDate.plus(6, ChronoUnit.DAYS);

            ForecastGenerateRequest request = ForecastGenerateRequest.builder()
                    .forecastType(AppConstants.FORECAST_TYPE_WEEKLY)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();

            forecastService.generateForecast(request);
            log.info("Weekly forecast generated successfully");
        } catch (Exception e) {
            log.error("Error generating weekly forecast", e);
        }
    }

    /**
     * Generate monthly forecast - 1st of each month at 02:00 AM
     */
    @Scheduled(cron = "0 2 1 * * ?", zone = "UTC")
    public void generateMonthlyForecast() {
        log.info("Starting scheduled monthly forecast generation");
        try {
            LocalDate today = LocalDate.now();
            LocalDate startDate = today.withDayOfMonth(1);
            LocalDate endDate = today.withDayOfMonth(today.lengthOfMonth());

            ForecastGenerateRequest request = ForecastGenerateRequest.builder()
                    .forecastType(AppConstants.FORECAST_TYPE_MONTHLY)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();

            forecastService.generateForecast(request);
            log.info("Monthly forecast generated successfully");
        } catch (Exception e) {
            log.error("Error generating monthly forecast", e);
        }
    }

    /**
     * Generate quarterly forecast - 1st of each quarter at 02:00 AM
     */
    @Scheduled(cron = "0 2 1 1,4,7,10 * ?", zone = "UTC")
    public void generateQuarterlyForecast() {
        log.info("Starting scheduled quarterly forecast generation");
        try {
            LocalDate today = LocalDate.now();
            int quarter = (today.getMonthValue() - 1) / 3;
            LocalDate startDate = today.withMonth(quarter * 3 + 1).withDayOfMonth(1);
            LocalDate endDate = startDate.plus(2, ChronoUnit.MONTHS)
                    .withDayOfMonth(startDate.withMonth(quarter * 3 + 3).lengthOfMonth());

            ForecastGenerateRequest request = ForecastGenerateRequest.builder()
                    .forecastType(AppConstants.FORECAST_TYPE_QUARTERLY)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();

            forecastService.generateForecast(request);
            log.info("Quarterly forecast generated successfully");
        } catch (Exception e) {
            log.error("Error generating quarterly forecast", e);
        }
    }

    /**
     * Daily refresh of forecasts - Every day at 03:00 AM
     */
    @Scheduled(cron = "0 3 * * * ?", zone = "UTC")
    public void dailyForecastRefresh() {
        log.info("Starting daily forecast refresh");
        try {
            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);

            ForecastGenerateRequest request = ForecastGenerateRequest.builder()
                    .forecastType(AppConstants.FORECAST_TYPE_WEEKLY)
                    .startDate(today)
                    .endDate(tomorrow)
                    .build();

            forecastService.generateForecast(request);
            log.info("Daily forecast refresh completed successfully");
        } catch (Exception e) {
            log.error("Error during daily forecast refresh", e);
        }
    }
}

