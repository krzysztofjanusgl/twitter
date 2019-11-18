package com.hsbc.twitter.infrastructure.memory;

import com.hsbc.twitter.domain.SubscriptionRepository;
import com.hsbc.twitter.domain.exception.SubscriptionException;
import com.hsbc.twitter.domain.model.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
class InMemorySubscriptionRepository implements SubscriptionRepository {
    private final Map<String, Set<User>> SUBSCRIPTIONS = new ConcurrentHashMap<>();

    @Override
    public Mono<Boolean> subscribe(User subscriber, User subscribed) {
        return Mono.just(findSubscribers(subscribed))
                .map(subscriptions -> Tuples.of(subscriptions, subscriptions.add(subscriber)))
                .filter(Tuple2::getT2)
                .switchIfEmpty(Mono.error(new SubscriptionException("Subscription already exists")))
                .doOnSuccess(tuples -> SUBSCRIPTIONS.put(subscribed.getName(), tuples.getT1()))
                .map(Tuple2::getT2);
    }

    @Override
    public Mono<Boolean> delete(User subscriber, User subscribed) {
        return Mono.just(findSubscribers(subscribed))
                .map(subscriptions -> subscriptions.removeIf(user -> user.getName().equals(subscriber.getName())));
    }

    @Override
    public Set<User> findSubscribers(User user) {
        return Optional.ofNullable(SUBSCRIPTIONS.get(user.getName()))
                .orElse(new LinkedHashSet<>());
    }
}
