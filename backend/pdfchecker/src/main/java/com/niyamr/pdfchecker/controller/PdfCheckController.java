package com.niyamr.pdfchecker.controller;

import com.niyamr.pdfchecker.dto.CheckRequest;
import com.niyamr.pdfchecker.service.LLMService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.niyamr.pdfchecker.dto.CheckResponse;
import com.niyamr.pdfchecker.model.LLMResponse;
import com.niyamr.pdfchecker.model.RuleResult;
import com.niyamr.pdfchecker.service.PdfService;
import com.niyamr.pdfchecker.service.ValidationService;

import java.util.ArrayList;
import java.util.List;

/**
 * REST Controller for PDF checking operations
 */
@Slf4j
@RestController
@RequestMapping("/v1/pdf")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PdfCheckController {
    
    private final PdfService pdfService;
    private final LLMService llmService;
    private final ValidationService validationService;
    
    /**
     * Check PDF document against rules
     * 
     * @param file PDF file to check
     * @param rules List of rules (comma-separated or multiple params)
     * @return Check response with results
     */
    @PostMapping(value = "/check", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CheckResponse> checkDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("rules") List<String> rules) {
        
        long startTime = System.currentTimeMillis();
        log.info("Received check request for file: {} with {} rules", 
                file.getOriginalFilename(), rules.size());
        
        try {
            // Create and validate request
            CheckRequest request = CheckRequest.builder()
                    .file(file)
                    .rules(rules)
                    .build();
            
            validationService.validateRequest(request);
            
            // Extract text from PDF
            String documentText = pdfService.extractText(file);
            int pageCount = pdfService.getPageCount(file);
            
            // Check each rule
            List<RuleResult> results = new ArrayList<>();
            for (String rule : rules) {
                LLMResponse llmResponse = llmService.checkRule(documentText, rule);
                
                RuleResult ruleResult = RuleResult.builder()
                        .rule(rule)
                        .status(llmResponse.getStatus())
                        .evidence(llmResponse.getEvidence())
                        .reasoning(llmResponse.getReasoning())
                        .confidence(llmResponse.getConfidence())
                        .build();
                
                results.add(ruleResult);
            }
            
            // Build response
            long processingTime = System.currentTimeMillis() - startTime;
            CheckResponse response = CheckResponse.builder()
                    .fileName(file.getOriginalFilename())
                    .totalPages(pageCount)
                    .results(results)
                    .processingTimeMs(processingTime)
                    .build();
            
            response.calculateOverallStatus();
            
            log.info("Check completed successfully in {}ms - Status: {}", 
                    processingTime, response.getOverallStatus());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error processing check request", e);
            throw e;
        }
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("PDF Checker Service is running");
    }
}
