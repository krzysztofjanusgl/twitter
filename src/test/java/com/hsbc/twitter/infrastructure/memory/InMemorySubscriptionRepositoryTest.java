package com.hsbc.twitter.infrastructure.memory;

import com.hsbc.twitter.domain.SubscriptionRepository;
import com.hsbc.twitter.domain.exception.SubscriptionException;
import com.hsbc.twitter.domain.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.test.StepVerifier.Step;

class InMemorySubscriptionRepositoryTest {
    private static final User bob = new User("bob");
    private static final User alice = new User("alice");
    private final SubscriptionRepository repository = new InMemorySubscriptionRepository();

    @Test
    void shouldAddSubscriber() {
        createSubscriptionStep().verifyComplete();
    }

    @Test
    void shouldNotCreateSubscriptionWhenUserAlreadySubscribe() {
        createSubscriptionStep().verifyComplete();

        StepVerifier.create(repository.subscribe(alice, bob))
                .expectError(SubscriptionException.class)
                .verify();
    }

    @Test
    void shouldNotDeleteSubscriptionWhenNotExist() {
        StepVerifier.create(repository.delete(alice, bob))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void shouldDeleteSubscription() {
        createSubscriptionStep().verifyComplete();

        StepVerifier.create(repository.delete(alice, bob))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptySubscriptions() {
        Assertions.assertEquals(0, repository.findSubscribers(alice).size());
    }

    @Test
    void shouldReturnSubscriptions() {
        createSubscriptionStep().verifyComplete();

        var results = repository.findSubscribers(bob);

        Assertions.assertEquals(1, results.size());
        Assertions.assertSame(alice, results.toArray()[0]);
    }

    private Step<Boolean> createSubscriptionStep() {
        return StepVerifier.create(repository.subscribe(alice, bob))
                .expectNext(true);
    }
}