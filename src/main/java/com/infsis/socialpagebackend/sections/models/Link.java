package com.infsis.socialpagebackend.sections.models;

import com.infsis.socialpagebackend.enums.OwnerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@ToString
@SQLDelete(sql = "UPDATE Link SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
@Table(name = "link", indexes = { @Index(name = "idx_link_owner", columnList = "owner_type, owner_uuid") })
public class Link {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(updatable = false, nullable = false, unique = true, length = 36)
    private String uuid;

    // Polimórfico: tipo y uuid del propietario
    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type", length = 50)
    private OwnerType ownerType;

    @Column(name = "owner_uuid", length = 36)
    private String ownerUuid;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 500)
    private String url;

    @CreatedDate
    @Column(updatable = false)
    private Date createdDate;

    @LastModifiedDate
    @Column(updatable = false)
    private Date lastModifiedDate;

    @Column(nullable = false, columnDefinition = "BOOLEAN NOT NULL DEFAULT '0'")
    private boolean deleted;

    @PrePersist
    public void initializeUuid() {
        this.setUuid(UUID.randomUUID().toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return Objects.equals(id, link.id) && Objects.equals(uuid, link.uuid) && ownerType == link.ownerType && Objects.equals(ownerUuid, link.ownerUuid) && Objects.equals(name, link.name) && Objects.equals(url, link.url) && Objects.equals(createdDate, link.createdDate) && Objects.equals(lastModifiedDate, link.lastModifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, ownerType, ownerUuid, name, url, createdDate, lastModifiedDate);
    }
}
