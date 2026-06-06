package com.revcast.forecast.service;

import com.revcast.allocation.repository.ResourceAllocationRepository;
import com.revcast.backfill.repository.STBOPositionRepository;
import com.revcast.common.constants.AppConstants;
import com.revcast.common.exception.ForecastException;
import com.revcast.forecast.dto.ForecastDetailResponse;
import com.revcast.forecast.dto.ForecastGenerateRequest;
import com.revcast.forecast.dto.ForecastResponse;
import com.revcast.forecast.entity.Forecast;
import com.revcast.forecast.entity.ForecastDetail;
import com.revcast.forecast.repository.ForecastDetailRepository;
import com.revcast.forecast.repository.ForecastRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Forecast Service - Orchestrates forecast generation
 */
@Slf4j
@Service
public class ForecastService {

    @Autowired
    private ForecastRepository forecastRepository;

    @Autowired
    private ForecastDetailRepository detailRepository;

    @Autowired
    private ResourceAllocationRepository allocationRepository;

    @Autowired
    private STBOPositionRepository stboPositionRepository;

    @Autowired
    private ForecastCalculator calculator;

    /**
     * Generate forecast
     */
    @Transactional
    public ForecastResponse generateForecast(ForecastGenerateRequest request) {
        log.info("Generating {} forecast for period: {} to {}",
                request.getForecastType(), request.getStartDate(), request.getEndDate());

        // Validate request
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new ForecastException("End date cannot be before start date");
        }

        // Create forecast entity
        String forecastCode = generateForecastCode(request.getForecastType());
        String forecastPeriod = generateForecastPeriod(request.getForecastType(), request.getStartDate());

        Forecast forecast = Forecast.builder()
                .forecastCode(forecastCode)
                .forecastType(request.getForecastType())
                .forecastPeriod(forecastPeriod)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(AppConstants.FORECAST_STATUS_DRAFT)
                .build();

        forecast = forecastRepository.save(forecast);
        log.info("Forecast created with code: {}", forecastCode);

        // Calculate forecast details
        List<ForecastDetail> details = calculateForecastDetails(forecast, request);

        // Save details
        for (ForecastDetail detail : details) {
            detail.setForecast(forecast);
            detailRepository.save(detail);
        }

        // Aggregate totals
        forecast = calculator.aggregateForecastDetails(details, forecast);
        forecast.setStatus(AppConstants.FORECAST_STATUS_FINALIZED);
        forecast = forecastRepository.save(forecast);

        log.info("Forecast finalized: {} with total revenue: {}", forecastCode, forecast.getTotalRevenue());

