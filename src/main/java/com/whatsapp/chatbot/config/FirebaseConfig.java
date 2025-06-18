package com.whatsapp.chatbot.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.service.account.base64:#{null}}")
    private String firebaseServiceAccountBase64;

    @Value("${firebase.database.url}")
    private String firebaseDatabaseUrl;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            InputStream serviceAccount;
            
            if (firebaseServiceAccountBase64 != null) {
                // Use base64 encoded service account from environment variable
                byte[] decodedBytes = Base64.getDecoder().decode(firebaseServiceAccountBase64);
                serviceAccount = new ByteArrayInputStream(decodedBytes);
            } else {
                // Fallback to local file (for development)
                serviceAccount = getClass().getResourceAsStream("/firebase-service-account.json");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(firebaseDatabaseUrl)
                    .build();

            return FirebaseApp.initializeApp(options);
        }
        return FirebaseApp.getInstance();
    }
} 