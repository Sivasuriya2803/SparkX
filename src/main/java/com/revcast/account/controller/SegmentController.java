package com.revcast.account.controller;

import com.revcast.account.dto.SegmentCreateRequest;
import com.revcast.account.dto.SegmentResponse;
import com.revcast.account.entity.Segment;
import com.revcast.account.entity.Account;
import com.revcast.account.service.SegmentService;
import com.revcast.common.response.ApiResponse;
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
 * Segment Controller
 */
@Slf4j
@RestController
@RequestMapping("/segments")
@Tag(name = "Segment Management", description = "Segment endpoints")
public class SegmentController {

    @Autowired
    private SegmentService segmentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create segment")
    public ResponseEntity<ApiResponse<SegmentResponse>> createSegment(
            @Valid @RequestBody SegmentCreateRequest request) {
        Segment segment = Segment.builder()
                .segmentName(request.getSegmentName())
                .segmentCode(request.getSegmentCode())
                .account(Account.builder().id(request.getAccountId()).build())
                .description(request.getDescription())
                .build();
        Segment saved = segmentService.createSegment(segment);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(mapToResponse(saved), "Segment created"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    @Operation(summary = "Get segment")
    public ResponseEntity<ApiResponse<SegmentResponse>> getSegmentById(@PathVariable Long id) {
        Segment segment = segmentService.getSegmentById(id);
        return ResponseEntity.ok(ApiResponse.success(mapToResponse(segment)));
    }

    @GetMapping("/account/{accountId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    @Operation(summary = "Get segments by account")
    public ResponseEntity<ApiResponse<List<SegmentResponse>>> getByAccount(@PathVariable Long accountId) {
        List<SegmentResponse> segments = segmentService.getSegmentsByAccount(accountId)
                .stream().map(this::mapToResponse).collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(segments));
    }

    private SegmentResponse mapToResponse(Segment segment) {
        return SegmentResponse.builder()
                .id(segment.getId())
                .segmentName(segment.getSegmentName())
                .segmentCode(segment.getSegmentCode())
                .accountId(segment.getAccount().getId())
                .accountName(segment.getAccount().getAccountName())
                .description(segment.getDescription())
                .isActive(segment.getIsActive())
                .build();
    }
}

