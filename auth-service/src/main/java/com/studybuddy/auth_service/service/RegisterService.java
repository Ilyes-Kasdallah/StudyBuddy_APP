package com.studybuddy.auth_service.service;

import com.studybuddy.auth_service.DTO.RegisterRequest;
import com.studybuddy.auth_service.model.User;
import com.studybuddy.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    @Autowired
    private UserRepository userRepository;

    public User register(RegisterRequest registerRequest) throws Exception {
        // Check if username already exists
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new Exception("Username already exists");
        }

        // Hash the password
        String hashedPassword = new BCryptPasswordEncoder().encode(registerRequest.getPassword());

        // Save the user
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(hashedPassword);
        user.setRole(registerRequest.getRole());
        userRepository.save(user);
        return user;
    }
}
