package com.studybuddy.chatbot.service;

import com.studybuddy.chatbot.model.Conversation;
import com.studybuddy.chatbot.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;

    @Autowired
    public ConversationService(ConversationRepository conversationRepository, MongoTemplate mongoTemplate) {
        this.conversationRepository = conversationRepository;
    }

    public Conversation createNewConversation(Integer userId) {
        // Check if a conversation already exists for the given userId
        Optional<Conversation> existingConversation = conversationRepository.findByUserId(userId);

        if (existingConversation.isPresent()) {
            // Return the existing conversation if found
            return existingConversation.get();
        }

        // Create a new conversation if none exists
        Conversation newConversation = new Conversation();
        String generalContext = """
            Instructions: Provided is a conversation with timestamps. The user may ask questions that require context from the ongoing conversation.
            When a user question is detected, analyze the latest query and answer it using the relevant context provided up to that point.
            If the question relates to previously mentioned details (such as calendar events, meeting summaries, etc.), use that context to enhance your response.
            If the question is random or does not require any specific context, simply provide a direct and relevant answer.
            If answering the question requires additional clarification or missing context, either ask follow-up questions or explicitly mention what is needed to proceed.
            Ensure the length of your response is reasonable and relevant to the question at hand—do not overwhelm with unnecessary details.
            Your responses should be clear, conversational, precise, and aligned with the most recent data. Always prioritize accuracy and helpfulness, ensuring responses are focused on the context at hand.
            """;

        newConversation.setUserId(userId);
        Conversation.Message m = new Conversation.Message();
        m.setText(generalContext);
        newConversation.addMessage(m);

        return conversationRepository.save(newConversation);
    }


    public List<Conversation.Message> getConversationHistory(Integer userId) {
        Conversation conversation = conversationRepository.findByUserId(userId).orElse(null);
        return conversationRepository.findByUserId(userId)
                .map(Conversation::getMessages)
                .orElse(List.of());
    }



    public void addMessageToUser(Integer userId, Conversation.Message message) {
        // Retrieve the conversation or create a new one if it doesn't exist

        Conversation.Message tmp1 = new Conversation.Message();
        Conversation conversation = conversationRepository.findByUserId(userId)
                .orElse(new Conversation(userId,List.of(), LocalDateTime.now()));

        // Add the new message
        conversation.getMessages().add(message);
        conversation.setLastUpdated(LocalDateTime.now());

        // Save the updated conversation back to the repository
        conversationRepository.save(conversation);
    }
}
