package com.example.smartemailassistant.model;

public class EmailResponse {
    private String generatedEmail;
    private String subject;
    private boolean success;
    private String provider;
    private String errorMessage;

    // Getters and Setters
    public String getGeneratedEmail() { return generatedEmail; }
    public void setGeneratedEmail(String generatedEmail) { this.generatedEmail = generatedEmail; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}