package com.niyamr.pdfchecker.util;

import org.springframework.stereotype.Component;

/**
 * Utility class for building LLM prompts
 */
@Component
public class PromptBuilder {
    
    /**
     * Build system prompt for LLM
     */
    public String buildSystemPrompt() {
        return """
                You are a document compliance checker. Your task is to analyze documents 
                and verify if they comply with specific rules.
                
                For each rule, you must respond ONLY with a valid JSON object in this exact format:
                {
                    "status": "PASS" or "FAIL",
                    "evidence": "A specific sentence or phrase from the document that supports your decision",
                    "reasoning": "Brief explanation of why the rule passed or failed",
                    "confidence": <number between 0-100>
                }
                
                Guidelines:
                - Be precise and objective
                - Use exact quotes from the document as evidence
                - Confidence should reflect how certain you are about the decision
                - If the rule is satisfied, status should be "PASS"
                - If the rule is not satisfied, status should be "FAIL"
                - Always provide clear reasoning
                """;
    }
    
    /**
     * Build user prompt for specific rule check
     */
    public String buildUserPrompt(String documentText, String rule) {
        // Truncate document if too long (to avoid token limits)
        String truncatedText = documentText.length() > 8000 
                ? documentText.substring(0, 8000) + "... [truncated]"
                : documentText;
        
        return String.format("""
                Document to analyze:
                ---
                %s
                ---
                
                Rule to check:
                "%s"
                
                Please analyze the document and respond with ONLY a JSON object as specified.
                """, truncatedText, rule);
    }
    
    /**
     * Build prompt for batch rule checking
     */
    public String buildBatchPrompt(String documentText, String... rules) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Document to analyze:\n---\n");
        
        String truncatedText = documentText.length() > 8000 
                ? documentText.substring(0, 8000) + "... [truncated]"
                : documentText;
        
        prompt.append(truncatedText).append("\n---\n\n");
        prompt.append("Rules to check:\n");
        
        for (int i = 0; i < rules.length; i++) {
            prompt.append(String.format("%d. %s\n", i + 1, rules[i]));
        }
        
        prompt.append("\nPlease analyze each rule and respond with a JSON array of results.");
        
        return prompt.toString();
    }
}