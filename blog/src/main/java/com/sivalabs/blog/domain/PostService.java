package com.sivalabs.blog.domain;

import com.sivalabs.blog.ApplicationProperties;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BlogEventPublisher blogEventPublisher;
    private final ApplicationProperties properties;

    PostService(
            PostRepository postRepository,
            CommentRepository commentRepository,
            UserRepository userRepository,
            BlogEventPublisher blogEventPublisher,
            ApplicationProperties properties) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.blogEventPublisher = blogEventPublisher;
        this.properties = properties;
    }

    public Page<PostProjection> findPosts(Integer pageNo) {
        Pageable pageable = this.getPageRequest(pageNo);
        return postRepository.findPosts(pageable);
    }

    public Page<PostProjection> searchPosts(String query, Integer pageNo) {
        Pageable pageable = this.getPageRequest(pageNo);
        return postRepository.searchPosts("%" + query.toLowerCase() + "%", pageable);
    }

    public List<PostProjection> findPostsCreatedBetween(LocalDateTime start, LocalDateTime end) {
        return postRepository.findByCreatedDate(start, end);
    }

    public Optional<PostProjection> findPostBySlug(String slug) {
        return postRepository.findBySlug(slug);
    }

    public Optional<PostProjection> findPostById(Long postId) {
        return postRepository.findPostById(postId);
    }

    @Transactional
    public void createPost(CreatePostCmd cmd) {
        var user = userRepository.getReferenceById(cmd.createdBy());

        var entity = new Post();
        entity.setTitle(cmd.title());
        entity.setSlug(cmd.slug());
        entity.setContent(cmd.content());
        entity.setCreatedBy(user);
        postRepository.save(entity);

        var event = new PostPublishedEvent(entity.getTitle(), entity.getSlug(), entity.getContent());
        blogEventPublisher.publish(event);
    }

    @Transactional
    public void updatePost(UpdatePostCmd cmd) {
        var entity = postRepository
                .findById(cmd.id())
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + cmd.id() + " not found"));
        entity.setTitle(cmd.title());
        entity.setSlug(cmd.slug());
        entity.setContent(cmd.content());
        postRepository.save(entity);
    }

    public boolean isPostSlugExists(String slug) {
        return postRepository.existsBySlug(slug);
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Transactional
    public void createComment(CreateCommentCmd cmd) {
        var post = postRepository.getReferenceById(cmd.postId());
        var entity = new Comment();
        entity.setName(cmd.name());
        entity.setEmail(cmd.email());
        entity.setContent(cmd.content());
        entity.setPost(post);
        commentRepository.save(entity);
    }

    private Pageable getPageRequest(Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        int pageSize = properties.postsPerPage();
        if (pageNo == null || pageNo < 1) {
            pageNo = 1;
        }
        return PageRequest.of(pageNo - 1, pageSize, sort);
    }
}
