package com.travelerinsider.feed_service.controller;


import com.travelerinsider.feed_service.model.UserFeedEvent;
import com.travelerinsider.feed_service.service.FeedService;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
@Slf4j
@Observed
public class FeedController {

    private final FeedService service;

    @PostMapping("/{userId}")
    public ResponseEntity<String> postEvent(@PathVariable UUID userId,
                                            @RequestParam String type,
                                            @RequestBody String payload) {
        service.addEvent(userId, type, payload);
        log.info("New feed posted for userId: {}, type: {}", userId, type);
        return ResponseEntity.status(HttpStatus.CREATED).body("Event added successfully");
    }

    @GetMapping("/{userId}")
    public List<UserFeedEvent> getFeed(@PathVariable UUID userId,
                                       @RequestParam(defaultValue = "20") int limit) {
        log.info("Get feed for userId: {}, limit: {}", userId, limit);
        return service.getLatest(userId, limit);
    }
}
