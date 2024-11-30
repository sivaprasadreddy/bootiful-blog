package com.sivalabs.blog.jobs;

import com.sivalabs.blog.domain.*;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class WeeklyEmailSenderJob {

    private static final Logger log = LoggerFactory.getLogger(WeeklyEmailSenderJob.class);
    private final PostService postService;
    private final UserService userService;
    private final EmailService emailService;

    public WeeklyEmailSenderJob(PostService postServiceImpl, UserService userService, EmailService emailService) {
        this.postService = postServiceImpl;
        this.userService = userService;
        this.emailService = emailService;
    }

    @Scheduled(cron = "${blog.newsletter-job-cron}")
    void sendNewsLetter() {
        log.info("Sending newsletter at {}", Instant.now());
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
        List<PostProjection> posts = postService.findPostsCreatedBetween(startOfWeek, end);
        if (posts.isEmpty()) {
            log.info("No posts found for this week. Skipping newsletter");
            return;
        }
        String newsLetterContent = createNewsLetterContent(posts);
        List<String> userEmails =
                userService.findAllUsers().stream().map(User::getEmail).toList();
        if (userEmails.isEmpty()) {
            log.info("No users found for this week. Skipping newsletter");
            return;
        }
        emailService.send("Weekly Newsletter", userEmails, newsLetterContent);
        log.info("Sent newsletter at {} to {} users", Instant.now(), userEmails.size());
    }

    private String createNewsLetterContent(List<PostProjection> posts) {
        StringBuilder emailContent = new StringBuilder();
        for (PostProjection post : posts) {
            // Externalize base url
            String postUrl = "http://localhost:8080/blog/posts/" + post.getSlug();
            var fragment =
                    """
                    <h2><a href="%s">%s</a></h2>
                    <p>%s</p>
                    """
                            .formatted(postUrl, post.getTitle(), post.getContent());
            emailContent.append(fragment);
        }
        return emailContent.toString();
    }
}
