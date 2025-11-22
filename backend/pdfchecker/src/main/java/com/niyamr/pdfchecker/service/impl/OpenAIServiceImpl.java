package com.niyamr.pdfchecker.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niyamr.pdfchecker.exception.LLMServiceException;
import com.niyamr.pdfchecker.model.LLMResponse;

import com.niyamr.pdfchecker.constant.AppConstants;
import com.niyamr.pdfchecker.model.LLMRequest;
import com.niyamr.pdfchecker.service.LLMService;
import com.niyamr.pdfchecker.util.PromptBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of LLM service supporting both OpenAI and Anthropic APIs
 */
@Slf4j
@Service
public class OpenAIServiceImpl implements LLMService {
    
    @Value("${llm.api-key}")
    private String apiKey;
    
    @Value("${llm.api-url}")
    private String apiUrl;
    
    @Value("${llm.model}")
    private String model;
    
    @Value("${llm.max-tokens}")
    private Integer maxTokens;
    
    @Value("${llm.temperature}")
    private Double temperature;
    
    @Value("${llm.provider:openai}")
    private String provider;
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final PromptBuilder promptBuilder;
    
    public OpenAIServiceImpl() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.promptBuilder = new PromptBuilder();
    }
    
    @Override
    public LLMResponse checkRule(String documentText, String rule) {
        log.info("Checking rule with LLM ({}): {}", provider, rule);
        
        try {
            LLMRequest request = buildRequest(documentText, rule);
            String responseText = callLLMApi(request);
            LLMResponse response = parseResponse(responseText);
            
            log.info("LLM check completed - Status: {}, Confidence: {}", 
                    response.getStatus(), response.getConfidence());
            
            return response;
            
        } catch (Exception e) {
            log.error("Error checking rule with LLM", e);
            return LLMResponse.builder()
                    .status(AppConstants.STATUS_ERROR)
                    .evidence("Error occurred during LLM processing")
                    .reasoning("Failed to process rule: " + e.getMessage())
                    .confidence(0)
                    .error(e.getMessage())
                    .build();
        }
    }
    
    @Override
    public LLMRequest buildRequest(String documentText, String rule) {
        return LLMRequest.builder()
                .model(model)
                .documentText(documentText)
                .rule(rule)
                .maxTokens(maxTokens)
                .temperature(temperature)
                .systemPrompt(promptBuilder.buildSystemPrompt())
                .userPrompt(promptBuilder.buildUserPrompt(documentText, rule))
                .build();
    }
    
    @Override
    public LLMResponse parseResponse(String responseText) {
        try {
            JsonNode jsonNode = objectMapper.readTree(responseText);
            
            return LLMResponse.builder()
                    .status(jsonNode.get("status").asText())
                    .evidence(jsonNode.get("evidence").asText())
                    .reasoning(jsonNode.get("reasoning").asText())
                    .confidence(jsonNode.get("confidence").asInt())
                    .rawResponse(responseText)
                    .build();
                    
        } catch (Exception e) {
            log.error("Failed to parse LLM response", e);
            throw new LLMServiceException("Failed to parse LLM response", e);
        }
    }
    
    /**
     * Call LLM API - supports both OpenAI and Anthropic
     */
    private String callLLMApi(LLMRequest request) {
        try {
            if ("anthropic".equalsIgnoreCase(provider)) {
                return callAnthropicApi(request);
            } else {
                return callOpenAIApi(request);
            }
        } catch (Exception e) {
            log.error("Failed to call LLM API", e);
            throw new LLMServiceException("Failed to call LLM API", e);
        }
    }
    
    /**
     * Call OpenAI API (GPT)
     */
    private String callOpenAIApi(LLMRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", request.getModel());
            requestBody.put("max_tokens", request.getMaxTokens());
            requestBody.put("temperature", request.getTemperature());
            requestBody.put("messages", List.of(
                Map.of("role", "system", "content", request.getSystemPrompt()),
                Map.of("role", "user", "content", request.getUserPrompt())
            ));
            
            // Request JSON response format
            requestBody.put("response_format", Map.of("type", "json_object"));
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            log.debug("Calling OpenAI API: {}", apiUrl);
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl, HttpMethod.POST, entity, String.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                return jsonNode.get("choices").get(0).get("message").get("content").asText();
            } else {
                throw new LLMServiceException("OpenAI API returned status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            log.error("Failed to call OpenAI API", e);
            throw new LLMServiceException("Failed to call OpenAI API", e);
        }
    }
    
    /**
     * Call Anthropic API (Claude)
     */
    private String callAnthropicApi(LLMRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-api-key", apiKey);
            headers.set("anthropic-version", "2023-06-01");
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", request.getModel());
            requestBody.put("max_tokens", request.getMaxTokens());
            requestBody.put("temperature", request.getTemperature());
            requestBody.put("system", request.getSystemPrompt());
            requestBody.put("messages", List.of(
                Map.of("role", "user", "content", request.getUserPrompt())
            ));
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            log.debug("Calling Anthropic API: {}", apiUrl);
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl, HttpMethod.POST, entity, String.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                return jsonNode.get("content").get(0).get("text").asText();
            } else {
                throw new LLMServiceException("Anthropic API returned status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            log.error("Failed to call Anthropic API", e);
            throw new LLMServiceException("Failed to call Anthropic API", e);
        }
    }
}