package com.niyamr.pdfchecker.service;

import com.niyamr.pdfchecker.model.LLMResponse;

import com.niyamr.pdfchecker.model.LLMRequest;

/**
 * Service interface for LLM operations
 */
public interface LLMService {
    
    /**
     * Check a single rule against document text using LLM
     * 
     * @param documentText Extracted text from PDF
     * @param rule Rule to check
     * @return LLM response containing status, evidence, reasoning, and confidence
     * @throws com.niyamr.pdfchecker.com.niyamr.pdfchecker.exception.LLMServiceException if LLM call fails
     */
    LLMResponse checkRule(String documentText, String rule);
    
    /**
     * Build LLM request object
     * 
     * @param documentText Document text
     * @param rule Rule to check
     * @return Configured LLM request
     */
    LLMRequest buildRequest(String documentText, String rule);
    
    /**
     * Parse LLM response text into structured format
     * 
     * @param responseText Raw response from LLM
     * @return Parsed LLM response
     */
    LLMResponse parseResponse(String responseText);
}