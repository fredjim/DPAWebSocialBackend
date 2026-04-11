package com.infsis.socialpagebackend.moderation.services;

import com.infsis.socialpagebackend.moderation.dtos.BlacklistWordDTO;
import com.infsis.socialpagebackend.moderation.dtos.BlacklistWordRequest;
import com.infsis.socialpagebackend.moderation.enums.BlacklistCategory;
import com.infsis.socialpagebackend.moderation.models.BlacklistWord;
import com.infsis.socialpagebackend.moderation.repositories.BlacklistWordRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio de blacklist híbrido.
 *
 * Fuente 1 — Archivo local (badwords-es.txt / badwords-en.txt):
 *   Palabras base en español e inglés. Inmutables sin deploy.
 *
 * Fuente 2 — Base de datos (tabla blacklist_word):
 *   Palabras añadidas/gestionadas por el moderador en tiempo real.
 *   Puede incluir entradas con excludeFromFile=true para anular palabras del archivo.
 *
 * El trie Aho-Corasick se construye a partir de la unión de ambas fuentes
 * y se reconstruye automáticamente cada vez que la BD cambia.
 */
@Slf4j
@Service
public class BlacklistService {

    private final BlacklistWordRepository repository;
    private final BadWordsFileLoader fileLoader;

    // Trie inmutable una vez construido — reads son thread-safe
    private volatile Trie blacklistTrie;

    // Vista legible del conjunto activo (para debug/admin)
    private volatile Set<String> activeWordSet = new HashSet<>();

    public BlacklistService(BlacklistWordRepository repository, BadWordsFileLoader fileLoader) {
        this.repository = repository;
        this.fileLoader = fileLoader;
    }

