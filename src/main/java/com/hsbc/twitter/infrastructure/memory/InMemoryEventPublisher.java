package com.hsbc.twitter.infrastructure.memory;

import com.hsbc.twitter.domain.EventPublisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@AllArgsConstructor
@Component
public class InMemoryEventPublisher implements EventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public <T> void publish(Flux<T> events) {
         events.doOnNext(applicationEventPublisher::publishEvent)
                 .subscribe(event -> log.info("Event with payload: {}", event));
    }
}
