package com.revcast.variance.controller;

import com.revcast.common.response.ApiResponse;
import com.revcast.variance.dto.VarianceResponse;
import com.revcast.variance.service.VarianceAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Variance Analysis Controller
 */
@Slf4j
@RestController
@RequestMapping("/variance")
@Tag(name = "Variance Analysis", description = "Variance analysis and reporting endpoints")
public class VarianceAnalysisController {

    @Autowired
    private VarianceAnalysisService varianceService;

    /**
     * Analyze variance
     */
    @PostMapping("/analyze/{forecastId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DELIVERY_HEAD', 'FINANCE')")
    @Operation(summary = "Analyze variance", description = "Analyze variance for a forecast")
    public ResponseEntity<ApiResponse<VarianceResponse>> analyzeVariance(@PathVariable Long forecastId) {
        log.info("Analyze variance for forecast: {}", forecastId);
        VarianceResponse response = varianceService.analyzeVariance(forecastId);
        return ResponseEntity.ok(ApiResponse.success(response, "Variance analysis completed"));
    }

    /**
     * Get variance analysis
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DELIVERY_HEAD', 'FINANCE')")
    @Operation(summary = "Get variance analysis", description = "Retrieve variance analysis details")
    public ResponseEntity<ApiResponse<VarianceResponse>> getVarianceAnalysis(@PathVariable Long id) {
        log.info("Get variance analysis: {}", id);
        VarianceResponse response = varianceService.getVarianceAnalysis(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get variance analyses for forecast
     */
    @GetMapping("/forecast/{forecastId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DELIVERY_HEAD', 'FINANCE')")
    @Operation(summary = "Get variance analyses for forecast", description = "Get all variance analyses for a forecast")
    public ResponseEntity<ApiResponse<List<VarianceResponse>>> getVarianceAnalysesForForecast(
            @PathVariable Long forecastId) {
        log.info("Get variance analyses for forecast: {}", forecastId);
        List<VarianceResponse> responses = varianceService.getVarianceAnalysesForForecast(forecastId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}

