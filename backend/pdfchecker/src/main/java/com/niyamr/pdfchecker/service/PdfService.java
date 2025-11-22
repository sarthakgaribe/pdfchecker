package com.niyamr.pdfchecker.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for PDF operations
 */
public interface PdfService {
    
    /**
     * Extract text from PDF file
     * 
     * @param file MultipartFile containing the PDF
     * @return Extracted text content
     * @throws com.niyamr.pdfchecker.com.niyamr.pdfchecker.exception.PdfProcessingException if extraction fails
     */
    String extractText(MultipartFile file);
    
    /**
     * Get number of pages in PDF
     * 
     * @param file MultipartFile containing the PDF
     * @return Number of pages
     * @throws com.niyamr.pdfchecker.com.niyamr.pdfchecker.exception.PdfProcessingException if reading fails
     */
    int getPageCount(MultipartFile file);
    
    /**
     * Validate PDF file
     * 
     * @param file MultipartFile to validate
     * @throws com.niyamr.pdfchecker.com.niyamr.pdfchecker.exception.ValidationException if validation fails
     */
    void validatePdfFile(MultipartFile file);
}