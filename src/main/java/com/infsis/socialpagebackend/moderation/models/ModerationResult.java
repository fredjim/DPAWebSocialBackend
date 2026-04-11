package com.infsis.socialpagebackend.moderation.models;

import com.infsis.socialpagebackend.moderation.enums.ContentType;
import com.infsis.socialpagebackend.moderation.enums.ModerationDecision;
import com.infsis.socialpagebackend.moderation.enums.ModerationFilterType;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

/**
 * Registro de auditoría de cada decisión del pipeline de moderación.
 * Permite consultar el historial completo de moderación por contenido.
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "moderation_result")
public class ModerationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false, unique = true, length = 36)
    private String uuid;

    /** UUID del comment o reply moderado */
    @Column(nullable = false, length = 36)
    private String contentUuid;

    /** Tipo de contenido moderado */
    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    /** Decisión final del pipeline */
    @Column(nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private ModerationDecision decision;

    /** Filtro que tomó la decisión (null si fue aprobado pasando todos) */
    @Column(length = 15)
    @Enumerated(EnumType.STRING)
    private ModerationFilterType filterUsed;

    /** Razón de rechazo o revisión (null si fue aprobado) */
    @Column(length = 500)
    private String reason;

    /** Score de confianza de la decisión (0.0 - 1.0) */
    @Column
    private Double confidence;

    /** Tiempo total del pipeline en ms */
    @Column
    private Long processingTimeMs;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;

    @PrePersist
    public void initUuid() {
        this.uuid = UUID.randomUUID().toString();
    }
}
