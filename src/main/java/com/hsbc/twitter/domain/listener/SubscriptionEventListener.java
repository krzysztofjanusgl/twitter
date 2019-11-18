package com.hsbc.twitter.domain.listener;

import com.hsbc.twitter.domain.SubscriptionRepository;
import com.hsbc.twitter.domain.TimelineRepository;
import com.hsbc.twitter.domain.TweetRepository;
import com.hsbc.twitter.domain.event.SubscriptionCreatedEvent;
import com.hsbc.twitter.domain.event.SubscriptionDeletedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class SubscriptionEventListener {
    private final SubscriptionRepository subscriptionRepository;
    private final TweetRepository tweetRepository;
    private final TimelineRepository timelineRepository;

    @EventListener
    public void on(final SubscriptionCreatedEvent event) {
        var subscriptions = subscriptionRepository.findSubscribers(event.getSubscribed());
        var tweets = tweetRepository.findByUser(event.getSubscribed());

        if (!tweets.isEmpty()) {
            subscriptions.forEach(user -> {
                log.info("Copy tweets from user: {} to user: {}", event.getSubscribed(), user);
                timelineRepository.addTweets(user, tweets);
            });
        }
    }

    @EventListener
    public void on(final SubscriptionDeletedEvent event) {
        var tweets = tweetRepository.findByUser(event.getSubscribed());
        timelineRepository.delete(event.getSubscriber(), tweets);
        log.info("Removed tweets on user timeline: {} from user: {}", event.getSubscriber(), event.getSubscribed());
    }
}
