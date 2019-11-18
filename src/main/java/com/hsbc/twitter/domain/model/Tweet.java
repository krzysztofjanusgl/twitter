package com.hsbc.twitter.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
public class Tweet {
    private final UUID id;
    private final User user;
    private final String message;
    private final Instant createdAt;

    @JsonCreator
    private static Tweet create(UUID id, User user, String message, Instant createdAt) {
        return new Tweet(id, user, message, createdAt);
    }
}
