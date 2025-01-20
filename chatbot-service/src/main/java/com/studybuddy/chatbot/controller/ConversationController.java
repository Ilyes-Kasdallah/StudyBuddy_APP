package com.studybuddy.chatbot.controller;

import com.studybuddy.chatbot.dto.ChatRequest;
import com.studybuddy.chatbot.dto.ChatResponse;
import com.studybuddy.chatbot.dto.UserEventsResponse;
import com.studybuddy.chatbot.dto.UserId;
import com.studybuddy.chatbot.model.Conversation;
import com.studybuddy.chatbot.service.ConversationService;
import com.studybuddy.chatbot.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
    public ResponseEntity<ChatResponse> sendMessage(
            @PathVariable Integer userId,
            @RequestBody ChatRequest req) {
        // Create a message object
        Conversation.Message message = new Conversation.Message();
        message.setText(req.getQuestion());
        message.setTimestamp(LocalDateTime.now());
        message.setUserMessage(true);

        System.err.println(message);
         //Retrieve user conversation history
        List<Conversation.Message> history = conversationService.getConversationHistory(userId);

        // Fetch user events from the external service
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:5000/api/user-events/" + userId;
        UserEventsResponse userEvents = restTemplate.getForObject(url, UserEventsResponse.class);

        // Append user events to the conversation history as a message
        if (userEvents != null && userEvents.getEvents() != null && !userEvents.getEvents().isEmpty()) {
            StringBuilder eventsText = new StringBuilder("events:\n");
            for (UserEventsResponse.Event event : userEvents.getEvents()) {
                eventsText.append(String.format("Title: %s , Start: %s , End: %s\n",
                        event.getTitle(), event.getStartDate(), event.getEndDate()));
            }

            System.err.println(eventsText.toString());

            // Create a message for the events and add it to history
            Conversation.Message eventsMessage = new Conversation.Message();
            eventsMessage.setText(eventsText.toString());
            eventsMessage.setTimestamp(LocalDateTime.now());
            eventsMessage.setUserMessage(false);  // Bot message
            history.add(eventsMessage);
        }
         //Add current message to history
        history.add(message);


        // Generate context for the LLM (conversation history + current question)

        String context = geminiService.generateContext(history);

        // Send request to Gemini API
        String botResponse = geminiService.getBotResponse(context);
        // Save user message and bot response in conversation
        conversationService.addMessageToUser(userId, message);
        conversationService.addMessageToUser(userId, new Conversation.Message(botResponse, false));
        ChatResponse response =new ChatResponse(botResponse);
        return ResponseEntity.ok(response);
    }
}
