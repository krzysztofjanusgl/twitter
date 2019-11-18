package com.hsbc.twitter.domain.provider;

import com.hsbc.twitter.domain.EventPublisher;
import com.hsbc.twitter.domain.SubscriptionRepository;
import com.hsbc.twitter.domain.event.SubscriptionCreatedEvent;
import com.hsbc.twitter.domain.event.SubscriptionDeletedEvent;
import com.hsbc.twitter.domain.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Flux.just;

@Component
@AllArgsConstructor
public class SubscriptionProvider {
    private final SubscriptionRepository subscriptionRepository;
    private final EventPublisher eventPublisher;

    public Mono<Boolean> subscribe(final User subscriber, final User subscribed) {
        return subscriptionRepository.subscribe(subscriber, subscribed)
                .doOnSuccess(aBoolean -> eventPublisher.publish(just(new SubscriptionCreatedEvent(subscriber, subscribed))));
    }

    public Mono<Void> delete(final User subscriber, final User subscribed) {
        return subscriptionRepository.delete(subscriber, subscribed)
                .doOnSuccess(aVoid -> eventPublisher.publish(just(new SubscriptionDeletedEvent(subscriber, subscribed))))
                .then();
    }
}