    @PostConstruct
    public void init() {
        rebuildTrie();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Evaluación (usada por BlacklistFilter)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Evalúa el texto contra el blacklist activo.
     *
     * @param text texto a evaluar (comment/reply content)
     * @return lista de términos encontrados (vacía si pasa el filtro)
     */
    public List<String> findMatches(String text) {
        if (blacklistTrie == null || text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        String normalized = normalize(text);
        Collection<Emit> emits = blacklistTrie.parseText(normalized);

        return emits.stream()
                .map(Emit::getKeyword)
                .distinct()
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // CRUD para el moderador (BD)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Añade una palabra nueva a la BD y reconstruye el trie.
     */
    @Transactional
    public BlacklistWordDTO addWord(BlacklistWordRequest request) {
        String word = request.getWord().trim().toLowerCase();

        if (repository.existsByWordIgnoreCase(word)) {
            throw new IllegalArgumentException("La palabra ya existe en la blacklist: " + word);
        }

        BlacklistWord entity = new BlacklistWord();
        entity.setWord(word);
        entity.setCategory(request.getCategory());
        entity.setActive(true);
        entity.setExcludeFromFile(request.isExcludeFromFile());
        entity.setNotes(request.getNotes());

        BlacklistWord saved = repository.save(entity);
        log.info("Blacklist: word added by moderator — '{}' [{}]", word, request.getCategory());

        rebuildTrie();
        return toDTO(saved);
    }

    /**
     * Activa o desactiva una palabra de la BD.
     */
    @Transactional
    public BlacklistWordDTO toggleWord(Long id) {
        BlacklistWord entity = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Blacklist word not found: " + id));

        entity.setActive(!entity.isActive());
        BlacklistWord saved = repository.save(entity);

        log.info("Blacklist: word '{}' toggled to active={}", saved.getWord(), saved.isActive());
        rebuildTrie();
        return toDTO(saved);
    }

    /**
     * Elimina una palabra de la BD.
     */
    @Transactional
    public void deleteWord(Long id) {
        BlacklistWord entity = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Blacklist word not found: " + id));

        repository.delete(entity);
        log.info("Blacklist: word '{}' deleted", entity.getWord());
        rebuildTrie();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Consultas de administración
    // ─────────────────────────────────────────────────────────────────────────

    /** Lista todas las palabras almacenadas en BD (activas e inactivas). */
    public List<BlacklistWordDTO> getAllDbWords() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /** Lista las palabras cargadas desde archivos locales, organizadas por categoría. */
    public Map<BlacklistCategory, List<String>> getFileWords() {
        Map<BlacklistCategory, Set<String>> byCategory = fileLoader.loadByCategory();
        Map<BlacklistCategory, List<String>> result = new LinkedHashMap<>();
        byCategory.forEach((cat, words) -> {
            List<String> sorted = new ArrayList<>(words);
            Collections.sort(sorted);
            result.put(cat, sorted);
        });
        return result;
    }

    /** Devuelve el conjunto completo de palabras activas actualmente en el trie. */
    public Set<String> getActiveWordSet() {
        return Collections.unmodifiableSet(activeWordSet);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Construcción del trie
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Reconstruye el trie Aho-Corasick a partir de:
     *   1. Palabras del archivo local
     *   2. Palabras activas en BD
     *   3. Menos las palabras excluidas (excludeFromFile=true en BD)
     *
     * Llamado en @PostConstruct y después de cada cambio en BD.
     */
    synchronized void rebuildTrie() {
        long start = System.currentTimeMillis();

        // Fuente 1: archivo local
        Set<String> fileWords = fileLoader.loadAll();

        // Fuente 2: BD activas (solo active=true, excludeFromFile=false)
        List<BlacklistWord> dbEntries = repository.findAllByActiveTrue();

        Set<String> dbWords = dbEntries.stream()
                .filter(w -> !w.isExcludeFromFile())
                .map(w -> w.getWord().trim().toLowerCase())
                .collect(Collectors.toSet());

        // Exclusiones: entradas en BD con excludeFromFile=true
        Set<String> exclusions = repository.findAllByExcludeFromFileTrue().stream()
                .map(w -> normalize(w.getWord()))
                .collect(Collectors.toSet());

        // Unión de fuentes menos exclusiones
        Set<String> merged = new HashSet<>(fileWords);
        merged.addAll(dbWords);

        // Normalizar y quitar exclusiones
        Set<String> normalizedWords = merged.stream()
                .map(this::normalize)
                .filter(w -> !w.isBlank())
                .filter(w -> !exclusions.contains(w))
                .collect(Collectors.toSet());

        this.activeWordSet = normalizedWords;

        if (normalizedWords.isEmpty()) {
            this.blacklistTrie = null;
            log.warn("Blacklist trie rebuilt with 0 words — filter is effectively disabled");
            return;
        }

        this.blacklistTrie = Trie.builder()
                .addKeywords(normalizedWords)
                .ignoreCase()
                .ignoreOverlaps()
                .build();

        long elapsed = System.currentTimeMillis() - start;
        log.info("Blacklist trie rebuilt: {} words ({} from file, {} from DB, {} exclusions) in {}ms",
                normalizedWords.size(), fileWords.size(), dbWords.size(), exclusions.size(), elapsed);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Utilidades
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Normaliza el texto para la búsqueda:
     * - Minúsculas
     * - Elimina tildes/acentos (puta = püta = pùta)
     * - Elimina caracteres no alfanuméricos (p.u.t.a = puta)
     * - Colapsa caracteres repetidos (puuuuta = puta)
     */
    String normalize(String text) {
        if (text == null) return "";
        return StringUtils.stripAccents(text.toLowerCase())
                .replaceAll("[^a-z0-9\\s]", " ")
                .replaceAll("(.)\\1{2,}", "$1")
                .trim();
    }

    private BlacklistWordDTO toDTO(BlacklistWord entity) {
        BlacklistWordDTO dto = new BlacklistWordDTO();
        dto.setId(entity.getId());
        dto.setWord(entity.getWord());
        dto.setCategory(entity.getCategory());
        dto.setActive(entity.isActive());
        dto.setExcludeFromFile(entity.isExcludeFromFile());
        dto.setNotes(entity.getNotes());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}
