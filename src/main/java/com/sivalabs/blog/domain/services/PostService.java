package com.sivalabs.blog.domain.services;

import com.sivalabs.blog.ApplicationProperties;
import com.sivalabs.blog.domain.entities.PostEntity;
import com.sivalabs.blog.domain.exceptions.ResourceNotFoundException;
import com.sivalabs.blog.domain.models.Comment;
import com.sivalabs.blog.domain.models.CreateCommentCmd;
import com.sivalabs.blog.domain.models.CreatePostCmd;
import com.sivalabs.blog.domain.models.PagedResult;
import com.sivalabs.blog.domain.models.Post;
import com.sivalabs.blog.domain.models.UpdatePostCmd;
import com.sivalabs.blog.domain.repositories.CommentRepository;
import com.sivalabs.blog.domain.repositories.PostRepository;
import com.sivalabs.blog.domain.repositories.UserRepository;
import com.sivalabs.blog.events.BlogEventPublisher;
import com.sivalabs.blog.events.PostPublishedEvent;
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
    private final BlogMapper blogMapper;
    private final BlogEventPublisher blogEventPublisher;
    private final ApplicationProperties properties;

    public PostService(
            PostRepository postRepository,
            CommentRepository commentRepository,
            UserRepository userRepository,
            BlogMapper blogMapper,
            BlogEventPublisher blogEventPublisher,
            ApplicationProperties properties) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.blogMapper = blogMapper;
        this.blogEventPublisher = blogEventPublisher;
        this.properties = properties;
    }

    public PagedResult<Post> findPosts(Integer pageNo) {
        Pageable pageable = this.getPageRequest(pageNo);
        Page<Post> postsPage = postRepository.findPosts(pageable).map(blogMapper::toPost);
        return PagedResult.from(postsPage);
    }

    public PagedResult<Post> searchPosts(String query, Integer pageNo) {
        Pageable pageable = this.getPageRequest(pageNo);
        Page<Post> postsPage = postRepository
                .searchPosts("%" + query.toLowerCase() + "%", pageable)
                .map(blogMapper::toPost);
        return PagedResult.from(postsPage);
    }

    public Optional<Post> findPostBySlug(String slug) {
        return postRepository.findBySlug(slug).map(blogMapper::toPost);
    }

    public Optional<Post> findPostById(Long postId) {
        return postRepository.findPostById(postId).map(blogMapper::toPost);
    }

    @Transactional
    public void createPost(CreatePostCmd cmd) {
        var user = userRepository.getReferenceById(cmd.createdBy());

        var entity = new PostEntity();
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
        var entity = blogMapper.toCommentEntity(cmd);
        var post = postRepository.getReferenceById(cmd.postId());
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
