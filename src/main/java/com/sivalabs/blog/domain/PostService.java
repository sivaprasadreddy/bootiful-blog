package com.sivalabs.blog.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostService {
    PagedResult<Post> findPosts(Integer pageNo);

    PagedResult<Post> searchPosts(String query, Integer pageNo);

    List<Post> findPostsCreatedBetween(LocalDateTime start, LocalDateTime end);

    Optional<Post> findPostBySlug(String slug);

    Optional<Post> findPostById(Long postId);

    void createPost(CreatePostCmd cmd);

    void updatePost(UpdatePostCmd cmd);

    boolean isPostSlugExists(String slug);

    List<Comment> getCommentsByPostId(Long postId);

    void createComment(CreateCommentCmd cmd);
}
