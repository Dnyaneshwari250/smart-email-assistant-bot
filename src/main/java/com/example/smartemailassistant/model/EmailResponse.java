package com.example.smartemailassistant.model;

public class EmailResponse {
    private String generatedEmail;
    private String subject;

    public EmailResponse() {}

    public EmailResponse(String generatedEmail, String subject) {
        this.generatedEmail = generatedEmail;
        this.subject = subject;
    }

    public String getGeneratedEmail() { return generatedEmail; }
    public void setGeneratedEmail(String generatedEmail) { this.generatedEmail = generatedEmail; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
}
