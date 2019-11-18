package com.hsbc.twitter.domain.listener;

import com.hsbc.twitter.domain.SubscriptionRepository;
import com.hsbc.twitter.domain.TimelineRepository;
import com.hsbc.twitter.domain.event.TweetCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class TweetEventListener {
    private final SubscriptionRepository subscriptionRepository;
    private final TimelineRepository timelineRepository;

    @EventListener
    public void on(TweetCreatedEvent event) {
        timelineRepository.addTweet(event.getTweet().getUser(), event.getTweet());
        subscriptionRepository.findSubscribers(event.getTweet().getUser())
                .stream()
                .peek(user -> log.info("Add tweet to user timeline: {}", user))
                .forEach(user -> timelineRepository.addTweet(user, event.getTweet()));
    }
}
