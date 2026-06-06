package com.revcast.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Segment Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SegmentResponse {

    private Long id;
    private String segmentName;
    private String segmentCode;
    private Long accountId;
    private String accountName;
    private String description;
    private Boolean isActive;
}

