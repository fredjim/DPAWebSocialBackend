package com.infsis.socialpagebackend.navigation.models;

import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.institutions.models.Institution;
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
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"users", "institution"})
@Entity
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE nav_item SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
@Table(name = "nav_item")
public class NavItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(updatable = false, nullable = false, unique = true, length = 36)
    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "uuid")
    private Users users; // referencia por uuid al usuario (userUUID)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id", referencedColumnName = "uuid")
    private Institution institution; // referencia por uuid a la institución (institutionUUID)

    @Column(nullable = false, length = 200)
    private String label;

    @Column(length = 1000)
    private String url;

    @Column(nullable = false, columnDefinition = "BOOLEAN NOT NULL DEFAULT '1'")
    private boolean visible = true;

    @Column(name = "item_order", nullable = false)
    private Integer orderIndex = 0;

    @CreatedDate
    @Column(updatable = false)
    private Date createdDate;

    @LastModifiedDate
    private Date lastModifiedDate;

    @Column(nullable = false, columnDefinition = "BOOLEAN NOT NULL DEFAULT '0'")
    private boolean deleted = false;

    @PrePersist
    public void initializeUuid() {
        if (this.uuid == null) this.uuid = UUID.randomUUID().toString();
    }
}

