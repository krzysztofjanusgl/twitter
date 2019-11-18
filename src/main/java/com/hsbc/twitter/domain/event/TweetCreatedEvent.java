package com.hsbc.twitter.domain.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.hsbc.twitter.domain.model.Tweet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class TweetCreatedEvent extends Event {
    private final Tweet tweet;

    @JsonCreator
    private static TweetCreatedEvent create(Tweet tweet) {
        return new TweetCreatedEvent(tweet);
    }
}
