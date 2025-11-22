package com.niyamr.pdfchecker.exception;

/**
 * Exception for LLM service errors
 */
public class LLMServiceException extends RuntimeException {
    
    public LLMServiceException(String message) {
        super(message);
    }
    
    public LLMServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}