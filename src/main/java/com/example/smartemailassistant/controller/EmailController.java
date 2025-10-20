package com.example.smartemailassistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.smartemailassistant.model.EmailRequest;
import com.example.smartemailassistant.model.EmailResponse;
import com.example.smartemailassistant.service.GeminiEmailService;

@RestController
@CrossOrigin(origins = "*")
public class EmailController {

    @Autowired
    private GeminiEmailService emailService;

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
    }
}