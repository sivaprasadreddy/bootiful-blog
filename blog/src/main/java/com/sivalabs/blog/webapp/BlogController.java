package com.sivalabs.blog.webapp;

import com.sivalabs.blog.domain.CreateCommentCmd;
import com.sivalabs.blog.domain.PostService;
import com.sivalabs.blog.domain.ResourceNotFoundException;
import com.sivalabs.blog.dtos.PagedResult;
import com.sivalabs.blog.dtos.PostDto;
import com.sivalabs.blog.mappers.BlogMapper;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
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
@RequestMapping("/blog/posts")
class BlogController {
    private final PostService postService;
    private final BlogMapper blogMapper;

    BlogController(PostService postService, BlogMapper blogMapper) {
        this.postService = postService;
        this.blogMapper = blogMapper;
    }

    @GetMapping("")
    String viewPosts(
            @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            Model model) {
        Page<PostDto> postDtoPage = postService.searchPosts(query, page).map(blogMapper::toPostDto);
        PagedResult<PostDto> pagedResult = PagedResult.from(postDtoPage);
        model.addAttribute("postsResponse", pagedResult);
        String paginationRootUrl = "/blog/posts?";
        if (StringUtils.isNotBlank(query)) {
            paginationRootUrl = "/blog/posts?query=" + query;
        }
        model.addAttribute("paginationRootUrl", paginationRootUrl);
        return "blog/posts";
    }

    @GetMapping("/{slug}")
    String viewPostBySlug(@PathVariable(value = "slug") String slug, Model model) {
        PostDto postDto = postService
                .findPostBySlug(slug)
                .map(blogMapper::toPostDto)
                .orElseThrow(() -> new ResourceNotFoundException("Post with slug '" + slug + "' not found"));
        model.addAttribute("post", postDto);
        model.addAttribute("comments", postService.getCommentsByPostId(postDto.id()));
        model.addAttribute("comment", new CreateCommentPayload("", "", ""));
        return "blog/view-post";
    }

    @PostMapping("/{slug}/comments")
    String addComment(
            @PathVariable(value = "slug") String slug,
            @Valid @ModelAttribute("comment") CreateCommentPayload comment,
            BindingResult result,
            Model model) {
        PostDto postDto = postService
                .findPostBySlug(slug)
                .map(blogMapper::toPostDto)
                .orElseThrow(() -> new ResourceNotFoundException("Post with slug '" + slug + "' not found"));
        if (result.hasErrors()) {
            model.addAttribute("post", postDto);
            model.addAttribute("comment", comment);
            return "blog/view-post";
        }
        var cmd = new CreateCommentCmd(comment.name(), comment.email(), comment.content(), postDto.id());
        postService.createComment(cmd);
        return "redirect:/blog/posts/" + postDto.slug();
    }

    public record CreateCommentPayload(String name, String email, String content) {}
}
