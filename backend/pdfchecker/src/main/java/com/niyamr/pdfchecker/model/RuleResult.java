package com.niyamr.pdfchecker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Domain model representing the result of a single rule check
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleResult implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * The rule that was checked
     */
    private String rule;
    
    /**
     * Status: PASS, FAIL, or ERROR
     */
    private String status;
    
    /**
     * Evidence sentence supporting the result
     */
    private String evidence;
    
    /**
     * Reasoning behind the decision
     */
    private String reasoning;
    
    /**
     * Confidence score (0-100)
     */
    private Integer confidence;
    
    /**
     * Check if the rule passed
     */
    public boolean isPassed() {
        return "PASS".equalsIgnoreCase(status);
    }
    
    /**
     * Check if the rule failed
     */
    public boolean isFailed() {
        return "FAIL".equalsIgnoreCase(status);
    }
    
    /**
     * Check if there was an error
     */
    public boolean hasError() {
        return "ERROR".equalsIgnoreCase(status);
    }
}