package com.infsis.socialpagebackend.moderation.controllers;

import com.infsis.socialpagebackend.moderation.dtos.BlacklistWordDTO;
import com.infsis.socialpagebackend.moderation.dtos.BlacklistWordRequest;
import com.infsis.socialpagebackend.moderation.enums.BlacklistCategory;
import com.infsis.socialpagebackend.moderation.services.BlacklistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/moderation/blacklist")
public class BlacklistController {

    private final BlacklistService blacklistService;

    public BlacklistController(BlacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    /**
     * Lista todas las palabras almacenadas en BD (activas e inactivas).
     */
    @PreAuthorize("hasAuthority('MANAGE_BLACKLIST')")
    @GetMapping
    public List<BlacklistWordDTO> getAllDbWords() {
        return blacklistService.getAllDbWords();
    }

    /**
     * Vista de solo lectura de las palabras cargadas desde los archivos locales.
     */
    @PreAuthorize("hasAuthority('MANAGE_BLACKLIST')")
    @GetMapping("/file-words")
    public Map<BlacklistCategory, List<String>> getFileWords() {
        return blacklistService.getFileWords();
    }

    /**
     * Devuelve el conjunto completo de palabras activas en el trie (archivo + BD).
     * Útil para debug/auditoría.
     */
    @PreAuthorize("hasAuthority('MANAGE_BLACKLIST')")
    @GetMapping("/active")
    public Set<String> getActiveWords() {
        return blacklistService.getActiveWordSet();
    }

    /**
     * Añade una nueva palabra personalizada a la BD.
     * Si excludeFromFile=true, también anula esa palabra si estuviera en el archivo local.
     */
    @PreAuthorize("hasAuthority('MANAGE_BLACKLIST')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BlacklistWordDTO addWord(@Valid @RequestBody BlacklistWordRequest request) {
        return blacklistService.addWord(request);
    }

    /**
     * Activa o desactiva una palabra de la BD.
     */
    @PreAuthorize("hasAuthority('MANAGE_BLACKLIST')")
    @PatchMapping("/{id}/toggle")
    public BlacklistWordDTO toggleWord(@PathVariable Long id) {
        return blacklistService.toggleWord(id);
    }

    /**
     * Elimina una palabra de la BD.
     * Nota: las palabras del archivo local no se pueden eliminar desde aquí —
     * usar addWord con excludeFromFile=true para anularlas.
     */
    @PreAuthorize("hasAuthority('MANAGE_BLACKLIST')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWord(@PathVariable Long id) {
        blacklistService.deleteWord(id);
    }
}
