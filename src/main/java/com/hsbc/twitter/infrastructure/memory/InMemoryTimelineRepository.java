package com.hsbc.twitter.infrastructure.memory;

import com.hsbc.twitter.domain.TimelineRepository;
import com.hsbc.twitter.domain.model.Tweet;
import com.hsbc.twitter.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

@Component
class InMemoryTimelineRepository implements TimelineRepository {
    private static final Map<String, Set<Tweet>> TIMELINE = new ConcurrentHashMap<>();

    @Override
    public void addTweet(User user, Tweet tweet) {
        var timeline = get(user);
        timeline.add(tweet);
        TIMELINE.put(user.getName(), timeline);
    }

    @Override
    public void addTweets(User user, Set<Tweet> tweets) {
        var timeline = get(user);
        timeline.addAll(tweets);
        TIMELINE.put(user.getName(), timeline);
    }

    @Override
    public Page<Tweet> findByUser(User user, Pageable pageable) {
        var tweets = get(user);
        return new PageImpl<>(PageableUtil.slice(tweets, pageable), pageable, tweets.size());
    }

    @Override
    public void delete(User user, Set<Tweet> tweetsToRemove) {
        get(user).removeAll(tweetsToRemove);
    }

    private Set<Tweet> get(User user) {
        return Optional.ofNullable(TIMELINE.get(user.getName()))
                .orElse(new TreeSet<>(Comparator.comparing(Tweet::getCreatedAt).reversed()));
    }
}
