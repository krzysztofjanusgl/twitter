package com.hsbc.twitter.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(of = "name")
public class User {
    private final String name;

    @JsonCreator
    private static User create(String name) {
        return new User(name);
    }
}
