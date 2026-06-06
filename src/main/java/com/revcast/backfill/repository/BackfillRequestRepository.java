package com.revcast.backfill.repository;

import com.revcast.backfill.entity.BackfillRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Backfill Request Repository
 */
@Repository
public interface BackfillRequestRepository extends JpaRepository<BackfillRequest, Long> {

    Optional<BackfillRequest> findByResignationId(Long resignationId);

    List<BackfillRequest> findByStatus(String status);

    List<BackfillRequest> findByBackfillEmployeeId(Long employeeId);

    List<BackfillRequest> findByStboPositionId(Long stboPositionId);

    List<BackfillRequest> findByPriority(String priority);
}

