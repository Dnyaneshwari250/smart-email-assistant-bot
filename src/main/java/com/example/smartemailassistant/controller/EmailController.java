package com.example.smartemailassistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.smartemailassistant.model.EmailRequest;
import com.example.smartemailassistant.model.EmailResponse;
import com.example.smartemailassistant.service.GeminiEmailService;

@Controller
public class EmailController {

    @Autowired
    private GeminiEmailService emailService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("emailRequest", new EmailRequest());
        return "index";
    }

    @PostMapping("/generate")
    @ResponseBody
    public EmailResponse generateEmail(@RequestBody EmailRequest request) {
        System.out.println("🎯 Received email generation request");
        System.out.println("Topic: " + request.getPrompt());
        System.out.println("Tone: " + request.getTone());
        System.out.println("Recipient: " + request.getRecipient());

        EmailResponse response = emailService.generateEmail(request);
        System.out.println("📤 Sending response - Success: " + response.isSuccess());

        return response;
    }

    @GetMapping("/health")
    @ResponseBody
    public String healthCheck() {
        return "Smart Email Assistant is running!";
    }
}