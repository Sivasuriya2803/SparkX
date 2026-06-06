package com.revcast.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Project Create Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectCreateRequest {

    @NotBlank(message = "Project name is required")
    private String projectName;

    @NotBlank(message = "Project code is required")
    private String projectCode;

    @NotNull(message = "Segment ID is required")
    private Long segmentId;

    private LocalDate startDate;

    private LocalDate endDate;

    private String description;
}

