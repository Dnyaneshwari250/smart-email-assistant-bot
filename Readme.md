# Smart Email Assistant

A Spring Boot application that generates professional emails using Google Gemini AI.

## Features

- Generate professional emails with AI
- Multiple tone options (Formal, Casual, Friendly, Professional)
- Customizable recipients and subjects
- RESTful API
- Deployable on Railway

## API Endpoints

- `GET /` - Welcome page
- `GET /health` - Health check
- `POST /generate` - Generate email

### Generate Email Request

```json
{
  "prompt": "Request a meeting to discuss project timeline",
  "tone": "FORMAL",
  "recipient": "Project Manager",
  "subjectHint": "Meeting Request"
}