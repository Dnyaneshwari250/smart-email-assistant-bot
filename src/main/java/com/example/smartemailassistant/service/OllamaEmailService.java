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

            // Try multiple endpoints
            EmailResponse response = tryOpenAIEndpoint(prompt);
            if (response != null) {
                return response;
            }

            // If OpenAI endpoint fails, try legacy endpoint
            response = tryLegacyEndpoint(prompt);
            if (response != null) {
                return response;
            }

            throw new RuntimeException("All API endpoints failed");

        } catch (Exception e) {
            System.out.println("❌ Ollama failed: " + e.getMessage());
            e.printStackTrace();
            return createFallbackResponse(request, e.getMessage());
        }
    }

    private EmailResponse tryOpenAIEndpoint(String prompt) {
        try {
            String apiUrl = ollamaBaseUrl + "/v1/chat/completions";

            Map<String, Object> ollamaRequest = new HashMap<>();
            ollamaRequest.put("model", aiModel);
            ollamaRequest.put("messages", new Object[]{
                    Map.of("role", "user", "content", prompt)
            });
            ollamaRequest.put("stream", false);
            ollamaRequest.put("temperature", 0.7);
            ollamaRequest.put("max_tokens", 1000);

            HttpHeaders headers = createHeaders();
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(ollamaRequest, headers);

            System.out.println("🚀 Trying OpenAI endpoint: " + apiUrl);

            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    Map<String, Object> message = (Map<String, Object>) choice.get("message");
                    String generatedContent = (String) message.get("content");

                    System.out.println("✅ Success with OpenAI endpoint");
                    return parseGeneratedContent(generatedContent);
                }
            }
        } catch (Exception e) {
            System.out.println("❌ OpenAI endpoint failed: " + e.getMessage());
        }
        return null;
    }

    private EmailResponse tryLegacyEndpoint(String prompt) {
        try {
            String apiUrl = ollamaBaseUrl + "/api/generate";

            Map<String, Object> ollamaRequest = new HashMap<>();
            ollamaRequest.put("model", aiModel);
            ollamaRequest.put("prompt", prompt);
            ollamaRequest.put("stream", false);
            ollamaRequest.put("temperature", 0.7);

            HttpHeaders headers = createHeaders();
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(ollamaRequest, headers);

            System.out.println("🚀 Trying legacy endpoint: " + apiUrl);

            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String generatedContent = (String) response.getBody().get("response");
                System.out.println("✅ Success with legacy endpoint");
                return parseGeneratedContent(generatedContent);
            }
        } catch (Exception e) {
            System.out.println("❌ Legacy endpoint failed: " + e.getMessage());
        }
        return null;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("User-Agent", "SmartEmailAssistant");
        headers.set("Accept", "*/*");
        // Removed ngrok-specific headers for Serveo
        return headers;
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
            if (content.contains("SUBJECT:") && content.contains("BODY:")) {
                int subjectStart = content.indexOf("SUBJECT:") + "SUBJECT:".length();
                int bodyStart = content.indexOf("BODY:") + "BODY:".length();

                String subject = content.substring(subjectStart, content.indexOf("BODY:")).trim();
                String body = content.substring(bodyStart).trim();

                response.setSubject(subject);
                response.setGeneratedEmail(body);
            } else {
                extractSubjectAndBody(content, response);
            }
        } catch (Exception e) {
            System.out.println("❌ Parsing failed: " + e.getMessage());
            extractSubjectAndBody(content, response);
        }

        return response;
    }

    private void extractSubjectAndBody(String content, EmailResponse response) {
        String[] lines = content.split("\n");
        if (lines.length > 0) {
            String subject = lines[0].trim();
            if (subject.length() > 0 && subject.length() < 100) {
                response.setSubject(subject);
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
                        "Best regards,\n[Your Name]"
        );
        response.setSubject("Email: " + request.getSubjectHint());
        return response;
    }
}