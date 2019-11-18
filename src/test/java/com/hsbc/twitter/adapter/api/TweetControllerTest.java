package com.hsbc.twitter.adapter.api;

import com.hsbc.twitter.domain.TimelineRepository;
import com.hsbc.twitter.domain.TweetRepository;
import com.hsbc.twitter.domain.event.TweetCreatedEvent;
import com.hsbc.twitter.domain.model.Tweet;
import com.hsbc.twitter.domain.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest
class TweetControllerTest {
    private static final String message = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's";
    private static final ParameterizedTypeReference<Map<String, Object>> reference = new ParameterizedTypeReference<>() {};

    @MockBean
    private TweetRepository tweetRepository;

    @MockBean
    private TimelineRepository timelineRepository;

    @Autowired
    WebTestClient client;

    @Test
    void shouldCreateTweet() {
        Mockito.when(tweetRepository.save(Mockito.any(Tweet.class)))
                .thenReturn(Mono.just(example("alice", message)));

        final Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("userName", "alice");
        requestBody.put("message", message);

        client.post()
                .uri("/tweets")
                .body(Mono.just(requestBody), reference)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TweetCreatedEvent.class)
                .value(event -> {
                    final Tweet tweet = event.getTweet();
                    final User user = tweet.getUser();
                    Assertions.assertEquals("alice", user.getName());
                    Assertions.assertEquals(message, tweet.getMessage());
                });
    }

    @Test
    void shouldValidateTweetMessageLengthMin() {
        final Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("userName", "alice");
        requestBody.put("message", "small");
        client.post()
                .uri("/tweets")
                .body(Mono.just(requestBody), reference)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldValidateTweetMessageLengthMax() {
        final Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("userName", "alice");
        requestBody.put("message", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry'sLorem Ipsum is simply dummy text");
        client.post()
                .uri("/tweets")
                .body(Mono.just(requestBody), reference)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnWall() {
        final List<Tweet> list = List.of(
                example("alice", "a"),
                example("alice", "b")
        );

        when(tweetRepository.findByUser(Mockito.any(User.class))).thenReturn(new LinkedHashSet<>(list));

        client.get()
                .uri("/tweets/alice/wall")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Tweet.class)
                .value(tweets -> {
                    Assertions.assertEquals(2, tweets.size());
                    Assertions.assertEquals(tweets, list);
                });
    }

    @Test
    void shouldReturnTimeline() {
        final List<Tweet> list = List.of(
                example("alice", "a"),
                example("bob", "b")
        );

        when(timelineRepository.findByUser(Mockito.any(User.class), Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(list));

        client.get()
                .uri("/tweets/bob/timeline")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Tweet.class)
                .value(tweets -> {
                    Assertions.assertEquals(2, tweets.size());
                    Assertions.assertEquals(tweets, list);
                });
    }

    private Tweet example(String userName, String message) {
        return new Tweet(UUID.randomUUID(), new User(userName), message, Instant.now());
    }
}