package com.sivalabs.blog.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {

    @Column(name = "name", nullable = false, length = 150)
    @NotEmpty(message = "Name is required")
    private String name;

    @Column(name = "email", length = 150)
    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;

    @Column(name = "content", nullable = false)
    @NotEmpty(message = "Content is required")
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }
}
