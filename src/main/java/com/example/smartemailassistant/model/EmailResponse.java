package com.example.smartemailassistant.model;

public class EmailResponse {
    private String generatedEmail;
    private String subject;
    private boolean success;
    private String provider;
    private String errorMessage;

    // Constructors
    public EmailResponse() {
        this.success = false; // Default to false for safety
    }

    public EmailResponse(String generatedEmail, String subject, boolean success, String provider) {
        this.generatedEmail = generatedEmail;
        this.subject = subject;
        this.success = success;
        this.provider = provider;
    }

    // Getters and Setters
    public String getGeneratedEmail() {
        return generatedEmail != null ? generatedEmail : "";
    }
    public void setGeneratedEmail(String generatedEmail) {
        this.generatedEmail = generatedEmail;
    }

    public String getSubject() {
        return subject != null ? subject : "No Subject";
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getProvider() {
        return provider != null ? provider : "Unknown";
    }
    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getErrorMessage() {
        return errorMessage != null ? errorMessage : "";
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "EmailResponse{" +
                "success=" + success +
                ", provider='" + provider + '\'' +
                ", subject='" + subject + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}