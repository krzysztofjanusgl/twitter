package com.hsbc.twitter.domain.listener;

import com.hsbc.twitter.domain.SubscriptionRepository;
import com.hsbc.twitter.domain.TimelineRepository;
import com.hsbc.twitter.domain.TweetRepository;
import com.hsbc.twitter.domain.event.SubscriptionCreatedEvent;
import com.hsbc.twitter.domain.event.SubscriptionDeletedEvent;
import com.hsbc.twitter.domain.model.Tweet;
import com.hsbc.twitter.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class SubscriptionEventListenerTest {
    private static final User bob = new User("bob");
    private static final User alice = new User("alice");
    private static final Tweet tweet = new Tweet(UUID.randomUUID(), bob, "test", Instant.now());

    @Mock private SubscriptionRepository subscriptionRepository;
    @Mock private TweetRepository tweetRepository;
    @Mock private TimelineRepository timelineRepository;

    @InjectMocks
    private SubscriptionEventListener listener;

    @Test
    void shouldCopyTweetsFromBobToAlice() {
        Mockito.when(subscriptionRepository.findSubscribers(bob)).thenReturn(Set.of(alice));
        Mockito.when(tweetRepository.findByUser(bob)).thenReturn(Set.of(tweet));

        listener.on(new SubscriptionCreatedEvent(alice, bob));

        Mockito.verify(timelineRepository).addTweets(alice, Set.of(tweet));
    }

    @Test
    void shouldNotCopyTweetsWhenSubscribedDoesNotHave() {
        Mockito.when(subscriptionRepository.findSubscribers(bob)).thenReturn(Set.of(alice));
        Mockito.when(tweetRepository.findByUser(bob)).thenReturn(Set.of());

        listener.on(new SubscriptionCreatedEvent(alice, bob));

        Mockito.verify(timelineRepository, Mockito.never()).addTweets(Mockito.any(User.class), Mockito.anySet());
    }

    @Test
    void shouldDeleteSubscriptionsBetweenAliceAndBobAndTweets() {
        Mockito.when(tweetRepository.findByUser(bob)).thenReturn(Set.of(tweet));

        listener.on(new SubscriptionDeletedEvent(alice, bob));

        Mockito.verify(timelineRepository).delete(alice, Set.of(tweet));
    }
}