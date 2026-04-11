package com.infsis.socialpagebackend.moderation.models;

import com.infsis.socialpagebackend.moderation.enums.BlacklistCategory;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "blacklist_word")
public class BlacklistWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 200)
    private String word;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private BlacklistCategory category;

    @Column(nullable = false)
    private boolean active = true;

    /**
     * Si true, esta entrada excluye la palabra del archivo local aunque esté en él.
     * Permite al moderador "anular" palabras del archivo estático sin hacer deploy.
     */
    @Column(nullable = false)
    private boolean excludeFromFile = false;

    @Column(length = 300)
    private String notes;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;
}
