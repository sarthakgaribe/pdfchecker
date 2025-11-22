package com.niyamr.pdfchecker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import com.niyamr.pdfchecker.model.RuleResult;

/**
 * DTO for PDF check response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckResponse {
    
    /**
     * Name of the checked file
     */
    private String fileName;
    
    /**
     * Total number of pages in the document
     */
    private Integer totalPages;
    
    /**
     * List of rule check results
     */
    private List<RuleResult> results;
    
    /**
     * Timestamp of the check
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    /**
     * Overall status (all pass, some fail, etc.)
     */
    private String overallStatus;
    
    /**
     * Processing time in milliseconds
     */
    private Long processingTimeMs;
    
    /**
     * Calculate overall status based on individual results
     */
    public void calculateOverallStatus() {
        if (results == null || results.isEmpty()) {
            this.overallStatus = "NO_RESULTS";
            return;
        }
        
        boolean hasError = results.stream().anyMatch(RuleResult::hasError);
        if (hasError) {
            this.overallStatus = "ERROR";
            return;
        }
        
        boolean allPassed = results.stream().allMatch(RuleResult::isPassed);
        if (allPassed) {
            this.overallStatus = "ALL_PASS";
            return;
        }
        
        boolean allFailed = results.stream().allMatch(RuleResult::isFailed);
        if (allFailed) {
            this.overallStatus = "ALL_FAIL";
            return;
        }
        
        this.overallStatus = "PARTIAL_PASS";
    }
}