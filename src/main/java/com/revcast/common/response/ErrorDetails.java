package com.revcast.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Error details in API response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorDetails {

    private String code;
    private String details;
    private String timestamp;

    public static ErrorDetails of(String code, String details) {
        return ErrorDetails.builder()
                .code(code)
                .details(details)
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();
    }
}

