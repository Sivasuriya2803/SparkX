package com.revcast.account.service;

import com.revcast.account.entity.Account;
import com.revcast.account.entity.Segment;
import com.revcast.account.repository.AccountRepository;
import com.revcast.account.repository.SegmentRepository;
import com.revcast.common.exception.ResourceNotFoundException;
import com.revcast.common.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Segment Service
 */
@Slf4j
@Service
public class SegmentService {

    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Create segment
     */
    @Transactional
    public Segment createSegment(Segment segment) {
        log.info("Creating segment: {}", segment.getSegmentName());

        Account account = accountRepository.findById(segment.getAccount().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", segment.getAccount().getId()));

        if (segmentRepository.existsBySegmentCode(segment.getSegmentCode())) {
            throw new ValidationException("Segment with code " + segment.getSegmentCode() + " already exists");
        }

        segment.setAccount(account);
        segment.setIsActive(true);
        return segmentRepository.save(segment);
    }

    /**
     * Get segment by ID
     */
    @Transactional(readOnly = true)
    public Segment getSegmentById(Long id) {
        return segmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Segment", "id", id));
    }

    /**
     * Get segments by account
     */
    @Transactional(readOnly = true)
    public List<Segment> getSegmentsByAccount(Long accountId) {
        return segmentRepository.findByAccountId(accountId);
    }

    /**
     * Get all active segments
     */
    @Transactional(readOnly = true)
    public List<Segment> getAllActiveSegments() {
        return segmentRepository.findByIsActiveTrue();
    }

    /**
     * Update segment
     */
    @Transactional
    public Segment updateSegment(Long id, Segment segmentUpdate) {
        log.info("Updating segment: {}", id);

        Segment segment = segmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Segment", "id", id));

        segment.setSegmentName(segmentUpdate.getSegmentName());
        segment.setDescription(segmentUpdate.getDescription());

        return segmentRepository.save(segment);
    }

    /**
     * Deactivate segment
     */
    @Transactional
    public Segment deactivateSegment(Long id) {
        log.info("Deactivating segment: {}", id);

        Segment segment = segmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Segment", "id", id));

        segment.setIsActive(false);
        return segmentRepository.save(segment);
    }
}

