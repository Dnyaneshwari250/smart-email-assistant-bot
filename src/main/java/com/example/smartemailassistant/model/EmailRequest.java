package com.example.smartemailassistant.model;

public class EmailRequest {
    private String purpose;
    private String tone;
    private String recipient;
    private String subjectHint;

    // Default constructor
    public EmailRequest() {}

<<<<<<< HEAD
    // Parameterized constructor
    public EmailRequest(String purpose, String tone, String recipient, String subjectHint) {
        this.purpose = purpose;
        this.tone = tone;
        this.recipient = recipient;
        this.subjectHint = subjectHint;
    }

    // Getters and setters
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
=======
    // Getters and Setters
    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
>>>>>>> 504b364f9f1e90c4a266e2dee449f232aa4bd926

    public String getTone() { return tone; }
    public void setTone(String tone) { this.tone = tone; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public String getSubjectHint() { return subjectHint; }
    public void setSubjectHint(String subjectHint) { this.subjectHint = subjectHint; }

    // Alias method for getPurpose to match your service
    public String getPrompt() { return purpose; }
}