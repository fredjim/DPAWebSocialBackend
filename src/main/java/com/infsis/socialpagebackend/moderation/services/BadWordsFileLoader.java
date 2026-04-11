package com.infsis.socialpagebackend.moderation.services;

import com.infsis.socialpagebackend.moderation.enums.BlacklistCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Carga las palabras prohibidas desde los archivos locales en classpath.
 * Los archivos son: moderation/badwords-es.txt y moderation/badwords-en.txt
 *
 * Formato del archivo:
 *   # PROFANITY      <- marca inicio de categoría (ignorado si no coincide con enum)
 *   palabra1         <- palabra a añadir a la categoría actual
 *   # comentario     <- cualquier otra línea con # es ignorada
 *   (línea vacía)    <- ignorada
 */
@Slf4j
@Component
public class BadWordsFileLoader {

    private static final String[] FILES = {
            "moderation/badwords-es.txt",
            "moderation/badwords-en.txt"
    };

    /**
     * Carga todas las palabras de todos los archivos y las devuelve
     * como un mapa categoría → conjunto de palabras.
     */
    public Map<BlacklistCategory, Set<String>> loadByCategory() {
        Map<BlacklistCategory, Set<String>> result = new HashMap<>();
        for (BlacklistCategory cat : BlacklistCategory.values()) {
            result.put(cat, new HashSet<>());
        }

        for (String filePath : FILES) {
            loadFile(filePath, result);
        }

        log.info("BadWordsFileLoader: loaded {} PROFANITY, {} HATE_SPEECH, {} SPAM words from files",
                result.get(BlacklistCategory.PROFANITY).size(),
                result.get(BlacklistCategory.HATE_SPEECH).size(),
                result.get(BlacklistCategory.SPAM).size());

        return result;
    }

    /**
     * Devuelve todas las palabras del archivo como un Set plano (sin categoría).
     */
    public Set<String> loadAll() {
        Set<String> all = new HashSet<>();
        loadByCategory().values().forEach(all::addAll);
        return all;
    }

    private void loadFile(String filePath, Map<BlacklistCategory, Set<String>> result) {
        ClassPathResource resource = new ClassPathResource(filePath);

        if (!resource.exists()) {
            log.warn("Bad words file not found in classpath: {}", filePath);
            return;
        }

        BlacklistCategory currentCategory = BlacklistCategory.PROFANITY; // categoría por defecto

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                // Ignorar líneas vacías
                if (line.isEmpty()) continue;

                // Detectar marcador de categoría: # PROFANITY / # HATE_SPEECH / # SPAM
                if (line.startsWith("#")) {
                    String possibleCategory = line.substring(1).trim().toUpperCase();
                    try {
                        currentCategory = BlacklistCategory.valueOf(possibleCategory);
                        log.debug("Switching to category {} in file {}", currentCategory, filePath);
                    } catch (IllegalArgumentException e) {
                        // Es un comentario normal, no una categoría — ignorar
                    }
                    continue;
                }

                // Es una palabra/frase: añadirla a la categoría actual
                // Eliminar sufijos como "(insulto)" o "(slur)" usados en el archivo
                String word = line.replaceAll("\\s*\\(.*?\\)\\s*$", "").trim().toLowerCase();
                if (!word.isEmpty()) {
                    result.get(currentCategory).add(word);
                }
            }

            log.info("Loaded {} lines from {}", lineNumber, filePath);

        } catch (IOException e) {
            log.error("Error reading bad words file: {}", filePath, e);
        }
    }
}
