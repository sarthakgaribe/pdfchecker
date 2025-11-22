package com.niyamr.pdfchecker.constant;

/**
 * Application-wide constants
 */
public final class AppConstants {
    
    private AppConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // API Endpoints
    public static final String API_VERSION = "/v1";
    public static final String PDF_CHECK_ENDPOINT = "/check";
    public static final String HEALTH_ENDPOINT = "/health";
    
    // File Processing
    public static final String PDF_EXTENSION = ".pdf";
    public static final int MAX_PAGES = 50;
    public static final int MAX_FILE_SIZE_MB = 10;
    
    // Validation Messages
    public static final String FILE_REQUIRED_MSG = "PDF file is required";
    public static final String RULES_REQUIRED_MSG = "At least one rule is required";
    public static final String RULE_EMPTY_MSG = "Rule cannot be empty";
    public static final String INVALID_FILE_TYPE_MSG = "Only PDF files are allowed";
    public static final String FILE_SIZE_EXCEEDED_MSG = "File size exceeds maximum limit";
    
    // LLM Configuration
    public static final String LLM_MODEL_DEFAULT = "gpt-4";
    public static final int LLM_MAX_TOKENS = 1000;
    public static final double LLM_TEMPERATURE = 0.3;
    
    // Response Status
    public static final String STATUS_PASS = "PASS";
    public static final String STATUS_FAIL = "FAIL";
    public static final String STATUS_ERROR = "ERROR";
    
    // Confidence Thresholds
    public static final int MIN_CONFIDENCE = 0;
    public static final int MAX_CONFIDENCE = 100;
    public static final int DEFAULT_CONFIDENCE = 50;
}