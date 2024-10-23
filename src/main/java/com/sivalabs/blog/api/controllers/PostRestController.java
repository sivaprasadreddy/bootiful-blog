package com.sivalabs.blog.api.controllers;

import com.sivalabs.blog.api.models.PostPayload;
import com.sivalabs.blog.domain.exceptions.ResourceNotFoundException;
import com.sivalabs.blog.domain.models.Comment;
import com.sivalabs.blog.domain.models.CreateCommentCmd;
import com.sivalabs.blog.domain.models.CreatePostCmd;
import com.sivalabs.blog.domain.models.PagedResult;
import com.sivalabs.blog.domain.models.Post;
import com.sivalabs.blog.domain.models.UpdatePostCmd;
import com.sivalabs.blog.domain.services.PostService;
import com.sivalabs.blog.security.SecurityUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/posts")
class PostRestController {
    private final PostService postService;

    PostRestController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    PagedResult<Post> searchPosts(
            @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam(value = "page", defaultValue = "1") Integer page) {
        if (query == null || query.trim().isEmpty()) {
            return postService.findPosts(page);
        }
        return postService.searchPosts(query, page);
    }

    @GetMapping("/{slug}")
    ResponseEntity<Post> getPostBySlug(@PathVariable(value = "slug") String slug) {
        var post = postService
                .findPostBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post with slug \"" + slug + "\" not found"));
        return ResponseEntity.ok(post);
    }

    @GetMapping("/{slug}/comments")
    List<Comment> getPostComments(@PathVariable(value = "slug") String slug) {
        Post post = postService
                .findPostBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post with slug \"" + slug + "\" not found"));
        return postService.getCommentsByPostId(post.id());
    }

    @PostMapping("/{slug}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    void createComment(@PathVariable(value = "slug") String slug, @Valid @RequestBody CreateCommentPayload payload) {
        Post post = postService
                .findPostBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post with slug '" + slug + "' not found"));
        var createdCommentCmd = new CreateCommentCmd(payload.name, payload.email, payload.content, post.id());
        postService.createComment(createdCommentCmd);
    }

    public record CreateCommentPayload(String name, String email, String content) {}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "Bearer")
    ResponseEntity<Void> createPost(@Valid @RequestBody PostPayload postPayload) {
        var loginUserId = SecurityUtils.getCurrentUserIdOrThrow();
        var slug = postPayload.slug();
        var createPostCmd = new CreatePostCmd(postPayload.title(), slug, postPayload.content(), loginUserId);
        this.postService.createPost(createPostCmd);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath(null)
                .path("/api/posts/{slug}")
                .buildAndExpand(slug)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{slug}")
    @SecurityRequirement(name = "Bearer")
    ResponseEntity<Void> updatePost(@PathVariable("slug") String slug, @Valid @RequestBody PostPayload postPayload) {
        Post post = postService
                .findPostBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post with slug '" + slug + "' not found"));
        var updatedSlug = postPayload.slug();
        var cmd = new UpdatePostCmd(post.id(), postPayload.title(), updatedSlug, postPayload.content());
        this.postService.updatePost(cmd);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath(null)
                .path("/api/posts/{slug}")
                .buildAndExpand(updatedSlug)
                .toUri();
        return ResponseEntity.status(HttpStatus.OK).location(location).build();
    }
}
