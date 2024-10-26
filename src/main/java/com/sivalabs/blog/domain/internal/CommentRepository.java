package com.sivalabs.blog.domain.internal;

import com.sivalabs.blog.domain.models.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<Comment> findByPostId(Long postId);
}
