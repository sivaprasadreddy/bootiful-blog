package com.sivalabs.blog.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class BlogEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public BlogEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publish(PostPublishedEvent event) {
        eventPublisher.publishEvent(event);
    }
}
