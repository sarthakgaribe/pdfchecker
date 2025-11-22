package com.niyamr.pdfchecker.service.impl;

import com.niyamr.pdfchecker.constant.AppConstants;
import com.niyamr.pdfchecker.dto.CheckRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.niyamr.pdfchecker.exception.ValidationException;
import com.niyamr.pdfchecker.service.ValidationService;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of validation service
 */
@Slf4j
@Service
public class ValidationServiceImpl implements ValidationService {
    
    @Override
    public void validateRequest(CheckRequest request) {
        log.info("Validating check request");
        
        List<String> errors = new ArrayList<>();
        
        // Validate file
        if (request.getFile() == null || request.getFile().isEmpty()) {
            errors.add(AppConstants.FILE_REQUIRED_MSG);
        } else {
            if (!isValidPdfExtension(request.getFile().getOriginalFilename())) {
                errors.add(AppConstants.INVALID_FILE_TYPE_MSG);
            }
            
            if (!isValidFileSize(request.getFile().getSize(), AppConstants.MAX_FILE_SIZE_MB)) {
                errors.add(AppConstants.FILE_SIZE_EXCEEDED_MSG);
            }
        }
        
        // Validate rules
        if (request.getRules() == null || request.getRules().isEmpty()) {
            errors.add(AppConstants.RULES_REQUIRED_MSG);
        } else {
            errors.addAll(validateRules(request.getRules()));
        }
        
        if (!errors.isEmpty()) {
            String errorMessage = String.join(", ", errors);
            log.error("Validation failed: {}", errorMessage);
            throw new ValidationException(errorMessage);
        }
        
        log.info("Request validation successful");
    }
    
    @Override
    public List<String> validateRules(List<String> rules) {
        List<String> errors = new ArrayList<>();
        
        if (rules == null || rules.isEmpty()) {
            errors.add(AppConstants.RULES_REQUIRED_MSG);
            return errors;
        }
        
        for (int i = 0; i < rules.size(); i++) {
            String rule = rules.get(i);
            if (StringUtils.isBlank(rule)) {
                errors.add(String.format("Rule %d: %s", i + 1, AppConstants.RULE_EMPTY_MSG));
            }
            if (rule != null && rule.length() > 500) {
                errors.add(String.format("Rule %d is too long (max 500 characters)", i + 1));
            }
        }
        
        if (rules.size() > 10) {
            errors.add("Maximum 10 rules allowed");
        }
        
        return errors;
    }
    
    @Override
    public boolean isValidPdfExtension(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return false;
        }
        return fileName.toLowerCase().endsWith(AppConstants.PDF_EXTENSION);
    }
    
    @Override
    public boolean isValidFileSize(long fileSizeBytes, int maxSizeMB) {
        long maxSizeBytes = (long) maxSizeMB * 1024 * 1024;
        return fileSizeBytes > 0 && fileSizeBytes <= maxSizeBytes;
    }
}