package com.whatsapp.chatbot.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.twilio.Twilio;
import com.twilio.rest.messaging.v1.session.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class WhatsAppService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.number}")
    private String whatsappNumber;

    private final FirebaseDatabase firebaseDatabase;

    public WhatsAppService(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public void processMessage(String from, String body) {
        // Store message in Firebase
        DatabaseReference messagesRef = firebaseDatabase.getReference("messages");
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("from", from);
        messageData.put("body", body);
        messageData.put("timestamp", System.currentTimeMillis());
        messagesRef.push().setValueAsync(messageData);

        // Process message and send response
        String response = generateResponse(body);
        sendMessage(from, response);
    }

    private String generateResponse(String message) {
        // Add your message processing logic here
        message = message.toLowerCase();
        if (message.contains("hello") || message.contains("hi")) {
            return "Hello! How can I help you today?";
        } else if (message.contains("help")) {
            return "I can help you with:\n1. Information\n2. Support\n3. Queries\nJust ask me anything!";
        } else {
            return "I'm not sure how to help with that. Type 'help' to see what I can do!";
        }
    }

    public void sendMessage(String to, String body) {
        try {
            Message.creator(
                    new com.twilio.type.PhoneNumber("whatsapp:" + to),
                    new com.twilio.type.PhoneNumber("whatsapp:" + whatsappNumber),
                    body)
                    .create();
        } catch (Exception e) {
            log.error("Error sending WhatsApp message: {}", e.getMessage());
        }
    }
} 