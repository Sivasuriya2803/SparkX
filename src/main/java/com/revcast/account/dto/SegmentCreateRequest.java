package com.revcast.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Segment Create Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SegmentCreateRequest {

    @NotBlank(message = "Segment name is required")
    private String segmentName;

    @NotBlank(message = "Segment code is required")
    private String segmentCode;

    @NotNull(message = "Account ID is required")
    private Long accountId;

    private String description;
}

