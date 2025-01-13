package com.studybuddy.chatbot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "conversation") // Matches your MongoDB collection name
public class Conversation {
    @Id
    private String id; // Unique conversation ID
    @Field("user_id")
    private Integer userId; // Matches the user ID


    private List<Message> messages; // List of messages in the conversation

    @Field("last_updated")
    private LocalDateTime lastUpdated; // Last message timestamp

    // Default Constructor
    public Conversation() {
        this.messages = new ArrayList<>();
        this.lastUpdated = LocalDateTime.now();
    }

    // Parameterized Constructor
    public Conversation( Integer userId, List<Message> messages, LocalDateTime lastUpdated) {
        this.userId = userId;
        this.messages = messages;
        this.lastUpdated = lastUpdated != null ? lastUpdated : LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    // Inner class for Message
    public static class Message {
        private String text; // Message text
        private boolean isUserMessage; // Indicates if the message is from the user
        private LocalDateTime timestamp; // Timestamp for the message

        // Default Constructor
        public Message() {
            this.timestamp = LocalDateTime.now();
        }

        // Parameterized Constructor
        public Message(String text, boolean isUserMessage) {
            this.text = text;
            this.isUserMessage = isUserMessage;
            this.timestamp = LocalDateTime.now();
        }

        // Getters and Setters
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean isUserMessage() {
            return isUserMessage;
        }

        public void setUserMessage(boolean isUserMessage) {
            this.isUserMessage = isUserMessage;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
    }
}
