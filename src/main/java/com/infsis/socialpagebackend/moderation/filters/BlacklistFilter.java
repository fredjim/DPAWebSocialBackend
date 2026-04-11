package com.infsis.socialpagebackend.moderation.filters;

import com.infsis.socialpagebackend.moderation.services.BlacklistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Filtro 1 del pipeline de moderación.
 *
 * Verifica si el texto contiene alguna palabra de la blacklist híbrida
 * (archivo local + BD). Usa Aho-Corasick para búsqueda en O(n).
 *
 * Latencia esperada: ~1ms
 * Costo: $0
 */
@Slf4j
@Component
public class BlacklistFilter {

    private final BlacklistService blacklistService;

    public BlacklistFilter(BlacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    /**
     * Evalúa si el texto contiene palabras prohibidas.
     *
     * @param text contenido del comentario o reply
     * @return resultado de la evaluación
     */
    public BlacklistResult evaluate(String text) {
        long start = System.currentTimeMillis();

        List<String> matches = blacklistService.findMatches(text);
        long elapsed = System.currentTimeMillis() - start;

        if (!matches.isEmpty()) {
            String reason = "Términos prohibidos encontrados: " + String.join(", ", matches);
            log.info("BlacklistFilter: REJECTED in {}ms — {}", elapsed, reason);
            return BlacklistResult.rejected(matches, reason, elapsed);
        }

        log.debug("BlacklistFilter: PASSED in {}ms", elapsed);
        return BlacklistResult.passed(elapsed);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Result inner class
    // ─────────────────────────────────────────────────────────────────────────

    public static class BlacklistResult {

        private final boolean rejected;
        private final List<String> matchedTerms;
        private final String reason;
        private final long processingTimeMs;

        private BlacklistResult(boolean rejected, List<String> matchedTerms,
                                String reason, long processingTimeMs) {
            this.rejected = rejected;
            this.matchedTerms = matchedTerms;
            this.reason = reason;
            this.processingTimeMs = processingTimeMs;
        }

        public static BlacklistResult rejected(List<String> terms, String reason, long ms) {
            return new BlacklistResult(true, terms, reason, ms);
        }

        public static BlacklistResult passed(long ms) {
            return new BlacklistResult(false, List.of(), null, ms);
        }

        public boolean isRejected() { return rejected; }
        public List<String> getMatchedTerms() { return matchedTerms; }
        public String getReason() { return reason; }
        public long getProcessingTimeMs() { return processingTimeMs; }
    }
}
