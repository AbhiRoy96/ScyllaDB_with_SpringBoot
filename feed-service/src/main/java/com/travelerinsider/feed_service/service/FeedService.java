package com.travelerinsider.feed_service.service;

import com.travelerinsider.feed_service.model.UserFeedEvent;
import com.travelerinsider.feed_service.repository.FeedRepository;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Observed
public class FeedService {

    private final FeedRepository repo;

    public void addEvent(UUID userId, String eventType, String payload) {
        UserFeedEvent event = new UserFeedEvent(
                userId,
                Instant.now(),
                eventType,
                payload
        );
        log.debug(event.toString());
        repo.save(event);
    }

    public List<UserFeedEvent> getLatest(UUID userId, int limit) {
        log.debug("Get latest feed for userId: {}, limit: {}", userId, limit);
        return repo.findLatest(userId, limit);
    }
}
