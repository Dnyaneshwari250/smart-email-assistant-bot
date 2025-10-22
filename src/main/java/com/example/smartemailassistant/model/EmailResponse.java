package com.example.smartemailassistant.model;

public class EmailResponse {
    private String email;
    private String generatedEmail;
    private String subject;

    public EmailResponse() {}

    public EmailResponse(String email) {
        this.email = email;
    }

    public EmailResponse(String generatedEmail, String subject) {
        this.generatedEmail = generatedEmail;
        this.subject = subject;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGeneratedEmail() {
        return generatedEmail;
    }

    public void setGeneratedEmail(String generatedEmail) {
        this.generatedEmail = generatedEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
