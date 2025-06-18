package com.whatsapp.chatbot.controller;

import com.whatsapp.chatbot.service.WhatsAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/whatsapp")
@RequiredArgsConstructor
public class WhatsAppController {

    private final WhatsAppService whatsAppService;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> payload) {
        try {
            log.info("Received webhook: {}", payload);
            
            // Extract message details from payload
            Map<String, Object> entry = (Map<String, Object>) ((java.util.List) payload.get("entry")).get(0);
            Map<String, Object> changes = (Map<String, Object>) ((java.util.List) entry.get("changes")).get(0);
            Map<String, Object> value = (Map<String, Object>) changes.get("value");
            Map<String, Object> messages = (Map<String, Object>) ((java.util.List) value.get("messages")).get(0);
            
            String from = (String) messages.get("from");
            String body = (String) ((Map<String, Object>) messages.get("text")).get("body");
            
            // Process the message
            whatsAppService.processMessage(from, body);
            
            return ResponseEntity.ok("Message processed successfully");
        } catch (Exception e) {
            log.error("Error processing webhook: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error processing message");
        }
    }

    @GetMapping("/webhook")
    public ResponseEntity<String> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.verify_token") String token,
            @RequestParam("hub.challenge") String challenge) {
        
        // Verify the webhook token
        if ("subscribe".equals(mode) && "your_verify_token".equals(token)) {
            return ResponseEntity.ok(challenge);
        }
        
        return ResponseEntity.badRequest().body("Invalid verification token");
    }
} 