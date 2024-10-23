package com.sivalabs.blog.domain.services;

import com.sivalabs.blog.ApplicationProperties;
import com.sivalabs.blog.domain.entities.CommentEntity;
import com.sivalabs.blog.domain.entities.PostEntity;
import com.sivalabs.blog.domain.exceptions.ResourceNotFoundException;
import com.sivalabs.blog.domain.mappers.CommentMapper;
import com.sivalabs.blog.domain.mappers.PostMapper;
import com.sivalabs.blog.domain.models.Comment;
import com.sivalabs.blog.domain.models.CreateCommentCmd;
import com.sivalabs.blog.domain.models.CreatePostCmd;
import com.sivalabs.blog.domain.models.PagedResult;
import com.sivalabs.blog.domain.models.Post;
import com.sivalabs.blog.domain.models.UpdatePostCmd;
import com.sivalabs.blog.domain.repositories.CommentRepository;
import com.sivalabs.blog.domain.repositories.PostRepository;
import com.sivalabs.blog.domain.repositories.UserRepository;
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
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;
    private final ApplicationProperties properties;

    public PostService(
            PostRepository postRepository,
            CommentRepository commentRepository,
            UserRepository userRepository,
            PostMapper postMapper,
            CommentMapper commentMapper,
            ApplicationProperties properties) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
        this.properties = properties;
    }

    public PagedResult<Post> findPosts(Integer pageNo) {
        Pageable pageable = this.getPageRequest(pageNo);
        Page<Post> pagedData = postRepository.findPosts(pageable);
        return PagedResult.from(pagedData);
    }

    public PagedResult<Post> searchPosts(String query, Integer pageNo) {
        Pageable pageable = this.getPageRequest(pageNo);
        Page<Post> pagedData = postRepository.searchPosts("%" + query.toLowerCase() + "%", pageable);
        return PagedResult.from(pagedData);
    }

    public Optional<Post> findPostBySlug(String slug) {
        return postRepository.findBySlug(slug).map(postMapper::toPost);
    }

    public Optional<Post> findPostById(Long postId) {
        return postRepository.findById(postId).map(postMapper::toPost);
    }

    public void createPost(CreatePostCmd cmd) {
        PostEntity entity = new PostEntity();
        entity.setTitle(cmd.title());
        entity.setSlug(cmd.slug());
        entity.setContent(cmd.content());
        entity.setCreatedAt(LocalDateTime.now());

        var user = userRepository.getReferenceById(cmd.createdBy());
        entity.setCreatedBy(user);
        postRepository.save(entity);
    }

    public void updatePost(UpdatePostCmd cmd) {
        PostEntity entity = postRepository
                .findById(cmd.id())
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + cmd.id() + " not found"));
        entity.setTitle(cmd.title());
        entity.setSlug(cmd.slug());
        entity.setContent(cmd.content());
        entity.setUpdatedAt(LocalDateTime.now());
        postRepository.save(entity);
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public void createComment(CreateCommentCmd cmd) {
        CommentEntity entity = commentMapper.toCommentEntity(cmd);
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

    public boolean slugExists(String slug) {
        return postRepository.existsBySlug(slug);
    }
}
