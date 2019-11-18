package com.hsbc.twitter.domain.provider;

import com.hsbc.twitter.domain.EventPublisher;
import com.hsbc.twitter.domain.TimelineRepository;
import com.hsbc.twitter.domain.TweetRepository;
import com.hsbc.twitter.domain.event.TweetCreatedEvent;
import com.hsbc.twitter.domain.model.Tweet;
import com.hsbc.twitter.domain.model.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Component
public class TweetProvider {
    private final TweetRepository tweetRepository;
    private final TimelineRepository timelineRepository;
    private final EventPublisher eventPublisher;

    public Mono<TweetCreatedEvent> create(final String message, final User user) {
        return tweetRepository.save(new Tweet(UUID.randomUUID(), user, message, Instant.now()))
                .map(TweetCreatedEvent::new)
                .doOnNext(event -> eventPublisher.publish(Flux.just(event)));
    }

    public Set<Tweet> wall(User user) {
        return tweetRepository.findByUser(user);
    }

    public Flux<Tweet> timeline(User user, Pageable pageable) {
        return Flux.fromIterable(timelineRepository.findByUser(user, pageable));
    }
}
