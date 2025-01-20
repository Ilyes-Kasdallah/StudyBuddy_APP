package com.studdybuddy.calendar.dto;

import java.util.List;

public class UserEventsDTO {

    private Integer userId; // Identifier for the user
    private List<EventDTO> events; // List of event DTOs

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<EventDTO> getEvents() {
        return events;
    }

    public void setEvents(List<EventDTO> events) {
        this.events = events;
    }
}
