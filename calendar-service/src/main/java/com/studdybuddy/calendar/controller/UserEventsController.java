package com.studdybuddy.calendar.controller;

import com.studdybuddy.calendar.dto.EventDTO;
import com.studdybuddy.calendar.dto.UserEventsDTO;
import com.studdybuddy.calendar.dto.UserId;
import com.studdybuddy.calendar.service.UserEventsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-events")
public class UserEventsController {

    private final UserEventsService userEventsService;

    public UserEventsController(UserEventsService userEventsService) {
        this.userEventsService = userEventsService;
    }
    @PostMapping("/add")
    public ResponseEntity<String> addNewUserEvnets(@RequestBody UserId userId) {
        // Create a new empty conversation document for the user
        userEventsService.createNewUserEvent(userId.getUserId());
        return ResponseEntity.ok("New Calendar started for user: " + userId);
    }
    // Get all events for a user by userId
    @GetMapping("/{userId}")
    public ResponseEntity<UserEventsDTO> getUserEvents(@PathVariable Integer userId) {
        try {
            UserEventsDTO userEventsDTO = userEventsService.getUserEvents(userId);
            return new ResponseEntity<>(userEventsDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Add an event for a user
    @PostMapping("/{userId}")
    public ResponseEntity<UserEventsDTO> addEvent(@PathVariable Integer userId, @RequestBody EventDTO eventDTO) {
        try {
            UserEventsDTO userEventsDTO = userEventsService.addEvent(userId, eventDTO);
            return new ResponseEntity<>(userEventsDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Delete an event by title for a user
    @DeleteMapping("/{userId}/{title}")
    public ResponseEntity<UserEventsDTO> deleteEvent(@PathVariable Integer userId, @PathVariable String title) {
        try {
            UserEventsDTO userEventsDTO = userEventsService.deleteEvent(userId, title);
            return new ResponseEntity<>(userEventsDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
