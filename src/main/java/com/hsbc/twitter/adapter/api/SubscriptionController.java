package com.hsbc.twitter.adapter.api;

import com.hsbc.twitter.domain.model.User;
import com.hsbc.twitter.domain.provider.SubscriptionProvider;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/subscriptions/{subscriber}/{subscribed}")
@AllArgsConstructor
class SubscriptionController {
    private final SubscriptionProvider subscriptionProvider;

    @ResponseStatus(ACCEPTED)
    @PutMapping
    Mono<Boolean> add(@PathVariable final String subscriber, @PathVariable final String subscribed) {
        return subscriptionProvider.subscribe(new User(subscriber), new User(subscribed));
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping
    Mono<Void> delete(@PathVariable final String subscriber, @PathVariable final String subscribed) {
        return subscriptionProvider.delete(new User(subscriber), new User(subscribed));
    }
}
