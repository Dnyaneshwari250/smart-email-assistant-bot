package com.example.smartemailassistant.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.smartemailassistant.model.EmailRequest;
import com.example.smartemailassistant.model.EmailResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class GeminiEmailService {

    private final RestTemplate restTemplate;

    @Value("${GEMINI_API_KEY:}")
    private String geminiApiKey;

    public GeminiEmailService() {
        this.restTemplate = new RestTemplate();
    }

    public EmailResponse generateEmail(EmailRequest request) {
        try {
            String prompt = buildPrompt(request);
            String apiUrl = "https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent?key=" + geminiApiKey;

            // Build Gemini request
            Map<String, Object> geminiRequest = new HashMap<>();

            Map<String, Object> content = new HashMap<>();
            content.put("parts", new Object[]{
                    Map.of("text", prompt)
            });

            geminiRequest.put("contents", new Object[]{content});
            geminiRequest.put("generationConfig", Map.of(
                    "temperature", 0.7,
                    "maxOutputTokens", 1000
            ));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(geminiRequest, headers);

            System.out.println("🚀 Calling Google Gemini API...");

            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");

                if (candidates != null && !candidates.isEmpty()) {
                    Map<String, Object> firstCandidate = candidates.get(0);
                    Map<String, Object> contentResponse = (Map<String, Object>) firstCandidate.get("content");
                    List<Map<String, Object>> parts = (List<Map<String, Object>>) contentResponse.get("parts");

                    if (parts != null && !parts.isEmpty()) {
                        String generatedContent = (String) parts.get(0).get("text");

                        System.out.println("✅ Gemini response received");
                        return parseGeneratedContent(generatedContent);
                    }
                }
            }

            throw new RuntimeException("Gemini API returned: " + response.getStatusCode());

        } catch (Exception e) {
            System.out.println("❌ Gemini failed: " + e.getMessage());
            return createFallbackResponse(request, e.getMessage());
        }
    }

    private String buildPrompt(EmailRequest request) {
        return String.format(
                "Generate a professional %s email about: %s. The email is for: %s. " +
                        "Subject suggestion: %s. " +
                        "Please format your response exactly as: SUBJECT: <email subject>\\n\\nBODY: <email body content>",
                request.getTone().toLowerCase(),
                request.getPrompt(),
                request.getRecipient(),
                request.getSubjectHint()
        );
    }

    private EmailResponse parseGeneratedContent(String content) {
        EmailResponse response = new EmailResponse();
        response.setSuccess(true);
        response.setProvider("Google Gemini");

        try {
            if (content.contains("SUBJECT:") && content.contains("BODY:")) {
                int subjectStart = content.indexOf("SUBJECT:") + "SUBJECT:".length();
                int bodyStart = content.indexOf("BODY:");
                String subject = content.substring(subjectStart, bodyStart).trim();
                String body = content.substring(bodyStart + "BODY:".length()).trim();

                response.setSubject(subject);
                response.setGeneratedEmail(body);
            } else {
                // Fallback parsing
                String[] lines = content.split("\n");
                if (lines.length > 0) {
                    response.setSubject(lines[0].trim());
                    StringBuilder body = new StringBuilder();
                    for (int i = 1; i < lines.length; i++) {
                        body.append(lines[i]).append("\n");
                    }
                    response.setGeneratedEmail(body.toString().trim());
                }
            }
        } catch (Exception e) {
            response.setSubject("AI Generated Email");
            response.setGeneratedEmail(content);
        }

        return response;
    }

    private EmailResponse createFallbackResponse(EmailRequest request, String error) {
        EmailResponse response = new EmailResponse();
        response.setSuccess(false);
        response.setProvider("Google Gemini");
        response.setErrorMessage(error);

        response.setGeneratedEmail(
                "Dear " + request.getRecipient() + ",\n\n" +
                        "I hope this message finds you well. " + request.getPrompt() + "\n\n" +
                        "Best regards,\n[Your Name]"
        );
        response.setSubject("Regarding: " + request.getSubjectHint());
        return response;
    }
}