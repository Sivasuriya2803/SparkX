package com.revcast.backfill.repository;

import com.revcast.backfill.entity.STBOPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * STBO Position Repository
 */
@Repository
public interface STBOPositionRepository extends JpaRepository<STBOPosition, Long> {

    Optional<STBOPosition> findByPositionCode(String positionCode);

    List<STBOPosition> findByProjectId(Long projectId);

    List<STBOPosition> findByStatus(String status);

    List<STBOPosition> findByFilledEmployeeId(Long employeeId);

    Boolean existsByPositionCode(String positionCode);
}

