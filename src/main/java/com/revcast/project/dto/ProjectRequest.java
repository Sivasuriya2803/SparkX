package com.revcast.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {

    @NotBlank(message = "Project name cannot be empty")
    @Size(max = 255, message = "Project name cannot exceed 255 characters")
    @Schema(description = "Name of the project", example = "RevCast Mobile App")
    private String projectName;

    @NotBlank(message = "Project code cannot be empty")
    @Size(max = 50, message = "Project code cannot exceed 50 characters")
    @Schema(description = "Unique code for the project", example = "RCMOBILE-001")
    private String projectCode;

    @NotNull(message = "Segment ID cannot be null")
    @Schema(description = "ID of the segment this project belongs to", example = "1")
    private Long segmentId;

    @Schema(description = "Start date of the project", example = "2023-01-01")
    private LocalDate startDate;

    @Schema(description = "End date of the project", example = "2023-12-31")
    private LocalDate endDate;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Schema(description = "Description of the project", example = "Development of the RevCast mobile application for iOS and Android.")
    private String description;

    @Schema(description = "Is the project currently active?", example = "true")
    private Boolean isActive = true;
}
