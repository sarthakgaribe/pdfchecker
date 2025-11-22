package com.niyamr.pdfchecker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model for LLM API response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LLMResponse {
    
    /**
     * Status: PASS or FAIL
     */
    private String status;
    
    /**
     * Evidence sentence from the document
     */
    private String evidence;
    
    /**
     * Reasoning for the decision
     */
    private String reasoning;
    
    /**
     * Confidence score (0-100)
     */
    private Integer confidence;
    
    /**
     * Raw response from LLM (for debugging)
     */
    private String rawResponse;
    
    /**
     * Error message if any
     */
    private String error;
    
    /**
     * Check if response is valid
     */
    public boolean isValid() {
        return status != null && 
               evidence != null && 
               reasoning != null && 
               confidence != null &&
               confidence >= 0 && 
               confidence <= 100;
    }
    
    /**
     * Check if there was an error
     */
    public boolean hasError() {
        return error != null && !error.isEmpty();
    }
}