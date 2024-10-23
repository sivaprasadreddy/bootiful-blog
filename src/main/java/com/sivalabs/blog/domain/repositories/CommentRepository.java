package com.sivalabs.blog.domain.repositories;

import com.sivalabs.blog.domain.entities.CommentEntity;
import com.sivalabs.blog.domain.models.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<Comment> findByPostId(Long postId);
}
