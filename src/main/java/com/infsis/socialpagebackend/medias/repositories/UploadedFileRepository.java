package com.infsis.socialpagebackend.medias.repositories;

import com.infsis.socialpagebackend.enums.FileCategory;
import com.infsis.socialpagebackend.medias.models.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UploadedFileRepository extends JpaRepository<UploadedFile, Integer> {

    Optional<UploadedFile> findByUuid(String uuid);

    Optional<UploadedFile> findByUrlResource(String urlResource);

    List<UploadedFile> findAllByCategory(FileCategory category);
}
