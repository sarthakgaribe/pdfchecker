package com.niyamr.pdfchecker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for error responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    /**
     * HTTP status code
     */
    private Integer status;
    
    /**
     * Error message
     */
    private String message;
    
    /**
     * Detailed error description
     */
    private String details;
    
    /**
     * List of validation errors (if applicable)
     */
    private List<String> errors;
    
    /**
     * Timestamp of the error
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    /**
     * Request path where error occurred
     */
    private String path;
    
    /**
     * Create a simple error response
     */
    public static ErrorResponse of(Integer status, String message, String path) {
        return ErrorResponse.builder()
                .status(status)
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Create error response with validation errors
     */
    public static ErrorResponse withValidationErrors(Integer status, String message, 
                                                     List<String> errors, String path) {
        return ErrorResponse.builder()
                .status(status)
                .message(message)
                .errors(errors)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }
}