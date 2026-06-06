package com.revcast.account.repository;

import com.revcast.account.entity.Segment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Segment Repository
 */
@Repository
public interface SegmentRepository extends JpaRepository<Segment, Long> {

    Optional<Segment> findBySegmentCode(String segmentCode);

    List<Segment> findByAccountId(Long accountId);

    List<Segment> findByIsActiveTrue();

    Boolean existsBySegmentCode(String segmentCode);
}

