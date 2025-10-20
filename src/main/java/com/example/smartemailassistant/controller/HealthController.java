package com.example.smartemailassistant.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;

@Controller
public class HealthController {

<<<<<<< HEAD
    @GetMapping("/")
    public String home() {
        System.out.println("🟢 Redirecting to index.html");
        return "redirect:/index.html"; // This will redirect to the static file
    }

=======
    // This serves the HTML page for root path
    @GetMapping("/")
    public String home() {
        System.out.println("🟢 Serving index.html for root path /");
        return "index"; // This will serve index.html from templates folder
    }

    // This serves JSON for /health endpoint
>>>>>>> 504b364f9f1e90c4a266e2dee449f232aa4bd926
    @GetMapping("/health")
    @ResponseBody
    public ResponseEntity<Map<String, String>> health() {
        System.out.println("🟢 Health check endpoint called");
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Smart Email Assistant is running!");
        response.put("service", "Google Gemini API");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
}