package com.studybuddy.chatbot.controller;

import com.studybuddy.chatbot.dto.ChatRequest;
import com.studybuddy.chatbot.dto.UserId;
import com.studybuddy.chatbot.model.Conversation;
import com.studybuddy.chatbot.service.ConversationService;
import com.studybuddy.chatbot.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chatbot")
public class ConversationController {

    private final ConversationService conversationService;
    private final GeminiService geminiService;

    @Autowired
    public ConversationController(ConversationService conversationService, GeminiService geminiService) {
        this.conversationService = conversationService;
        this.geminiService = geminiService;
    }

    // POST endpoint for creating a new conversation when a user registers
    @PostMapping("/add-conversation")
    public ResponseEntity<String> startNewConversation(@RequestBody UserId userId) {
        // Create a new empty conversation document for the user
        conversationService.createNewConversation(userId.getUserId());
        return ResponseEntity.ok("New conversation started for user: " + userId);
    }

    @PostMapping("/send/{userId}")
    public ResponseEntity<String> sendMessage(
            @PathVariable Integer userId,
            @RequestBody ChatRequest req) {
        // Create a message object
        Conversation.Message message = new Conversation.Message();
        message.setText(req.getQuestion());
        message.setTimestamp(LocalDateTime.now());
        message.setUserMessage(true);


         //Retrieve user conversation history
        List<Conversation.Message> history = conversationService.getConversationHistory(userId);
         //Add current message to history
        history.add(message);


        // Generate context for the LLM (conversation history + current question)

        String context = geminiService.generateContext(history);

        // Send request to Gemini API
        String botResponse = geminiService.getBotResponse(context);
        // Save user message and bot response in conversation
        conversationService.addMessageToUser(userId, message);
        conversationService.addMessageToUser(userId, new Conversation.Message(botResponse, false));

        return ResponseEntity.ok(botResponse);
    }
}
