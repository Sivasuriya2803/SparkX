package com.revcast.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Project Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponse {

    private Long id;
    private String projectName;
    private String projectCode;
    private Long segmentId;
    private String segmentName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private Boolean isActive;
}

