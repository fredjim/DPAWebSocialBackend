package com.infsis.socialpagebackend.moderation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando el pipeline de moderación rechaza un comentario o reply.
 * Devuelve HTTP 422 Unprocessable Entity al cliente.
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ContentBlockedException extends RuntimeException {

    private final String moderationReason;

    public ContentBlockedException(String moderationReason) {
        super("Contenido bloqueado por el sistema de moderación");
        this.moderationReason = moderationReason;
    }

    public String getModerationReason() {
        return moderationReason;
    }
}
