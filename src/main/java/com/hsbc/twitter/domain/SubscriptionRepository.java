package com.hsbc.twitter.domain;

import com.hsbc.twitter.domain.model.User;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface SubscriptionRepository {
    Mono<Boolean> subscribe(User subscriber, User subscribed);
    Mono<Boolean> delete(User subscriber, User subscribed);
    Set<User> findSubscribers(User user);
}
