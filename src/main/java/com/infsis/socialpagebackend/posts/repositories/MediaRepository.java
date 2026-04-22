package com.infsis.socialpagebackend.posts.repositories;

import com.infsis.socialpagebackend.posts.models.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Integer> {

    @Query("SELECT m FROM Media m WHERE m.uuid = ?1")
    Media findOneByUuid(String Uuid);

    @Query("SELECT m FROM Media m WHERE m.uploadedFile.uuid = ?1")
    Optional<Media> findByUploadedFileUuid(String uploadedFileUuid);
}
