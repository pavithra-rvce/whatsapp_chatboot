# WhatsApp Chatbot with Spring Boot and Firebase

This is a WhatsApp chatbot built using Spring Boot, integrated with Firebase for data storage and deployed on Render.

## Prerequisites

- Java 11 or higher
- Maven
- Firebase account
- Twilio account with WhatsApp API access
- Render account

## Setup Instructions

1. Clone the repository
2. Set up Firebase:
   - Create a new Firebase project
   - Enable Realtime Database
   - Download the service account key (firebase-service-account.json)
   - Place the service account key in `src/main/resources/`

3. Set up Twilio:
   - Create a Twilio account
   - Get your Account SID and Auth Token
   - Set up a WhatsApp sandbox or business account

4. Configure environment variables:
   ```bash
   TWILIO_ACCOUNT_SID=your_account_sid
   TWILIO_AUTH_TOKEN=your_auth_token
   TWILIO_WHATSAPP_NUMBER=your_whatsapp_number
   FIREBASE_DATABASE_URL=your_firebase_database_url
   ```

5. Build the project:
   ```bash
   mvn clean install
   ```

6. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## Deployment on Render

1. Create a new Web Service on Render
2. Connect your GitHub repository
3. Configure the following:
   - Build Command: `mvn clean install`
   - Start Command: `java -jar target/whatsapp-chatbot-0.0.1-SNAPSHOT.jar`
   - Add all required environment variables

4. Deploy the service

## Webhook Configuration

1. Set up your webhook URL in the Twilio console:
   - URL: `https://your-render-app.onrender.com/api/whatsapp/webhook`
   - HTTP Method: POST

2. Configure the webhook in your WhatsApp Business API settings

## Features

- WhatsApp message processing
- Firebase integration for message storage
- Customizable responses
- Webhook verification
- Error handling and logging

## Security Considerations

- Keep your Firebase service account key secure
- Never commit sensitive credentials to version control
- Use environment variables for all sensitive information
- Implement proper webhook verification

## Contributing

Feel free to submit issues and enhancement requests! 