package com.sivalabs.blog.web.controllers;

import com.sivalabs.blog.domain.exceptions.ResourceNotFoundException;
import com.sivalabs.blog.domain.models.CreateCommentCmd;
import com.sivalabs.blog.domain.models.PagedResult;
import com.sivalabs.blog.domain.models.Post;
import com.sivalabs.blog.domain.services.PostService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/blog")
class BlogController {
    private final PostService postService;

    BlogController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    String viewPosts(
            @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            Model model) {
        PagedResult<Post> pagedResult = postService.searchPosts(query, page);
        model.addAttribute("postsResponse", pagedResult);
        String paginationRootUrl = "/blog/posts?";
        if (!query.isEmpty()) {
            paginationRootUrl = "/blog/posts?query=" + query;
        }
        model.addAttribute("paginationRootUrl", paginationRootUrl);
        return "blog/posts";
    }

    @GetMapping("/posts/{slug}")
    String viewPostBySlug(@PathVariable(value = "slug") String slug, Model model) {
        Post post = postService
                .findPostBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post with slug \"" + slug + "\" not found"));
        model.addAttribute("post", post);
        model.addAttribute("comments", postService.getCommentsByPostId(post.id()));
        model.addAttribute("comment", new CreateCommentPayload("", "", ""));
        return "blog/view_post";
    }

    @PostMapping("/posts/{slug}/comments")
    String addComment(
            @PathVariable(value = "slug") String slug,
            @Valid @ModelAttribute("comment") CreateCommentPayload comment,
            BindingResult result,
            Model model) {
        Post post = postService
                .findPostBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post with slug \"" + slug + "\" not found"));
        if (result.hasErrors()) {
            model.addAttribute("post", post);
            model.addAttribute("comment", comment);
            return "blog/view_post";
        }
        var cmd = new CreateCommentCmd(comment.name(), comment.email(), comment.content(), post.id());
        postService.createComment(cmd);
        // String subject = "A new comment on post :" + post.title();
        // String content = "Comment :\n" + comment.content();
        // emailService.send(subject, content);
        return "redirect:/blog/posts/" + post.slug();
    }

    public record CreateCommentPayload(String name, String email, String content) {}
}
