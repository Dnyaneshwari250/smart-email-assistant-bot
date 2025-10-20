package com.example.smartemailassistant.model;

public class EmailResponse {
    private String generatedEmail;
    private String subject;
    private boolean success;
    private String provider;
    private String prompt;

    // Default constructor
    public EmailResponse() {}

<<<<<<< HEAD
    // Constructor for all fields
    public EmailResponse(String generatedEmail, String subject, boolean success, String provider, String prompt) {
        this.generatedEmail = generatedEmail;
        this.subject = subject;
        this.success = success;
        this.provider = provider;
        this.prompt = prompt;
    }

    // Constructor for basic fields
    public EmailResponse(String generatedEmail, String subject) {
        this.generatedEmail = generatedEmail;
        this.subject = subject;
        this.success = true;
        this.provider = "Google Gemini AI";
        this.prompt = "";
    }

    // Getters and setters
=======
    // Getters and Setters
>>>>>>> 504b364f9f1e90c4a266e2dee449f232aa4bd926
    public String getGeneratedEmail() { return generatedEmail; }
    public void setGeneratedEmail(String generatedEmail) { this.generatedEmail = generatedEmail; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
}