package com.hsbc.twitter.domain;

import com.hsbc.twitter.domain.model.Tweet;
import com.hsbc.twitter.domain.model.User;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface TweetRepository {
    Mono<Tweet> save(Tweet tweet);
    Set<Tweet> findByUser(User user);
}
