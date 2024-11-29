package com.sivalabs.blog.domain.internal;

import com.sivalabs.blog.domain.PostProjection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Query("from PostEntity p where p.id = :id")
    Optional<PostProjection> findPostById(Long id);

    @Query("from PostEntity p where p.slug = :slug")
    Optional<PostProjection> findBySlug(@Param("slug") String slug);

    @Query("from PostEntity p")
    Page<PostProjection> findPosts(Pageable pageable);

    @Query("""
        from PostEntity p
        where lower(p.title) like ?1 or lower(p.content) like ?1
    """)
    Page<PostProjection> searchPosts(String query, Pageable pageable);

    @Query("""
        from PostEntity p
        where p.createdAt >= :start and p.createdAt <= :end
    """)
    List<PostProjection> findByCreatedDate(LocalDateTime start, LocalDateTime end);

    boolean existsBySlug(String slug);
}
