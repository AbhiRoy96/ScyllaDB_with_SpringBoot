package com.travelerinsider.feed_service.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.travelerinsider.feed_service.model.UserFeedEvent;
import io.micrometer.observation.annotation.Observed;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
@Observed
public class FeedRepository {

    private final CqlSession session;

    private PreparedStatement insertStmt;
    private PreparedStatement selectStmt;


    @PostConstruct
    public void init() {
        log.debug("Init repository");
        insertStmt = session.prepare(
                "INSERT INTO user_feed (user_id, event_time, event_type, payload) " +
                        "VALUES (?, ?, ?, ?)");

        log.debug("Init Insert statement");
        selectStmt = session.prepare(
                "SELECT user_id, event_time, event_type, payload " +
                        "FROM user_feed WHERE user_id = ? LIMIT ?"
        );
        log.debug("Init Select statement");
    }

    public void save(UserFeedEvent event) {
        log.debug("Save user feed event: {}", event.toString());
        session.execute(insertStmt.bind(
                event.getUserId(),
                event.getEventTime(),
                event.getEventType(),
                event.getPayload()
        ));
    }

    public List<UserFeedEvent> findLatest(UUID userId, int limit) {
        log.debug("Get latest feed for userId: {}, limit: {}", userId, limit);
        ResultSet rs = session.execute(selectStmt.bind(userId, limit));
        List<UserFeedEvent> events = new ArrayList<>();
        rs.forEach(row -> events.add(new UserFeedEvent(
                row.getUuid("user_id"),
                row.getInstant("event_time"),
                row.getString("event_type"),
                row.getString("payload")
        )));
        log.debug("Results returned: {}", events.size());
        return events;
    }
}
