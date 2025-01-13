package com.studybuddy.chatbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studybuddy.chatbot.dto.GeminiResponse;
import com.studybuddy.chatbot.model.Conversation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {
    @Value("${api.key}")
    private String apiKey ;

    private final WebClient webClient;

    public GeminiService() {
        this.webClient = WebClient.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent");
    }



    private String extractResponseText(String response) {
        // Simplified extraction, use JSON parser if needed
        return response != null ? response.split("\"text\":\"")[1].split("\"")[0] : "No response";
    }
    public String generateContext(List<Conversation.Message> messages) {
        StringBuilder contextBuilder = new StringBuilder();
        for (Conversation.Message message : messages) {
            contextBuilder.append(message.getTimestamp()).append(": ").append(message.getText()).append("\n");
        }
        return contextBuilder.toString();
    }

    public String getBotResponse(String context) {
      //  System.err.println(context);
        String response = webClient.post()
                .uri("?key=" + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue("{\"contents\": [{\"parts\": [{\"text\": \"" + context + "\"}]}]}")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.err.println(response);
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            GeminiResponse res = objectMapper.readValue(response, GeminiResponse.class);
           // System.err.println(res.getCandidates());
            return res.getCandidates().get(0).getContent().getParts().get(0).getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "no response";

       // return extractResponseText(response);


    }

}
