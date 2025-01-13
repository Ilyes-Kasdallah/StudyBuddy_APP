package com.studybuddy.auth_service.controller;

import com.studybuddy.auth_service.DTO.TokenRequest;
import com.studybuddy.auth_service.DTO.TokenValidationResponse;
import com.studybuddy.auth_service.model.Session;
import com.studybuddy.auth_service.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class SessionController {

    @Autowired
    private SessionRepository sessionRepository;

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody TokenRequest tokenRequest) {
        Session session = sessionRepository.findByToken(tokenRequest.getToken())
                .orElse(null);

        if (session == null || session.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        return ResponseEntity.ok(new TokenValidationResponse(true, session.getUserId()));
    }
}
