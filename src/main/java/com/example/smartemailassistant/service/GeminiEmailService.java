package com.example.smartemailassistant.service;

import org.springframework.stereotype.Service;
<<<<<<< HEAD
import com.example.smartemailassistant.model.EmailRequest;
import com.example.smartemailassistant.model.EmailResponse;
import java.util.Random;
=======
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;

import com.example.smartemailassistant.model.EmailRequest;
import com.example.smartemailassistant.model.EmailResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.nio.charset.StandardCharsets;
>>>>>>> 504b364f9f1e90c4a266e2dee449f232aa4bd926

@Service
public class GeminiEmailService {

<<<<<<< HEAD
    private final Random random = new Random();

    public EmailResponse generateEmail(EmailRequest request) {
        System.out.println("🎯 Generating AI-powered email...");
        System.out.println("Purpose: " + request.getPurpose());
        System.out.println("Tone: " + request.getTone());
        System.out.println("Recipient: " + request.getRecipient());
        System.out.println("Subject Hint: " + request.getSubjectHint());

        String subject = generateSmartSubject(request);
        String emailBody = generateSmartEmailBody(request);

        // Create response with all required fields
=======
    private final RestTemplate restTemplate;

    @Value("${gemini.api.key:}")  // Changed to read from properties file
    private String geminiApiKey;

    public GeminiEmailService() {
        this.restTemplate = new RestTemplate();
        this.restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    public EmailResponse generateEmail(EmailRequest request) {
        System.out.println("🔧 Starting Gemini Email Generation...");

        // Validate API key
        if (geminiApiKey == null || geminiApiKey.isEmpty() || geminiApiKey.equals("your_actual_gemini_api_key_here")) {
            String error = "❌ API KEY ISSUE: gemini.api.key is: '" + geminiApiKey + "'";
            System.out.println(error);
            return createFallbackResponse(request, "API Key not configured in application.properties. Current value: " + geminiApiKey);
        }

        System.out.println("✅ API Key found (first 10 chars): " + geminiApiKey.substring(0, Math.min(10, geminiApiKey.length())) + "...");

        try {
            String apiUrl = "https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent?key=" + geminiApiKey;
            System.out.println("🌐 Calling Gemini API...");

            Map<String, Object> geminiRequest = createGeminiRequest(request);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("User-Agent", "SmartEmailAssistant/1.0");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(geminiRequest, headers);

            System.out.println("🚀 Sending request to Gemini API...");

            try {
                ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
                System.out.println("✅ Received response with status: " + response.getStatusCode());

                return handleSuccessfulResponse(response);

            } catch (ResourceAccessException e) {
                System.out.println("❌ NETWORK ERROR: " + e.getMessage());
                return createFallbackResponse(request, "Network connection failed. Please check your internet connection.");

            } catch (Exception e) {
                System.out.println("❌ API CALL ERROR: " + e.getClass().getSimpleName() + ": " + e.getMessage());
                return createFallbackResponse(request, "Google Gemini API error: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("❌ UNEXPECTED ERROR: " + e.getMessage());
            return createFallbackResponse(request, "Unexpected error: " + e.getMessage());
        }
    }

    private Map<String, Object> createGeminiRequest(EmailRequest request) {
        String prompt = String.format(
                "Generate a professional %s email about: %s. The email is for: %s. " +
                        "Subject suggestion: %s. " +
                        "Please format your response exactly as: SUBJECT: <email subject>\\n\\nBODY: <email body content>",
                request.getTone().toLowerCase(),
                request.getPrompt(),
                request.getRecipient(),
                request.getSubjectHint()
        );

        System.out.println("📝 Prompt: " + prompt);

        Map<String, Object> geminiRequest = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        content.put("parts", new Object[]{Map.of("text", prompt)});

        geminiRequest.put("contents", new Object[]{content});
        geminiRequest.put("generationConfig", Map.of(
                "temperature", 0.7,
                "maxOutputTokens", 1000,
                "topP", 0.8
        ));

        return geminiRequest;
    }

    private EmailResponse handleSuccessfulResponse(ResponseEntity<Map> response) {
        try {
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null && responseBody.containsKey("candidates")) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");

                if (candidates != null && !candidates.isEmpty()) {
                    Map<String, Object> firstCandidate = candidates.get(0);
                    Map<String, Object> contentResponse = (Map<String, Object>) firstCandidate.get("content");
                    List<Map<String, Object>> parts = (List<Map<String, Object>>) contentResponse.get("parts");

                    if (parts != null && !parts.isEmpty()) {
                        String generatedContent = (String) parts.get(0).get("text");
                        System.out.println("✅ Successfully parsed Gemini response");
                        return parseGeneratedContent(generatedContent);
                    }
                }
            }

            // Check for error in response
            if (responseBody != null && responseBody.containsKey("error")) {
                Map<String, Object> error = (Map<String, Object>) responseBody.get("error");
                String errorMsg = (String) error.get("message");
                System.out.println("❌ Gemini API Error: " + errorMsg);
                throw new RuntimeException("Gemini API Error: " + errorMsg);
            }

            throw new RuntimeException("Unexpected response format from Gemini API");

        } catch (Exception e) {
            System.out.println("❌ Error parsing response: " + e.getMessage());
            throw new RuntimeException("Failed to parse Gemini response", e);
        }
    }

    private EmailResponse parseGeneratedContent(String content) {
>>>>>>> 504b364f9f1e90c4a266e2dee449f232aa4bd926
        EmailResponse response = new EmailResponse();
        response.setGeneratedEmail(emailBody);
        response.setSubject(subject);
        response.setSuccess(true);
<<<<<<< HEAD
        response.setProvider("Smart Email AI");
        response.setPrompt(request.getPurpose());

        System.out.println("✅ Email generated successfully!");
        return response;
    }

    private String generateSmartSubject(EmailRequest request) {
        String tone = request.getTone().toUpperCase();
        String base = request.getSubjectHint();

        switch (tone) {
            case "FORMAL":
                return "Formal Communication: " + base;
            case "PROFESSIONAL":
                return "Update: " + base;
            case "CASUAL":
                return base;
            case "FRIENDLY":
                return "Quick Update: " + base;
            default:
                return base;
        }
    }

    private String generateSmartEmailBody(EmailRequest request) {
        String tone = request.getTone().toUpperCase();
        String recipient = request.getRecipient();
        String purpose = request.getPurpose();

        StringBuilder email = new StringBuilder();

        // Greeting
        email.append(getGreeting(tone)).append(" ").append(recipient).append(",\n\n");

        // Professional body based on purpose and tone
        email.append(generateProfessionalContent(purpose, tone)).append("\n\n");

        // Closing
        email.append(getClosing(tone)).append(",\n[Your Name]");

        return email.toString();
    }

    private String getGreeting(String tone) {
        switch (tone) {
            case "FORMAL": return "Dear";
            case "PROFESSIONAL": return "Hello";
            case "CASUAL": return "Hi";
            case "FRIENDLY": return "Hi there";
            default: return "Hello";
        }
    }

    private String generateProfessionalContent(String purpose, String tone) {
        String[] formalPhrases = {
                "I am writing to bring to your attention regarding " + purpose + ". ",
                "This communication serves to address the matter of " + purpose + ". ",
                "I would like to formally discuss " + purpose + ". "
        };

        String[] professionalPhrases = {
                "I wanted to follow up on " + purpose + ". ",
                "Regarding " + purpose + ", I think we should discuss this further. ",
                "I'm reaching out about " + purpose + ". "
        };

        String[] casualPhrases = {
                "Just wanted to touch base about " + purpose + ". ",
                "Quick update: " + purpose + ". ",
                "Following up on " + purpose + ". "
        };

        String[] friendlyPhrases = {
                "Hope you're doing well! I wanted to chat about " + purpose + ". ",
                "Just circling back on " + purpose + ". ",
                "Wanted to quickly connect about " + purpose + ". "
        };

        String[] content;
        switch (tone) {
            case "FORMAL": content = formalPhrases; break;
            case "PROFESSIONAL": content = professionalPhrases; break;
            case "CASUAL": content = casualPhrases; break;
            case "FRIENDLY": content = friendlyPhrases; break;
            default: content = professionalPhrases;
        }

        String mainContent = content[random.nextInt(content.length)];

        // Add contextually appropriate ending
        String[] endings = {
                "I look forward to your response.",
                "Please let me know your thoughts.",
                "Your input would be greatly appreciated.",
                "Looking forward to hearing from you.",
                "Let me know what you think.",
                "Talk soon!"
        };

        String ending = endings[random.nextInt(endings.length)];

        // Make ending more formal for formal tone
        if ("FORMAL".equals(tone)) {
            ending = "I look forward to your prompt response.";
        }

        return mainContent + ending;
    }

    private String getClosing(String tone) {
        switch (tone) {
            case "FORMAL": return "Sincerely";
            case "PROFESSIONAL": return "Best regards";
            case "CASUAL": return "Best";
            case "FRIENDLY": return "Cheers";
            default: return "Best regards";
        }
=======
        response.setProvider("Google Gemini");

        System.out.println("📄 Raw response: " + content);

        try {
            if (content.contains("SUBJECT:") && content.contains("BODY:")) {
                int subjectStart = content.indexOf("SUBJECT:") + "SUBJECT:".length();
                int bodyStart = content.indexOf("BODY:");
                String subject = content.substring(subjectStart, bodyStart).trim();
                String body = content.substring(bodyStart + "BODY:".length()).trim();

                response.setSubject(subject);
                response.setGeneratedEmail(body);
            } else {
                // Fallback: use first line as subject, rest as body
                String[] lines = content.split("\n");
                if (lines.length > 0) {
                    response.setSubject(lines[0].replace("Subject:", "").replace("SUBJECT:", "").trim());
                    StringBuilder emailBody = new StringBuilder();
                    for (int i = 1; i < lines.length; i++) {
                        emailBody.append(lines[i]).append("\n");
                    }
                    response.setGeneratedEmail(emailBody.toString().trim());
                } else {
                    response.setSubject("AI Generated Email");
                    response.setGeneratedEmail(content);
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Using fallback parsing");
            response.setSubject("AI Generated Email");
            response.setGeneratedEmail(content);
        }

        System.out.println("🎉 Final result - Subject: " + response.getSubject());
        return response;
    }

    private EmailResponse createFallbackResponse(EmailRequest request, String error) {
        System.out.println("🔄 Creating fallback response due to: " + error);

        EmailResponse response = new EmailResponse();
        response.setSuccess(false);
        response.setProvider("Google Gemini");
        response.setErrorMessage(error);

        // Simple fallback email generation
        String fallbackEmail = String.format(
                "Dear %s,\n\nI hope this message finds you well. %s\n\nBest regards,\n[Your Name]",
                request.getRecipient(),
                request.getPrompt()
        );

        response.setGeneratedEmail(fallbackEmail);
        response.setSubject("Regarding: " + request.getSubjectHint());

        return response;
>>>>>>> 504b364f9f1e90c4a266e2dee449f232aa4bd926
    }
}