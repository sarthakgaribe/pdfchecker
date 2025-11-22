package com.niyamr.pdfchecker.service;

import com.niyamr.pdfchecker.dto.CheckRequest;

import java.util.List;

/**
 * Service interface for validation operations
 */
public interface ValidationService {
    
    /**
     * Validate the entire check request
     * 
     * @param request Check request to validate
     * @throws com.niyamr.pdfchecker.com.niyamr.pdfchecker.exception.ValidationException if validation fails
     */
    void validateRequest(CheckRequest request);
    
    /**
     * Validate rules list
     * 
     * @param rules List of rules to validate
     * @return List of validation error messages (empty if valid)
     */
    List<String> validateRules(List<String> rules);
    
    /**
     * Validate file extension
     * 
     * @param fileName File name to validate
     * @return true if valid PDF extension
     */
    boolean isValidPdfExtension(String fileName);
    
    /**
     * Validate file size
     * 
     * @param fileSizeBytes File size in bytes
     * @param maxSizeMB Maximum allowed size in MB
     * @return true if within limit
     */
    boolean isValidFileSize(long fileSizeBytes, int maxSizeMB);
}