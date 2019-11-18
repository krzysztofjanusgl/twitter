package com.hsbc.twitter.infrastructure.memory;

import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class PageableUtil {
    static <T> List<T> slice(Collection<T> stream, Pageable pageable) {
        return stream.stream()
                .skip(pageable.getPageSize()*pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
    }
}
