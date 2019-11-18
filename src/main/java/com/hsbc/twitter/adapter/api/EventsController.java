package com.hsbc.twitter.adapter.api;

import com.hsbc.twitter.domain.event.Event;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ReplayProcessor;

@RestController
@RequestMapping("/events")
@CrossOrigin
class EventsController {
    private final ReplayProcessor<Object> processor = ReplayProcessor.create(10);

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Object> stream() {
        return processor.share();
    }

    @EventListener
    public void on(Event object) {
        processor.onNext(object);
    }
}
