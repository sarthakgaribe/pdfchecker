package com.niyamr.pdfchecker.exception;

/**
 * Exception for PDF processing errors
 */
public class PdfProcessingException extends RuntimeException {
    
    public PdfProcessingException(String message) {
        super(message);
    }
    
    public PdfProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}

