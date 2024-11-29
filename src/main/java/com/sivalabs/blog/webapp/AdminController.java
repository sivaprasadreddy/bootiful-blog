package com.sivalabs.blog.webapp;

import com.sivalabs.blog.domain.CreatePostCmd;
import com.sivalabs.blog.domain.Post;
import com.sivalabs.blog.domain.PostService;
import com.sivalabs.blog.domain.ResourceNotFoundException;
import com.sivalabs.blog.domain.UpdatePostCmd;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
class AdminController {
    private final PostService postService;

    AdminController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts/new")
    String newPostForm(Model model) {
        model.addAttribute("post", new PostPayload("", "", ""));
        return "blog/new-post";
    }

    @PostMapping("/posts")
    String createPost(@Valid @ModelAttribute("post") PostPayload postPayload, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("post", postPayload);
            return "blog/new-post";
        }
        var slug = postPayload.slug();
        if (this.postService.isPostSlugExists(slug)) {
            model.addAttribute("post", postPayload);
            result.rejectValue("slug", "slug.exists", "Slug already exists");
            return "blog/new-post";
        }

        var loginUserId = UserContextUtils.getCurrentUserIdOrThrow();
        var createPostCmd = new CreatePostCmd(postPayload.title(), slug, postPayload.content(), loginUserId);
        this.postService.createPost(createPostCmd);
        return "redirect:/blog/posts/" + createPostCmd.slug();
    }

    @GetMapping("/posts/{postId}/edit")
    String editPostForm(@PathVariable("postId") Long postId, Model model) {
        var post = postService
                .findPostById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + postId + " not found"));
        var postPayload = new PostPayload(post.title(), post.slug(), post.content());
        model.addAttribute("post", postPayload);
        model.addAttribute("postId", postId);
        return "blog/edit-post";
    }

    @PutMapping("/posts/{postId}")
    String updatePost(
            @PathVariable("postId") Long postId,
            @Valid @ModelAttribute("post") PostPayload postPayload,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("post", postPayload);
            model.addAttribute("postId", postId);
            return "blog/edit-post";
        }
        var slug = postPayload.slug();
        Optional<Post> postBySlug = postService.findPostBySlug(slug);
        if (postBySlug.isPresent() && !Objects.equals(postBySlug.get().id(), postId)) {
            model.addAttribute("post", postPayload);
            model.addAttribute("postId", postId);
            result.rejectValue("slug", "slug.exists", "Slug already exists");
            return "blog/edit-post";
        }
        var cmd = new UpdatePostCmd(postId, postPayload.title(), slug, postPayload.content());
        this.postService.updatePost(cmd);
        return "redirect:/blog/posts/" + slug;
    }

    record PostPayload(
            @NotEmpty(message = "Title is required") String title,
            @NotEmpty(message = "Slug is required") String slug,
            @NotEmpty(message = "Content is required") String content) {}
}
