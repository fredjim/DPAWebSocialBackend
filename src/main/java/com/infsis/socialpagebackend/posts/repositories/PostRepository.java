package com.infsis.socialpagebackend.posts.repositories;

import com.infsis.socialpagebackend.posts.models.Post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT p FROM Post p WHERE p.uuid = ?1")
    Post findOneByUuid(String Uuid);

    @Query("SELECT p FROM Post p WHERE p.institution.uuid = :tenantId AND p.deleted = false ORDER BY p.post_date DESC")
    List<Post> findAllByInstitutionUuid(@Param("tenantId") String tenantId);

    @Query("SELECT p FROM Post p WHERE p.institution.uuid = :tenantId AND p.deleted = false ORDER BY p.post_date DESC")
    Page<Post> findAllPagedByTenant(@Param("tenantId") String tenantId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.institution.uuid = :tenantId AND LOWER(p.content.text.text) LIKE LOWER(CONCAT('%', :text, '%')) ORDER BY p.createdDate DESC")
    List<Post> searchPostsByTextAndTenant(@Param("text") String text, @Param("tenantId") String tenantId);
}