        return mapToResponse(forecast, details);
    }

    /**
     * Calculate all forecast details
     */
    private List<ForecastDetail> calculateForecastDetails(Forecast forecast, ForecastGenerateRequest request) {
        List<ForecastDetail> details = new ArrayList<>();

        // Get allocations in date range
        var allocations = allocationRepository.findAllocationsInDateRange(
                forecast.getStartDate(),
                forecast.getEndDate()
        );

        for (var allocation : allocations) {
            // Apply filters if specified
            if (request.getAccountId() != null &&
                !allocation.getProject().getSegment().getAccount().getId().equals(request.getAccountId())) {
                continue;
            }
            if (request.getSegmentId() != null &&
                !allocation.getProject().getSegment().getId().equals(request.getSegmentId())) {
                continue;
            }
            if (request.getProjectId() != null &&
                !allocation.getProject().getId().equals(request.getProjectId())) {
                continue;
            }

            ForecastDetail detail = calculator.calculateSBLRevenue(
                    allocation,
                    forecast.getStartDate(),
                    forecast.getEndDate()
            );

            if (detail != null) {
                details.add(detail);
            }
        }

        // Get STBO positions in date range
        var stboPositions = stboPositionRepository.findAll()
                .stream()
                .filter(p -> !p.getStartDate().isAfter(forecast.getEndDate()))
                .collect(Collectors.toList());

        for (var stboPosition : stboPositions) {
            // Apply filters if specified
            if (request.getAccountId() != null &&
                !stboPosition.getProject().getSegment().getAccount().getId().equals(request.getAccountId())) {
                continue;
            }
            if (request.getSegmentId() != null &&
                !stboPosition.getProject().getSegment().getId().equals(request.getSegmentId())) {
                continue;
            }
            if (request.getProjectId() != null &&
                !stboPosition.getProject().getId().equals(request.getProjectId())) {
                continue;
            }

            ForecastDetail detail = calculator.calculateSTBORevenue(
                    stboPosition,
                    forecast.getStartDate(),
                    forecast.getEndDate()
            );

            if (detail != null) {
                details.add(detail);
            }
        }

        log.debug("Calculated {} forecast details", details.size());
        return details;
    }

    /**
     * Get forecast by ID
     */
    @Transactional(readOnly = true)
    public ForecastResponse getForecastById(Long id) {
        Forecast forecast = forecastRepository.findById(id)
                .orElseThrow(() -> new ForecastException("Forecast not found with id: " + id));

        List<ForecastDetail> details = detailRepository.findByForecastId(id);
        return mapToResponse(forecast, details);
    }

    /**
     * Get forecasts by type
     */
    @Transactional(readOnly = true)
    public List<ForecastResponse> getForecastsByType(String forecastType) {
        return forecastRepository.findByForecastType(forecastType)
                .stream()
                .map(f -> mapToResponse(f, detailRepository.findByForecastId(f.getId())))
                .collect(Collectors.toList());
    }

    /**
     * Generate forecast code
     */
    private String generateForecastCode(String forecastType) {
        return forecastType.substring(0, 1) + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Generate forecast period string
     */
    private String generateForecastPeriod(String forecastType, LocalDate date) {
        switch (forecastType) {
            case AppConstants.FORECAST_TYPE_WEEKLY:
                WeekFields wf = WeekFields.of(Locale.getDefault());
                int week = date.get(wf.weekOfWeekBasedYear());
                int year = date.get(wf.weekBasedYear());
                return year + "-W" + String.format("%02d", week);
            case AppConstants.FORECAST_TYPE_MONTHLY:
                return date.getYear() + "-" + String.format("%02d", date.getMonthValue());
            case AppConstants.FORECAST_TYPE_QUARTERLY:
                int quarter = (date.getMonthValue() - 1) / 3 + 1;
                return date.getYear() + "-Q" + quarter;
            default:
                return date.toString();
        }
    }

    /**
     * Map to response DTO
     */
    private ForecastResponse mapToResponse(Forecast forecast, List<ForecastDetail> details) {
        List<ForecastDetailResponse> detailResponses = details.stream()
                .map(d -> ForecastDetailResponse.builder()
                        .id(d.getId())
                        .employeeName(d.getEmployee() != null ?
                                d.getEmployee().getFirstName() + " " + d.getEmployee().getLastName() : "STBO Position")
                        .projectName(d.getProject().getProjectName())
                        .segmentName(d.getSegment().getSegmentName())
                        .accountName(d.getAccount().getAccountName())
                        .billableDays(d.getBillableDays())
                        .billableHours(d.getBillableHours())
                        .billingRate(d.getBillingRate())
                        .revenue(d.getRevenue())
                        .revenueType(d.getRevenueType())
                        .build())
                .collect(Collectors.toList());

        return ForecastResponse.builder()
                .id(forecast.getId())
                .forecastCode(forecast.getForecastCode())
                .forecastType(forecast.getForecastType())
                .forecastPeriod(forecast.getForecastPeriod())
                .startDate(forecast.getStartDate())
                .endDate(forecast.getEndDate())
                .totalRevenue(forecast.getTotalRevenue())
                .sblRevenue(forecast.getSblRevenue())
                .stboRevenue(forecast.getStboRevenue())
                .status(forecast.getStatus())
                .details(detailResponses)
                .build();
    }
}

