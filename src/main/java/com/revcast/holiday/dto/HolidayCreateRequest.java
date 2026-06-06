package com.revcast.holiday.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Holiday Create Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HolidayCreateRequest {

    @NotBlank(message = "Holiday name is required")
    private String holidayName;

    @NotNull(message = "Holiday date is required")
    private LocalDate holidayDate;

    private String holidayType;

    private Boolean isCompanyWide = false;

    private Long projectId;

    private String description;
}

