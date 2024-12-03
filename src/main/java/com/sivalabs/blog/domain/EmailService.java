package com.sivalabs.blog.domain;

import java.util.List;

public interface EmailService {
    void send(String subject, String content);

    void send(String subject, List<String> to, String content);
}
