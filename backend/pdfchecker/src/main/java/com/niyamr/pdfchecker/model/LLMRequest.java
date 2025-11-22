package com.niyamr.pdfchecker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model for LLM API request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LLMRequest {
    
    /**
     * Model to use (e.g., "gpt-4", "claude-sonnet-4-20250514")
     */
    private String model;
    
    /**
     * Extracted PDF text
     */
    private String documentText;
    
    /**
     * Rule to check
     */
    private String rule;
    
    /**
     * Maximum tokens for response
     */
    @Builder.Default
    private Integer maxTokens = 1000;
    
    /**
     * Temperature for response generation
     */
    @Builder.Default
    private Double temperature = 0.3;
    
    /**
     * System prompt for the LLM
     */
    private String systemPrompt;
    
    /**
     * User prompt for the LLM
     */
    private String userPrompt;
}