package com.sivalabs.blog.events;

import com.sivalabs.blog.domain.services.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class BlogEventListener {
    private final EmailService emailService;

    BlogEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    void handle(PostPublishedEvent event) {
        String subject = "New Post Published: " + event.title();
        String content =
                """
                New Post Published: <a href="%s">%s</a>
                %s
                """
                        .formatted(event.slug(), event.title(), event.content());
        emailService.send(subject, content);
    }
}
