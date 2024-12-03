package com.sivalabs.blog.domain;

import com.sivalabs.blog.ApplicationProperties;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

public class ConsoleLoggingEmailService implements EmailService {
    private static final Logger log = LoggerFactory.getLogger(ConsoleLoggingEmailService.class);
    private final ApplicationProperties properties;

    public ConsoleLoggingEmailService(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Async
    public void send(String subject, String content) {
        String supportEmail = properties.supportEmail();
        this.send(subject, List.of(supportEmail), content);
    }

    @Async
    public void send(String subject, List<String> to, String content) {
        String supportEmail = properties.supportEmail();
        String email =
                """
                \n
                ===================================================
                Subject : %s
                From : %s
                To: %s
                %s
                ===================================================
                \n
                """
                        .formatted(subject, supportEmail, to, content);
        log.info(email);
    }
}
