package com.hsbc.twitter.domain.event;

import java.time.Instant;

public abstract class Event {
    private final Long timestamp = Instant.now().toEpochMilli();
}
