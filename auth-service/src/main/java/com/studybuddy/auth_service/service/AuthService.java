package com.studybuddy.auth_service.service;

import com.studybuddy.auth_service.model.Session;
import com.studybuddy.auth_service.model.User;
import com.studybuddy.auth_service.repository.SessionRepository;
import com.studybuddy.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    public Session login(String username, String password) throws Exception {
        User user = (User) userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("User not found"));

        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            throw new Exception("Invalid credentials");
        }

        // Generate session token
        String token = UUID.randomUUID().toString();
        Session session = new Session();
        session.setToken(token);
        session.setUserId(user.getId());
        session.setExpiresAt(LocalDateTime.now().plusHours(4));

        sessionRepository.save(session);
        return session;
    }
}
