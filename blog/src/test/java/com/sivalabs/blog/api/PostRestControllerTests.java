package com.sivalabs.blog.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.sivalabs.blog.AbstractIT;
import com.sivalabs.blog.api.PostRestController.CreateCommentPayload;
import com.sivalabs.blog.api.PostRestController.PostPayload;
import com.sivalabs.blog.domain.Role;
import com.sivalabs.blog.dtos.CommentDto;
import com.sivalabs.blog.dtos.PagedResult;
import com.sivalabs.blog.dtos.PostDto;
import com.sivalabs.blog.dtos.UserDto;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-data.sql")
class PostRestControllerTests extends AbstractIT {
    @Autowired
    JwtTokenHelper jwtTokenHelper;

    @Test
    void shouldGetPosts() {
        ResponseEntity<PagedResult<PostDto>> response =
                restTemplate.exchange("/api/posts", GET, null, new ParameterizedTypeReference<>() {});
        assertThat(response.getStatusCode()).isEqualTo(OK);
        PagedResult<PostDto> pagedResult = Objects.requireNonNull(response.getBody());
        assertThat(pagedResult.data()).hasSize(5);
        assertThat(pagedResult.currentPageNo()).isEqualTo(1);
        assertThat(pagedResult.totalPages()).isEqualTo(2);
        assertThat(pagedResult.totalElements()).isEqualTo(9);
        assertThat(pagedResult.hasNextPage()).isTrue();
        assertThat(pagedResult.hasPreviousPage()).isFalse();
    }

    @Test
    void shouldSearchPosts() {
        ResponseEntity<PagedResult<PostDto>> response =
                restTemplate.exchange("/api/posts?query=spring", GET, null, new ParameterizedTypeReference<>() {});
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(Objects.requireNonNull(response.getBody()).data()).hasSize(4);
    }

    @Test
    void shouldGetPostBySlug() {
        ResponseEntity<PostDto> response =
                restTemplate.getForEntity("/api/posts/{slug}", PostDto.class, "introducing-springboot");
        assertThat(response.getStatusCode()).isEqualTo(OK);
        PostDto postDto = response.getBody();
        assertThat(postDto).isNotNull();
        assertThat(postDto.id()).isEqualTo(2);
        assertThat(postDto.title()).isEqualTo("SpringBoot: Introducing SpringBoot");
        assertThat(postDto.slug()).isEqualTo("introducing-springboot");
    }

    @Test
    void shouldGetPostComments() {
        ResponseEntity<List<CommentDto>> response = restTemplate.exchange(
                "/api/posts/{slug}/comments",
                GET,
                null,
                new ParameterizedTypeReference<>() {},
                "introducing-springboot");
        assertThat(response.getStatusCode()).isEqualTo(OK);
        List<CommentDto> commentDtos = response.getBody();
        assertThat(commentDtos).hasSize(2);
    }

    @Test
    void shouldCreateCommentSuccessfully() {
        var payload = new CreateCommentPayload("Siva", "siva@gmail.com", "Test comment");
        ResponseEntity<Void> response =
                restTemplate.postForEntity("/api/posts/{slug}/comments", payload, Void.class, "introducing-springboot");
        assertThat(response.getStatusCode()).isEqualTo(CREATED);
    }

    @Test
    void shouldCreatePostSuccessfully() {
        var payload = getSamplePostPayload();
        HttpHeaders headers = new HttpHeaders();
        UserDto userDto = new UserDto(2L, "Siva", "siva@gmail.com", "", Role.ROLE_USER);
        String token = jwtTokenHelper.generateToken(userDto).token();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> httpEntity = new HttpEntity<>(payload, headers);

        ResponseEntity<Void> response = restTemplate.postForEntity("/api/posts", httpEntity, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        URI location = response.getHeaders().getLocation();
        assertThat(location).isNotNull();
        assertThat(location.toString()).endsWith("/api/posts/post-slug");
    }

    @Test
    void shouldUpdatePostSuccessfully() {
        var payload =
                """
            {
              "title":"Installing LinuxMint OS",
              "slug":"installing-linuxmint-os",
              "content":"Installing LinuxMint 22"
            }
            """;
        HttpHeaders headers = new HttpHeaders();
        UserDto userDto = new UserDto(2L, "Siva", "siva@gmail.com", "", Role.ROLE_USER);
        String token = jwtTokenHelper.generateToken(userDto).token();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> httpEntity = new HttpEntity<>(payload, headers);

        ResponseEntity<Void> response =
                restTemplate.exchange("/api/posts/{slug}", PUT, httpEntity, Void.class, "installing-linuxmint");

        assertThat(response.getStatusCode()).isEqualTo(OK);
        URI location = response.getHeaders().getLocation();
        assertThat(location).isNotNull();
        assertThat(location.toString()).endsWith("/api/posts/installing-linuxmint-os");
    }

    public static PostPayload getSamplePostPayload() {
        return new PostPayload("Post Title", "post-slug", "Post content");
    }
}
