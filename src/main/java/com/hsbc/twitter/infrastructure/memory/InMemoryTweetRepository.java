package com.hsbc.twitter.infrastructure.memory;

import com.hsbc.twitter.domain.TweetRepository;
import com.hsbc.twitter.domain.model.Tweet;
import com.hsbc.twitter.domain.model.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

@Component
class InMemoryTweetRepository implements TweetRepository {
    private static final Map<String, Set<Tweet>> TWEETS = new ConcurrentHashMap<>();

    @Override
    public Mono<Tweet> save(Tweet tweet) {
        return Mono.just(tweet)
                .doOnNext(tweet1 -> {
                    var tweets = findByUser(tweet.getUser());
                    tweets.add(tweet);
                    TWEETS.put(tweet.getUser().getName(), tweets);
                });
    }

    @Override
    public Set<Tweet> findByUser(User user) {
        return Optional.ofNullable(TWEETS.get(user.getName()))
                .orElse(new TreeSet<>(Comparator.comparing(Tweet::getCreatedAt).reversed()));
    }
}
