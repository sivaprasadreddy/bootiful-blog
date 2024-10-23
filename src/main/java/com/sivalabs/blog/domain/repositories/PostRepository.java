package com.sivalabs.blog.domain.repositories;

import com.sivalabs.blog.domain.entities.PostEntity;
import com.sivalabs.blog.domain.models.Post;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Query("select p from PostEntity p join fetch p.createdBy where p.slug = :slug")
    Optional<PostEntity> findBySlug(@Param("slug") String slug);

    @Query(
            """
        select new com.sivalabs.blog.domain.models.Post(
            p.id, p.title, p.slug, p.content,
            p.createdBy.name,
            p.createdAt, p.updatedAt
        )
        from PostEntity p join p.createdBy
    """)
    Page<Post> findPosts(Pageable pageable);

    @Query(
            """
        select new com.sivalabs.blog.domain.models.Post(
            p.id, p.title, p.slug, p.content,
            p.createdBy.name,
            p.createdAt, p.updatedAt
        )
        from PostEntity p join p.createdBy
        where lower(p.title) like ?1 or lower(p.content) like ?1
    """)
    Page<Post> searchPosts(String query, Pageable pageable);

    boolean existsBySlug(String slug);
}
