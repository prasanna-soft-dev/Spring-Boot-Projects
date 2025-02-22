package com.devsync.DevSync.Controller;

import com.devsync.DevSync.Service.KafkaProducerService;
import com.devsync.DevSync.Util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")


public class DevSyncController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/forum")
    public ResponseEntity<String> joinForum(@RequestHeader("Authorization") String token) {
        // ✅ Extract token from "Bearer <TOKEN>"
        String jwt = token.replace("Bearer ", "");

        // ✅ Verify JWT
        String email = jwtUtil.extractEmail(jwt);
        if (email == null || !jwtUtil.validateAccessToken(jwt)) {
            return ResponseEntity.status(401).body("Invalid Token");
        }

        return ResponseEntity.ok("Welcome to DevSync Forum, " + email + "! You can now send messages.");
    }

    // ✅ Step 2: Send Message in Forum After Verification
    @PostMapping("/forum/send")
    public ResponseEntity<String> sendMessage(
            @RequestHeader("Authorization") String token,
            @RequestBody String message) {

        String jwt = token.replace("Bearer ", "");
        String senderEmail = jwtUtil.extractEmail(jwt);

        if (senderEmail == null || !jwtUtil.validateAccessToken(jwt)) {
            return ResponseEntity.status(401).body("Invalid Token");
        }

        // ✅ Send message to Kafka or store in DB
        kafkaProducerService.sendMessage(senderEmail + ": " + message);

        return ResponseEntity.ok("Message sent by " + senderEmail);
    }
}
