package com.example.smartemailassistant.controller;

<<<<<<< HEAD
import com.example.smartemailassistant.model.EmailRequest;
import com.example.smartemailassistant.model.EmailResponse;
import com.example.smartemailassistant.service.GeminiEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
=======
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.smartemailassistant.model.EmailRequest;
import com.example.smartemailassistant.model.EmailResponse;
import com.example.smartemailassistant.service.GeminiEmailService;

@RestController
>>>>>>> 504b364f9f1e90c4a266e2dee449f232aa4bd926
@CrossOrigin(origins = "*")
public class EmailController {

    @Value("${gemini.api.key:}")
    private String apiKey;

<<<<<<< HEAD
    private final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent";

    private final GeminiEmailService emailService;

    public EmailController(GeminiEmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/generate-email")
    public ResponseEntity<EmailResponse> generateEmail(@RequestBody EmailRequest request) {
        try {
            System.out.println("Received request - Purpose: " + request.getPurpose() + ", Tone: " + request.getTone());

            // Try Gemini API first if API key is available
            if (apiKey != null && !apiKey.trim().isEmpty()) {
                try {
                    String prompt = createPrompt(request);
                    String generatedEmail = callGeminiAPI(prompt);

                    if (generatedEmail != null) {
                        // Success case with Gemini
                        EmailResponse response = new EmailResponse(
                                generatedEmail,
                                request.getSubjectHint(),
                                true,
                                "Google Gemini AI",
                                request.getPurpose()
                        );
                        return ResponseEntity.ok(response);
                    }
                } catch (Exception e) {
                    System.err.println("Gemini API failed, using fallback: " + e.getMessage());
                }
            }

            // Fallback to our smart service
            System.out.println("Using Smart Email Service fallback");
            EmailResponse response = emailService.generateEmail(request);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error in generateEmail: " + e.getMessage());
            e.printStackTrace();

            // Final fallback
            EmailResponse response = emailService.generateEmail(request);
            response.setProvider("Emergency Fallback");
            return ResponseEntity.ok(response);
        }
    }

    private String createPrompt(EmailRequest request) {
        return String.format(
                "Generate a %s email to %s about: %s. The subject should be: %s. " +
                        "Make it professional and appropriate for the %s tone. Include a proper greeting and closing. " +
                        "Return only the email body without the subject line. Format it with proper paragraphs.",
                request.getTone().toLowerCase(),
                request.getRecipient(),
                request.getPurpose(),
                request.getSubjectHint(),
                request.getTone().toLowerCase()
        );
    }

    private String callGeminiAPI(String prompt) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> content = new HashMap<>();
            Map<String, Object> part = new HashMap<>();
            part.put("text", prompt);

            content.put("parts", new Object[]{part});
            requestBody.put("contents", new Object[]{content});

            // Add safety settings
            Map<String, Object> safetySettings = new HashMap<>();
            safetySettings.put("category", "HARM_CATEGORY_DANGEROUS_CONTENT");
            safetySettings.put("threshold", "BLOCK_MEDIUM_AND_ABOVE");

            requestBody.put("safetySettings", new Object[]{safetySettings});

            String url = GEMINI_URL + "?key=" + apiKey;

            System.out.println("Calling Gemini API...");
            Map<String, Object> response = restTemplate.postForObject(url, requestBody, Map.class);

            // Extract the generated text from the response
            if (response != null && response.containsKey("candidates")) {
                java.util.List<Map<String, Object>> candidates = (java.util.List<Map<String, Object>>) response.get("candidates");
                if (candidates != null && !candidates.isEmpty()) {
                    Map<String, Object> candidate = candidates.get(0);
                    Map<String, Object> contentMap = (Map<String, Object>) candidate.get("content");
                    java.util.List<Map<String, Object>> parts = (java.util.List<Map<String, Object>>) contentMap.get("parts");
                    String generatedText = (String) parts.get(0).get("text");
                    System.out.println("Successfully generated email with Gemini");
                    return generatedText.trim();
                }
            }

        } catch (Exception e) {
            System.err.println("Error calling Gemini API: " + e.getMessage());
        }

        return null;
=======
    @PostMapping("/generate")
    public ResponseEntity<EmailResponse> generateEmail(@RequestBody EmailRequest request) {
        try {
            System.out.println("📧 Received email generation request");
            System.out.println("Prompt: " + request.getPrompt());
            System.out.println("Tone: " + request.getTone());
            System.out.println("Recipient: " + request.getRecipient());

            EmailResponse response = emailService.generateEmail(request);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Controller error: " + e.getMessage());
            EmailResponse errorResponse = new EmailResponse();
            errorResponse.setSuccess(false);
            errorResponse.setProvider("Google Gemini");
            errorResponse.setErrorMessage("Service unavailable: " + e.getMessage());

            // Create fallback response
            errorResponse.setSubject("Regarding: " + request.getSubjectHint());
            errorResponse.setGeneratedEmail(
                    "Dear " + request.getRecipient() + ",\n\n" +
                            request.getPrompt() + "\n\n" +
                            "Best regards,\n[Your Name]"
            );

            return ResponseEntity.internalServerError().body(errorResponse);
        }
>>>>>>> 504b364f9f1e90c4a266e2dee449f232aa4bd926
    }
}