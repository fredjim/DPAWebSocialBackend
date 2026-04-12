package com.infsis.socialpagebackend.medias.models;

import com.infsis.socialpagebackend.enums.FileCategory;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "uploaded_file")
public class UploadedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(updatable = false, nullable = false, unique = true, length = 36)
    private String uuid;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 200)
    private String urlResource;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FileCategory category;

    @Column(nullable = false, length = 50)
    private String mimeType;

    @Column(nullable = false, length = 20)
    private String status;

    @PrePersist
    public void initUuid() {
        if (uuid == null) uuid = UUID.randomUUID().toString();
    }
}
