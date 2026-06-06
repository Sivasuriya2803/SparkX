package com.revcast.variance.service;

import com.revcast.common.constants.AppConstants;
import com.revcast.forecast.entity.Forecast;
import com.revcast.forecast.entity.ForecastDetail;
import com.revcast.forecast.repository.ForecastDetailRepository;
import com.revcast.forecast.repository.ForecastRepository;
import com.revcast.variance.dto.VarianceReasonResponse;
import com.revcast.variance.dto.VarianceResponse;
import com.revcast.variance.entity.VarianceAnalysis;
import com.revcast.variance.entity.VarianceReason;
import com.revcast.variance.repository.VarianceAnalysisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Variance Analysis Service
 */
@Slf4j
@Service
public class VarianceAnalysisService {

    @Autowired
    private VarianceAnalysisRepository varianceRepository;

    @Autowired
    private ForecastRepository forecastRepository;

    @Autowired
    private ForecastDetailRepository detailRepository;

    /**
     * Analyze variance between current and previous forecast
     */
    @Transactional
    public VarianceResponse analyzeVariance(Long currentForecastId) {
        log.info("Analyzing variance for forecast: {}", currentForecastId);

        Forecast currentForecast = forecastRepository.findById(currentForecastId)
                .orElseThrow(() -> new RuntimeException("Forecast not found"));

        // Find previous forecast of same type
        Forecast previousForecast = forecastRepository.findLatestForecastByTypeAndStatus(
                currentForecast.getForecastType(),
                AppConstants.FORECAST_STATUS_PUBLISHED)
                .orElse(null);

        if (previousForecast == null) {
            log.warn("No previous forecast found for comparison");
            return createVarianceAnalysis(currentForecast, null, new ArrayList<>());
        }

        // Calculate variance
        BigDecimal varianceAmount = currentForecast.getTotalRevenue()
                .subtract(previousForecast.getTotalRevenue());

        BigDecimal variancePercentage = BigDecimal.ZERO;
        if (previousForecast.getTotalRevenue().compareTo(BigDecimal.ZERO) > 0) {
            variancePercentage = varianceAmount
                    .divide(previousForecast.getTotalRevenue(), 2, java.math.RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        // Identify reasons for variance
        List<VarianceReason> reasons = identifyVarianceReasons(currentForecast, previousForecast);

        // Create variance analysis record
        VarianceAnalysis analysis = VarianceAnalysis.builder()
                .currentForecast(currentForecast)
                .previousForecast(previousForecast)
                .varianceAmount(varianceAmount)
                .variancePercentage(variancePercentage)
                .analysisStatus(AppConstants.FORECAST_STATUS_FINALIZED)
                .reasonsList(reasons)
                .build();

        VarianceAnalysis savedAnalysis = varianceRepository.save(analysis);

        if (reasons != null && !reasons.isEmpty()) {
            for (VarianceReason reason : reasons) {
                reason.setVarianceAnalysis(savedAnalysis);
            }
        }

        log.info("Variance analysis completed. Variance Amount: {}, Percentage: {}",
                varianceAmount, variancePercentage);

        return mapToResponse(savedAnalysis);
    }

    /**
     * Identify reasons for variance
     */
    private List<VarianceReason> identifyVarianceReasons(Forecast currentForecast, Forecast previousForecast) {
        List<VarianceReason> reasons = new ArrayList<>();

        List<ForecastDetail> currentDetails = detailRepository.findByForecastId(currentForecast.getId());
        List<ForecastDetail> previousDetails = detailRepository.findByForecastId(previousForecast.getId());

        // Reason 1: Changes in SBL revenue
        BigDecimal sblDifference = currentForecast.getSblRevenue()
                .subtract(previousForecast.getSblRevenue());

        if (sblDifference.compareTo(BigDecimal.ZERO) != 0) {
            VarianceReason reason = VarianceReason.builder()
                    .reasonType("SBL Changes")
                    .impactAmount(sblDifference)
                    .description("Change in allocated resources or billing rates")
                    .build();
            reasons.add(reason);
        }

        // Reason 2: Changes in STBO revenue
        BigDecimal stboDifference = currentForecast.getStboRevenue()
                .subtract(previousForecast.getStboRevenue());

        if (stboDifference.compareTo(BigDecimal.ZERO) != 0) {
            VarianceReason reason = VarianceReason.builder()
                    .reasonType("STBO Changes")
                    .impactAmount(stboDifference)
                    .description("Change in unfilled positions")
                    .build();
            reasons.add(reason);
        }

        return reasons;
    }

    /**
     * Get variance analysis
     */
    @Transactional(readOnly = true)
    public VarianceResponse getVarianceAnalysis(Long id) {
        VarianceAnalysis analysis = varianceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Variance analysis not found"));
        return mapToResponse(analysis);
    }

    /**
     * Get variance analyses for forecast
     */
    @Transactional(readOnly = true)
    public List<VarianceResponse> getVarianceAnalysesForForecast(Long forecastId) {
        return varianceRepository.findByCurrentForecastId(forecastId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Create variance analysis response
     */
    private VarianceResponse createVarianceAnalysis(Forecast currentForecast, Forecast previousForecast,
                                                     List<VarianceReason> reasons) {
        VarianceAnalysis analysis = VarianceAnalysis.builder()
                .currentForecast(currentForecast)
                .previousForecast(previousForecast)
                .varianceAmount(previousForecast != null ?
                        currentForecast.getTotalRevenue().subtract(previousForecast.getTotalRevenue()) :
                        currentForecast.getTotalRevenue())
                .variancePercentage(BigDecimal.ZERO)
                .analysisStatus(AppConstants.FORECAST_STATUS_DRAFT)
                .reasonsList(reasons)
                .build();

        return mapToResponse(analysis);
    }

    /**
     * Map to response DTO
     */
    private VarianceResponse mapToResponse(VarianceAnalysis analysis) {
        List<VarianceReasonResponse> reasonResponses = new ArrayList<>();

        if (analysis.getReasonsList() != null && !analysis.getReasonsList().isEmpty()) {
            reasonResponses = analysis.getReasonsList().stream()
                    .map(r -> VarianceReasonResponse.builder()
                            .reason(r.getReasonType())
                            .impact(r.getImpactAmount())
                            .description(r.getDescription())
                            .build())
                    .collect(Collectors.toList());
        }

        return VarianceResponse.builder()
                .id(analysis.getId())
                .currentForecastId(analysis.getCurrentForecast().getId())
                .previousForecastId(analysis.getPreviousForecast() != null ?
                        analysis.getPreviousForecast().getId() : null)
                .varianceAmount(analysis.getVarianceAmount())
                .variancePercentage(analysis.getVariancePercentage())
                .analysisStatus(analysis.getAnalysisStatus())
                .reasons(reasonResponses)
                .build();
    }
}

