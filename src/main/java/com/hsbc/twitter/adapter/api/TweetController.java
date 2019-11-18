package com.hsbc.twitter.adapter.api;

import com.hsbc.twitter.domain.event.TweetCreatedEvent;
import com.hsbc.twitter.domain.model.Tweet;
import com.hsbc.twitter.domain.model.User;
import com.hsbc.twitter.domain.provider.TweetProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/tweets")
@AllArgsConstructor
@CrossOrigin
@Validated
class TweetController {
    private final TweetProvider tweetProvider;

    @PostMapping
    @ResponseStatus(CREATED)
    Mono<TweetCreatedEvent> create(@RequestBody @Valid CreateTweetRequest request) {
        return tweetProvider.create(request.getMessage(), new User(request.getUserName()));
    }

    @GetMapping("/{user}/wall")
    Set<Tweet> wall(@PathVariable final String user) {
        return tweetProvider.wall(new User(user));
    }

    @GetMapping("/{user}/timeline")
    Flux<Tweet> timeline(@PathVariable final String user,
                         @RequestHeader(value = "page", defaultValue = "0") int page,
                         @RequestHeader(value = "size", defaultValue = "30") int size) {
        return tweetProvider.timeline(new User(user), PageRequest.of(page, size));
    }

    @Data
    private static class CreateTweetRequest {
        @NotBlank
        private String userName;
        @NotBlank
        @Size(min = 10, max = 140)
        private String message;
    }
}
