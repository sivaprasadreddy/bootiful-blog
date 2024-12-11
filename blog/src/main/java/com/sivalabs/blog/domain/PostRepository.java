package com.sivalabs.blog.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface PostRepository extends JpaRepository<Post, Long> {

    @Query("from Post p where p.id = :id")
    Optional<PostProjection> findPostById(Long id);

    @Query("from Post p where p.slug = :slug")
    Optional<PostProjection> findBySlug(@Param("slug") String slug);

    @Query("from Post p")
    Page<PostProjection> findPosts(Pageable pageable);

    @Query("""
        from Post p
        where lower(p.title) like ?1 or lower(p.content) like ?1
    """)
    Page<PostProjection> searchPosts(String query, Pageable pageable);

    @Query("""
        from Post p
        where p.createdAt >= :start and p.createdAt <= :end
    """)
    List<PostProjection> findByCreatedDate(LocalDateTime start, LocalDateTime end);

    boolean existsBySlug(String slug);
}
