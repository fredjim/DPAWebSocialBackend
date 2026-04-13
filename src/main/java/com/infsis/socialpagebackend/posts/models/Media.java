package com.infsis.socialpagebackend.posts.models;

import com.infsis.socialpagebackend.medias.models.UploadedFile;
import com.infsis.socialpagebackend.sections.models.Article;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE media SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
@Table(name = "media")
public class Media {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(updatable = false, nullable = false, unique = true, length = 36)
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "content_id", referencedColumnName = "uuid")
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", referencedColumnName = "uuid")
    private Article article;

    @Column(nullable = false, length = 150)
    private String file_name;

    @ManyToOne
    @JoinColumn(name = "uploaded_file_id", referencedColumnName = "id")
    private UploadedFile uploadedFile;

    @Column(nullable = false, length = 10)
    private String file_type;

    @Column(nullable = false, length = 10)
    private Integer number;

    @Column(nullable = false, columnDefinition = "BOOLEAN NOT NULL DEFAULT false")
    private boolean deleted;

    @CreatedDate
    @Column(updatable = false)
    private Date createdDate;

    @LastModifiedDate
    @Column(updatable = false)
    private Date lastModifiedDate;

    @PrePersist
    public void initializeUuid() {
        this.setUuid(UUID.randomUUID().toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Media media = (Media) o;
        return Objects.equals(id, media.id) && Objects.equals(uuid, media.uuid) && Objects.equals(uploadedFile, media.uploadedFile) && Objects.equals(file_type, media.file_type) && Objects.equals(number, media.number) && Objects.equals(createdDate, media.createdDate) && Objects.equals(lastModifiedDate, media.lastModifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, uploadedFile, file_type, number, createdDate, lastModifiedDate);
    }
}
