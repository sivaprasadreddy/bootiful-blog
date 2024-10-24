package com.sivalabs.blog.domain.services;

import com.sivalabs.blog.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender javaMailSender;
    private final ApplicationProperties properties;

    public EmailService(JavaMailSender javaMailSender, ApplicationProperties properties) {
        this.javaMailSender = javaMailSender;
        this.properties = properties;
    }

    @Async
    public void send(String subject, String content) {
        String supportEmail = properties.supportEmail();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(supportEmail);
        mailMessage.setReplyTo(supportEmail);
        mailMessage.setFrom(supportEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);

        try {
            javaMailSender.send(mailMessage);
        } catch (MailException e) {
            log.error(e.getMessage(), e);
        }
    }
}
