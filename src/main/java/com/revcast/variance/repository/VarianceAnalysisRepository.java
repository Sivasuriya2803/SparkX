package com.revcast.variance.repository;

import com.revcast.variance.entity.VarianceAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Variance Analysis Repository
 */
@Repository
public interface VarianceAnalysisRepository extends JpaRepository<VarianceAnalysis, Long> {

    List<VarianceAnalysis> findByCurrentForecastId(Long currentForecastId);

    List<VarianceAnalysis> findByAnalysisStatus(String status);
}

