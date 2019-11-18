package com.hsbc.twitter.domain.event;

import com.hsbc.twitter.domain.model.User;
import lombok.Data;

@Data
public class SubscriptionCreatedEvent extends Event {
    private final User subscriber;
    private final User subscribed;
}
