package com.example.smartemailassistant.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.smartemailassistant.model.EmailRequest;
import com.example.smartemailassistant.model.EmailResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class OllamaEmailService {

    private final RestTemplate restTemplate;

    @Value("${ai.base-url:http://localhost:11434}")
    private String ollamaBaseUrl;

    @Value("${ai.model:mistral}")
    private String aiModel;

    public OllamaEmailService() {
        this.restTemplate = new RestTemplate();
        // Increase timeout settings
        // ((HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory()).setConnectTimeout(10000);
        // ((HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory()).setReadTimeout(30000);
    }

    public EmailResponse generateEmail(EmailRequest request) {
        System.out.println("=== 🚀 STARTING EMAIL GENERATION ===");
        System.out.println("🔧 AI_BASE_URL: " + ollamaBaseUrl);
        System.out.println("🔧 OLLAMA_MODEL: " + aiModel);

        try {
            String prompt = buildPrompt(request);
            System.out.println("💬 Prompt: " + prompt);

            // Test basic connection first
            if (!testConnection()) {
                throw new RuntimeException("Cannot connect to Ollama service");
            }

            // Try endpoints in sequence
            EmailResponse response = tryChatCompletionsEndpoint(prompt);
            if (response != null && response.isSuccess()) {
                return response;
            }

            response = tryGenerateEndpoint(prompt);
            if (response != null && response.isSuccess()) {
                return response;
            }

            // Final fallback
            return createFallbackResponse(request, "All API endpoints failed. Please check if Ollama is running and accessible.");

        } catch (Exception e) {
            System.out.println("❌ FINAL ERROR: " + e.getMessage());
            return createFallbackResponse(request, "Service error: " + e.getMessage());
        }
    }

    private boolean testConnection() {
        try {
            String testUrl = ollamaBaseUrl + "/api/tags";
            System.out.println("🔍 Testing connection to: " + testUrl);

            HttpHeaders headers = createHeaders();
            // Remove body for GET request
            ResponseEntity<String> response = restTemplate.getForEntity(testUrl, String.class);

            System.out.println("✅ Connection test SUCCESS: " + response.getStatusCode());
            return true;

        } catch (Exception e) {
            System.out.println("❌ Connection test FAILED: " + e.getMessage());
            return false;
        }
    }

    private EmailResponse tryChatCompletionsEndpoint(String prompt) {
        System.out.println("🔄 Attempting /v1/chat/completions endpoint...");
        try {
            String apiUrl = ollamaBaseUrl + "/v1/chat/completions";
            System.out.println("🔗 Endpoint URL: " + apiUrl);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", aiModel);
            requestBody.put("messages", new Object[]{
                    Map.of("role", "user", "content", prompt)
            });
            requestBody.put("stream", false);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 1000);

            HttpHeaders headers = createHeaders();
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
            System.out.println("📡 Response status: " + response.getStatusCode());

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();

                // Parse OpenAI-compatible response
                if (responseBody.containsKey("choices")) {
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                    if (choices != null && !choices.isEmpty()) {
                        Map<String, Object> firstChoice = choices.get(0);
                        if (firstChoice.containsKey("message")) {
                            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
                            String content = (String) message.get("content");

                            System.out.println("✅ /v1/chat/completions endpoint SUCCESS");
                            return parseGeneratedContent(content);
                        }
                    }
                }
            }

            System.out.println("❌ /v1/chat/completions endpoint failed - invalid response format");
            return null;

        } catch (Exception e) {
            System.out.println("❌ /v1/chat/completions endpoint FAILED: " + e.getMessage());
            return null;
        }
    }

    private EmailResponse tryGenerateEndpoint(String prompt) {
        System.out.println("🔄 Attempting /api/generate endpoint...");
        try {
            String apiUrl = ollamaBaseUrl + "/api/generate";
            System.out.println("🔗 Endpoint URL: " + apiUrl);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", aiModel);
            requestBody.put("prompt", prompt);
            requestBody.put("stream", false);
            requestBody.put("temperature", 0.7);

            HttpHeaders headers = createHeaders();
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
            System.out.println("📡 Response status: " + response.getStatusCode());

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();

                if (responseBody.containsKey("response")) {
                    String content = (String) responseBody.get("response");
                    System.out.println("✅ /api/generate endpoint SUCCESS");
                    return parseGeneratedContent(content);
                }
            }

            System.out.println("❌ /api/generate endpoint failed - invalid response format");
            return null;

        } catch (Exception e) {
            System.out.println("❌ /api/generate endpoint FAILED: " + e.getMessage());
            return null;
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("User-Agent", "SmartEmailAssistant/1.0");
        headers.set("Accept", "application/json");

        // Add ngrok headers if using ngrok, remove if using Serveo
        if (ollamaBaseUrl.contains("ngrok")) {
            headers.set("ngrok-skip-browser-warning", "true");
        }

        return headers;
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
        response.setProvider("Ollama AI");

        try {
            if (content.contains("SUBJECT:") && content.contains("BODY:")) {
                // Extract subject
                int subjectStart = content.indexOf("SUBJECT:") + "SUBJECT:".length();
                int bodyStart = content.indexOf("BODY:");
                String subject = content.substring(subjectStart, bodyStart).trim();

                // Extract body
                int bodyContentStart = bodyStart + "BODY:".length();
                String body = content.substring(bodyContentStart).trim();

                response.setSubject(subject);
                response.setGeneratedEmail(body);
            } else {
                // Fallback parsing
                extractSubjectAndBody(content, response);
            }
        } catch (Exception e) {
            System.out.println("❌ Content parsing failed: " + e.getMessage());
            extractSubjectAndBody(content, response);
        }

        return response;
    }

    private void extractSubjectAndBody(String content, EmailResponse response) {
        // Simple fallback - first line as subject, rest as body
        String[] lines = content.split("\n");
        if (lines.length > 0) {
            String subject = lines[0].trim();
            // Clean up subject if it's too long
            if (subject.length() > 100) {
                subject = subject.substring(0, 100) + "...";
            }
            response.setSubject(subject.isEmpty() ? "AI Generated Email" : subject);

            // Combine remaining lines as body
            StringBuilder body = new StringBuilder();
            for (int i = 1; i < lines.length; i++) {
                body.append(lines[i].trim()).append("\n");
            }
            String emailBody = body.toString().trim();
            response.setGeneratedEmail(emailBody.isEmpty() ? content : emailBody);
        } else {
            response.setSubject("Generated Email");
            response.setGeneratedEmail(content);
        }
    }

    private EmailResponse createFallbackResponse(EmailRequest request, String error) {
        EmailResponse response = new EmailResponse();
        response.setSuccess(false);
        response.setProvider("Fallback");
        response.setErrorMessage(error);

        // Create a simple fallback email
        String fallbackEmail = String.format(
                "Dear %s,\n\nI hope this message finds you well. %s\n\nBest regards,\n[Your Name]",
                request.getRecipient(),
                request.getPrompt()
        );

        response.setGeneratedEmail(fallbackEmail);
        response.setSubject("Regarding: " + request.getSubjectHint());

        return response;
    }
}