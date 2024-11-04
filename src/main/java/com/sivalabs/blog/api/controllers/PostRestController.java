package com.sivalabs.blog.api.controllers;

import com.sivalabs.blog.api.services.JwtUserContextUtils;
import com.sivalabs.blog.domain.BadRequestException;
import com.sivalabs.blog.domain.PostService;
import com.sivalabs.blog.domain.ResourceNotFoundException;
import com.sivalabs.blog.domain.models.Comment;
import com.sivalabs.blog.domain.models.CreateCommentCmd;
import com.sivalabs.blog.domain.models.CreatePostCmd;
import com.sivalabs.blog.domain.models.PagedResult;
import com.sivalabs.blog.domain.models.Post;
import com.sivalabs.blog.domain.models.UpdatePostCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
@Tag(name = "Posts API")
class PostRestController {
    private final PostService postService;

    PostRestController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("")
    @Operation(summary = "Find posts by page number, optionally filter by a search query")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Returns posts for the given page number and query filter"),
    })
    PagedResult<Post> findPosts(
            @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam(value = "page", defaultValue = "1") Integer page) {
        if (query == null || query.trim().isEmpty()) {
            return postService.findPosts(page);
        }
        return postService.searchPosts(query, page);
    }

    @GetMapping("/{slug}")
    @Operation(summary = "Find post by slug")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Returns post for the given slug"),
        @ApiResponse(responseCode = "404", description = "Post doesn't exists for the given slug"),
    })
    ResponseEntity<Post> getPostBySlug(@PathVariable(value = "slug") String slug) {
        var post = postService
                .findPostBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post with slug '" + slug + "' not found"));
        return ResponseEntity.ok(post);
    }

    @GetMapping("/{slug}/comments")
    @Operation(summary = "Find post comments by post slug")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Returns post comments for the given slug"),
        @ApiResponse(responseCode = "404", description = "Post doesn't exists for the given slug"),
    })
    List<Comment> getPostComments(@PathVariable(value = "slug") String slug) {
        Post post = postService
                .findPostBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post with slug '" + slug + "' not found"));
        return postService.getCommentsByPostId(post.id());
    }

    @PostMapping("/{slug}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new comment")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Comment created successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "404", description = "Post doesn't exists for the given slug"),
    })
    void createComment(@PathVariable(value = "slug") String slug, @Valid @RequestBody CreateCommentPayload payload) {
        Post post = postService
                .findPostBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post with slug '" + slug + "' not found"));
        var createdCommentCmd = new CreateCommentCmd(payload.name, payload.email, payload.content, post.id());
        postService.createComment(createdCommentCmd);
    }

    public record CreateCommentPayload(String name, String email, String content) {}

    @PostMapping("")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Create a new post")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Post created successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    ResponseEntity<Void> createPost(@Valid @RequestBody PostPayload postPayload) {
        var loginUserId = JwtUserContextUtils.getCurrentUserIdOrThrow();
        var slug = postPayload.slug();
        var cmd = new CreatePostCmd(postPayload.title(), slug, postPayload.content(), loginUserId);
        this.postService.createPost(cmd);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath(null)
                .path("/api/posts/{slug}")
                .buildAndExpand(slug)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{slug}")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Update an existing post")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Post updated successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    ResponseEntity<Void> updatePost(@PathVariable("slug") String slug, @Valid @RequestBody PostPayload postPayload) {
        Post post = postService
                .findPostBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post with slug '" + slug + "' not found"));
        var updatedSlug = postPayload.slug();
        Optional<Post> postBySlug = postService.findPostBySlug(updatedSlug);
        if (postBySlug.isPresent() && !Objects.equals(postBySlug.get().id(), post.id())) {
            throw new BadRequestException("Post with slug '" + updatedSlug + "' already exists");
        }
        var cmd = new UpdatePostCmd(post.id(), postPayload.title(), updatedSlug, postPayload.content());
        this.postService.updatePost(cmd);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath(null)
                .path("/api/posts/{slug}")
                .buildAndExpand(updatedSlug)
                .toUri();
        return ResponseEntity.status(HttpStatus.OK).location(location).build();
    }

    public record PostPayload(
            @NotEmpty(message = "Title is required") String title,
            @NotEmpty(message = "Slug is required") String slug,
            @NotEmpty(message = "Content is required") String content) {}
}
