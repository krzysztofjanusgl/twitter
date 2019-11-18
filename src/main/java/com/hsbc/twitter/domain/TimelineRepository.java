package com.hsbc.twitter.domain;

import com.hsbc.twitter.domain.model.Tweet;
import com.hsbc.twitter.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface TimelineRepository {
    void addTweet(User user, Tweet tweet);
    void addTweets(User user, Set<Tweet> tweets);
    Page<Tweet> findByUser(final User user, Pageable pageable);
    void delete(User user, Set<Tweet> tweetsToRemove);
}
