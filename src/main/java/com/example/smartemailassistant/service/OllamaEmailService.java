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

@Service
public class OllamaEmailService {

    private final RestTemplate restTemplate;

    @Value("${AI_BASE_URL:http://localhost:11434}")
    private String ollamaBaseUrl;

    @Value("${OLLAMA_MODEL:mistral}")
    private String aiModel;

    public OllamaEmailService() {
        this.restTemplate = new RestTemplate();
    }

    public EmailResponse generateEmail(EmailRequest request) {
        try {
            String prompt = buildPrompt(request);

            // Use the configured base URL and model
            String apiUrl = ollamaBaseUrl + "/api/generate/";

            // Prepare the request to Ollama
            Map<String, Object> ollamaRequest = new HashMap<>();
            ollamaRequest.put("model", aiModel);
            ollamaRequest.put("prompt", prompt);
            ollamaRequest.put("stream", false);
            ollamaRequest.put("options", Map.of(
                    "temperature", 0.7,
                    "num_predict", 1000
            ));

            // UPDATED: Added ngrok headers to fix 403 Forbidden errors
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("ngrok-skip-browser-warning", "true");
            headers.set("User-Agent", "SmartEmailAssistant");
            headers.set("Accept", "*/*");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(ollamaRequest, headers);

            System.out.println("🚀 Sending to Ollama at: " + apiUrl);
            System.out.println("📝 Using model: " + aiModel);
            System.out.println("💬 Prompt: " + prompt);

            // Send request to Ollama
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String generatedContent = (String) response.getBody().get("response");
                System.out.println("✅ Ollama response received");
                return parseGeneratedContent(generatedContent);
            } else {
                throw new RuntimeException("Ollama API returned: " + response.getStatusCode());
            }

        } catch (Exception e) {
            System.out.println("❌ Ollama failed: " + e.getMessage());
            return createFallbackResponse(request, e.getMessage());
        }
    }

    private String buildPrompt(EmailRequest request) {
        return String.format(
                "Generate a %s email based on: %s. Recipient: %s. Subject hint: %s. " +
                        "Return in format: SUBJECT: <subject>\\n\\nBODY: <email body>",
                request.getTone().toLowerCase(),
                request.getPrompt(),
                request.getRecipient(),
                request.getSubjectHint()
        );
    }

    private EmailResponse parseGeneratedContent(String content) {
        EmailResponse response = new EmailResponse();
        response.setSuccess(true);
        response.setProvider("Ollama Mistral");

        try {
            // Look for the SUBJECT: and BODY: markers
            if (content.contains("SUBJECT:") && content.contains("BODY:")) {
                int subjectStart = content.indexOf("SUBJECT:") + "SUBJECT:".length();
                int bodyStart = content.indexOf("BODY:") + "BODY:".length();

                String subject = content.substring(subjectStart, content.indexOf("BODY:")).trim();
                String body = content.substring(bodyStart).trim();

                response.setSubject(subject);
                response.setGeneratedEmail(body);
            } else {
                // If parsing fails, use intelligent extraction
                extractSubjectAndBody(content, response);
            }
        } catch (Exception e) {
            System.out.println("❌ Parsing failed, using fallback: " + e.getMessage());
            extractSubjectAndBody(content, response);
        }

        return response;
    }

    private void extractSubjectAndBody(String content, EmailResponse response) {
        // Split by lines and try to find subject and body
        String[] lines = content.split("\n");

        if (lines.length > 0) {
            // First non-empty line as subject
            String subject = lines[0].trim();
            if (subject.length() > 0 && subject.length() < 100) {
                response.setSubject(subject);

                // Rest as body
                StringBuilder body = new StringBuilder();
                for (int i = 1; i < lines.length; i++) {
                    if (!lines[i].trim().isEmpty()) {
                        body.append(lines[i]).append("\n");
                    }
                }
                response.setGeneratedEmail(body.toString().trim());
                return;
            }
        }

        // Final fallback
        response.setSubject("Generated Email");
        response.setGeneratedEmail(content);
    }

    private EmailResponse createFallbackResponse(EmailRequest request, String error) {
        EmailResponse response = new EmailResponse();
        response.setSuccess(false);
        response.setProvider("Ollama Mistral");
        response.setErrorMessage(error);

        response.setGeneratedEmail(
                "Dear " + request.getRecipient() + ",\n\n" +
                        "I hope this message finds you well. This is regarding: " + request.getPrompt() + "\n\n" +
                        "Best regards,\n" +
                        "[Your Name]\n\n" +
                        "---\n" +
                        "AI Service Note: " + error + "\n" +
                        "Please check if Ollama is running and accessible."
        );
        response.setSubject("Demo: " + request.getSubjectHint());
        return response;
    }
}