package com.niyamr.pdfchecker.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * DTO for PDF check request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckRequest {
    
    /**
     * PDF file to be checked
     */
    @NotNull(message = "PDF file is required")
    private MultipartFile file;
    
    /**
     * List of rules to check against the document
     */
    @NotEmpty(message = "At least one rule is required")
    @Size(min = 1, max = 10, message = "Number of rules must be between 1 and 10")
    private List<@NotEmpty(message = "Rule cannot be empty") String> rules;
    
    /**
     * Validate file type
     */
    public boolean isPdfFile() {
        if (file == null || file.getOriginalFilename() == null) {
            return false;
        }
        return file.getOriginalFilename().toLowerCase().endsWith(".pdf");
    }
    
    /**
     * Get file size in MB
     */
    public double getFileSizeInMB() {
        if (file == null) {
            return 0;
        }
        return file.getSize() / (1024.0 * 1024.0);
    }
}