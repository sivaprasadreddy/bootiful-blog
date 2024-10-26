package com.sivalabs.blog.domain.internal;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
class BlogEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    BlogEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publish(PostPublishedEvent event) {
        eventPublisher.publishEvent(event);
    }
}
