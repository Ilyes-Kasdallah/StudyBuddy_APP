package com.studdybuddy.calendar.repository;


import com.studdybuddy.calendar.model.UserEvents;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserEventsRepository extends MongoRepository<UserEvents, String> {

    Optional<UserEvents> findByUserId(Integer userId); // Custom query to find by userId
}
