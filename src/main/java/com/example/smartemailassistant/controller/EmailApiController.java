package com.example.smartemailassistant.controller;

import com.example.smartemailassistant.model.EmailRequest;
import com.example.smartemailassistant.model.EmailResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class EmailApiController {

    @GetMapping("/health")
    public String health() {
        return "OK - Smart Email Assistant is running!";
    }

    @PostMapping("/generate-email")
    public ResponseEntity<?> generateEmail(@RequestBody EmailRequest request) {
        try {
            // For now, return a test response
            // We will integrate Gemini API later
            String testEmail = "Dear " + request.getRecipient() + ",\n\n" +
                    "I am writing to you regarding: " + request.getTopic() + "\n\n" +
                    "This email is written in a " + request.getTone() + " tone.\n\n" +
                    "Best regards,\n" +
                    "Smart Email Assistant";
            
            return ResponseEntity.ok(new EmailResponse(testEmail));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error generating email: " + e.getMessage());
        }
    }
}
