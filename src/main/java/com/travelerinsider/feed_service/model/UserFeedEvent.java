package com.travelerinsider.feed_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFeedEvent {
    private UUID userId;
    private Instant eventTime;
    private String eventType;
    private String payload;
}
