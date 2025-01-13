package com.studybuddy.chatbot.controller;

import com.studybuddy.chatbot.model.Conversation;
import com.studybuddy.chatbot.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class GetAllConversationsController {

    private final ConversationRepository conversationRepository;

    @Autowired
    public GetAllConversationsController(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    // Endpoint to get all conversations
    @GetMapping("/api/conversations")
    public ResponseEntity<List<Conversation>> getAllConversations() {
        List<Conversation> conversations = conversationRepository.findAll();
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/api/conversations/{userId}")
    public ResponseEntity<Conversation> getConversationByUserId(@PathVariable Integer userId) {
        // Find the conversation that matches the userId
        Optional<Conversation> conversation = conversationRepository.findByUserId(userId);

        // If no conversation is found, return 404 Not Found
        if (conversation.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Return the conversation with a 200 OK status
        return ResponseEntity.ok(conversation.get());
    }

}
