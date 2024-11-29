package com.sivalabs.blog.domain.internal;

import com.sivalabs.blog.ApplicationProperties;
import com.sivalabs.blog.domain.Comment;
import com.sivalabs.blog.domain.CreateCommentCmd;
import com.sivalabs.blog.domain.CreatePostCmd;
import com.sivalabs.blog.domain.PagedResult;
import com.sivalabs.blog.domain.Post;
import com.sivalabs.blog.domain.PostService;
import com.sivalabs.blog.domain.ResourceNotFoundException;
import com.sivalabs.blog.domain.UpdatePostCmd;
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
class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BlogMapper blogMapper;
    private final BlogEventPublisher blogEventPublisher;
    private final ApplicationProperties properties;

    PostServiceImpl(
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

    @Override
    public PagedResult<Post> findPosts(Integer pageNo) {
        Pageable pageable = this.getPageRequest(pageNo);
        Page<Post> postsPage = postRepository
                .findPosts(pageable)
                // we could directly return Page<PostProjection>.
                // Converting to PostProjection to Post for demonstrating MapStruct features
                .map(blogMapper::toPost);
        return PagedResult.from(postsPage);
    }

    @Override
    public PagedResult<Post> searchPosts(String query, Integer pageNo) {
        Pageable pageable = this.getPageRequest(pageNo);
        Page<Post> postsPage = postRepository
                .searchPosts("%" + query.toLowerCase() + "%", pageable)
                .map(blogMapper::toPost);
        return PagedResult.from(postsPage);
    }

    @Override
    public List<Post> findPostsCreatedBetween(LocalDateTime start, LocalDateTime end) {
        return postRepository.findByCreatedDate(start, end).stream()
                .map(blogMapper::toPost)
                .toList();
    }

    @Override
    public Optional<Post> findPostBySlug(String slug) {
        return postRepository.findBySlug(slug).map(blogMapper::toPost);
    }

    @Override
    public Optional<Post> findPostById(Long postId) {
        return postRepository.findPostById(postId).map(blogMapper::toPost);
    }

    @Transactional
    @Override
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
    @Override
    public void updatePost(UpdatePostCmd cmd) {
        var entity = postRepository
                .findById(cmd.id())
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + cmd.id() + " not found"));
        entity.setTitle(cmd.title());
        entity.setSlug(cmd.slug());
        entity.setContent(cmd.content());
        postRepository.save(entity);
    }

    @Override
    public boolean isPostSlugExists(String slug) {
        return postRepository.existsBySlug(slug);
    }

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Transactional
    @Override
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
