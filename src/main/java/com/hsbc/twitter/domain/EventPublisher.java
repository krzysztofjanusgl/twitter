package com.hsbc.twitter.domain;

import reactor.core.publisher.Flux;

public interface EventPublisher {
    <T> void publish(Flux<T> events);
}
