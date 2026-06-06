package com.revcast.forecast.controller;

import com.revcast.common.response.ApiResponse;
import com.revcast.forecast.dto.ForecastGenerateRequest;
import com.revcast.forecast.dto.ForecastResponse;
import com.revcast.forecast.service.ForecastService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Forecast Controller
 */
@Slf4j
@RestController
@RequestMapping("/forecasts")
@Tag(name = "Forecast Management", description = "Forecast generation and retrieval endpoints")
public class ForecastController {

    @Autowired
    private ForecastService forecastService;

    /**
     * Generate forecast
     */
    @PostMapping("/generate")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'DELIVERY_HEAD')")
    @Operation(summary = "Generate forecast", description = "Generate revenue forecast for a date range")
    public ResponseEntity<ApiResponse<ForecastResponse>> generateForecast(
            @Valid @RequestBody ForecastGenerateRequest request) {
        log.info("Generate forecast request");
        ForecastResponse response = forecastService.generateForecast(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Forecast generated successfully"));
    }

    /**
     * Get forecast by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'DELIVERY_HEAD', 'FINANCE')")
    @Operation(summary = "Get forecast by ID", description = "Retrieve forecast details")
    public ResponseEntity<ApiResponse<ForecastResponse>> getForecastById(@PathVariable Long id) {
        log.info("Get forecast by ID: {}", id);
        ForecastResponse response = forecastService.getForecastById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get forecasts by type
     */
    @GetMapping("/type/{forecastType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'DELIVERY_HEAD', 'FINANCE')")
    @Operation(summary = "Get forecasts by type", description = "Get all forecasts of a specific type")
    public ResponseEntity<ApiResponse<List<ForecastResponse>>> getForecastsByType(
            @PathVariable String forecastType) {
        log.info("Get forecasts by type: {}", forecastType);
        List<ForecastResponse> responses = forecastService.getForecastsByType(forecastType);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}

