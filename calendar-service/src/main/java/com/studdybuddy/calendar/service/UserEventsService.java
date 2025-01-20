package com.studdybuddy.calendar.service;

import com.studdybuddy.calendar.dto.EventDTO;
import com.studdybuddy.calendar.dto.UserEventsDTO;
import com.studdybuddy.calendar.model.UserEvents;
import com.studdybuddy.calendar.repository.UserEventsRepository;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserEventsService {

    private final UserEventsRepository userEventsRepository;

    public UserEventsService(UserEventsRepository userEventsRepository) {
        this.userEventsRepository = userEventsRepository;
    }
    public UserEvents createNewUserEvent(Integer userId) {
        // Check if a conversation already exists for the given userId
        Optional<UserEvents> existingUserEvent = userEventsRepository.findByUserId(userId);

        if (existingUserEvent.isPresent()) {
            // Return the existing conversation if found
            return existingUserEvent.get();
        }

        // Create a new conversation if none exists
        UserEvents newUserEvent = new UserEvents(userId,List.of());


        newUserEvent.setUserId(userId);


        return userEventsRepository.save(newUserEvent);
    }

    // Retrieve events by userId
    public UserEventsDTO getUserEvents(Integer userId) {
        Optional<UserEvents> userEventsOptional = userEventsRepository.findByUserId(userId);
        if (userEventsOptional.isEmpty()) {
            throw new RuntimeException("No events found for user: " + userId);
        }

        UserEvents userEvents = userEventsOptional.get();
        return mapToUserEventsDTO(userEvents);
    }

    // Add a new event for a user
    public UserEventsDTO addEvent(Integer userId, EventDTO eventDTO) {
        Optional<UserEvents> userEventsOptional = userEventsRepository.findByUserId(userId);
        UserEvents userEvents = userEventsOptional.orElseGet(() -> createNewUserEvent(userId));

        UserEvents.Event event = mapToEvent(eventDTO);
        userEvents.getEvents().add(event);
        userEventsRepository.save(userEvents);

        return mapToUserEventsDTO(userEvents);
    }


    // Delete an event for a user by title
    public UserEventsDTO deleteEvent(Integer userId, String title) {
        Optional<UserEvents> userEventsOptional = userEventsRepository.findByUserId(userId);
        if (userEventsOptional.isEmpty()) {
            throw new RuntimeException("No events found for user: " + userId);
        }

        UserEvents userEvents = userEventsOptional.get();
        List<UserEvents.Event> updatedEvents = userEvents.getEvents().stream()
                .filter(event -> !event.getTitle().equals(title))
                .collect(Collectors.toList());

        userEvents.setEvents(updatedEvents);
        userEventsRepository.save(userEvents);

        return mapToUserEventsDTO(userEvents);
    }

    // Mapping from UserEvents to UserEventsDTO
    private UserEventsDTO mapToUserEventsDTO(UserEvents userEvents) {
        UserEventsDTO dto = new UserEventsDTO();
        dto.setUserId(userEvents.getUserId());
        dto.setEvents(userEvents.getEvents().stream().map(this::mapToEventDTO).collect(Collectors.toList()));
        return dto;
    }

    // Mapping from Event to EventDTO
    private EventDTO mapToEventDTO(UserEvents.Event event) {
        EventDTO dto = new EventDTO();
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setStartDate(event.getStartDate());
        dto.setEndDate(event.getEndDate());
        return dto;
    }

    // Mapping from EventDTO to Event
    private UserEvents.Event mapToEvent(EventDTO eventDTO) {
        UserEvents.Event event = new UserEvents.Event();
        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setStartDate(eventDTO.getStartDate());
        event.setEndDate(eventDTO.getEndDate());
        return event;
    }
}
