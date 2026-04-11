package com.infsis.socialpagebackend.moderation.services;

import com.infsis.socialpagebackend.moderation.configuration.ModerationProperties;
import com.infsis.socialpagebackend.moderation.dtos.ModerationResponse;
import com.infsis.socialpagebackend.moderation.enums.ContentType;
import com.infsis.socialpagebackend.moderation.enums.ModerationFilterType;
import com.infsis.socialpagebackend.moderation.filters.BlacklistFilter;
import com.infsis.socialpagebackend.moderation.filters.LlmModerationFilter;
import com.infsis.socialpagebackend.moderation.filters.ToxicityApiFilter;
import com.infsis.socialpagebackend.moderation.models.ModerationResult;
import com.infsis.socialpagebackend.moderation.repositories.ModerationResultRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Orquestador del pipeline de moderación automática.
 *
 * Ejecuta los 3 filtros en cascada y retorna al primer REJECTED o NEEDS_REVIEW.
 * Si los 3 filtros pasan → APPROVED.
 *
 * Flujo:
 *   Texto
 *    │
 *    ├─► [F1] Blacklist  (~1ms)      → REJECTED si contiene palabra prohibida
 *    │
 *    ├─► [F2] Perspective API (~200ms) → REJECTED si score ≥ umbral
 *    │
 *    └─► [F3] LLM (~1-3s)           → REJECTED | NEEDS_REVIEW | APPROVED
 *
 * Cada filtro puede estar habilitado/deshabilitado en application.properties.
 * Fallos de infraestructura en F2/F3 son fail-open (no bloquean al usuario).
 */
@Slf4j
@Service
public class ModerationPipeline {

    private final BlacklistFilter blacklistFilter;
    private final ToxicityApiFilter toxicityApiFilter;
    private final LlmModerationFilter llmModerationFilter;
    private final ModerationResultRepository resultRepository;
    private final ModerationProperties properties;

    public ModerationPipeline(BlacklistFilter blacklistFilter,
                              ToxicityApiFilter toxicityApiFilter,
                              LlmModerationFilter llmModerationFilter,
                              ModerationResultRepository resultRepository,
                              ModerationProperties properties) {
        this.blacklistFilter = blacklistFilter;
        this.toxicityApiFilter = toxicityApiFilter;
        this.llmModerationFilter = llmModerationFilter;
        this.resultRepository = resultRepository;
        this.properties = properties;
    }

    /**
     * Ejecuta el pipeline completo sobre el texto dado.
     *
     * @param text texto del comentario o reply
     * @return decisión final del pipeline
     */
    public ModerationResponse moderate(String text) {
        long pipelineStart = System.currentTimeMillis();

        log.info("ModerationPipeline: starting evaluation for text of {} chars", text.length());

        // ── Filtro 1: Blacklist ──────────────────────────────────────────────
        if (properties.getBlacklist().isEnabled()) {
            BlacklistFilter.BlacklistResult blacklistResult = blacklistFilter.evaluate(text);

            if (blacklistResult.isRejected()) {
                long total = System.currentTimeMillis() - pipelineStart;
                log.info("ModerationPipeline: REJECTED by BLACKLIST in {}ms", total);
                return ModerationResponse.rejected(
                        ModerationFilterType.BLACKLIST,
                        blacklistResult.getReason(),
                        1.0,
                        total
                );
            }
        } else {
            log.debug("ModerationPipeline: BLACKLIST filter disabled — skipping");
        }

        // ── Filtro 2: Perspective API ────────────────────────────────────────
        if (properties.getPerspective().isEnabled()) {
            ToxicityApiFilter.ToxicityResult toxicityResult = toxicityApiFilter.evaluate(text);

            if (toxicityResult.isRejected()) {
                long total = System.currentTimeMillis() - pipelineStart;
                log.info("ModerationPipeline: REJECTED by TOXICITY_API in {}ms", total);
                return ModerationResponse.rejected(
                        ModerationFilterType.TOXICITY_API,
                        toxicityResult.getReason(),
                        toxicityResult.getToxicityScore(),
                        total
                );
            }
            // API_ERROR ya es fail-open dentro de ToxicityApiFilter → continúa
        } else {
            log.debug("ModerationPipeline: TOXICITY_API filter disabled — skipping");
        }

        // ── Filtro 3: LLM ────────────────────────────────────────────────────
        if (properties.getLlm().isEnabled()) {
            LlmModerationFilter.LlmResult llmResult = llmModerationFilter.evaluate(text);

            if (llmResult.isRejected()) {
                long total = System.currentTimeMillis() - pipelineStart;
                log.info("ModerationPipeline: REJECTED by LLM ({}) in {}ms",
                        llmResult.getProviderName(), total);
                return ModerationResponse.rejected(
                        ModerationFilterType.LLM,
                        llmResult.getReason(),
                        llmResult.getConfidence(),
                        total
                );
            }

            if (llmResult.isNeedsReview()) {
                long total = System.currentTimeMillis() - pipelineStart;
                log.info("ModerationPipeline: NEEDS_REVIEW by LLM ({}) in {}ms",
                        llmResult.getProviderName(), total);
                return ModerationResponse.needsReview(
                        ModerationFilterType.LLM,
                        llmResult.getReason(),
                        llmResult.getConfidence(),
                        total
                );
            }
            // PROVIDER_ERROR es fail-open → aprueba
        } else {
            log.debug("ModerationPipeline: LLM filter disabled — skipping");
        }

        // ── Todos los filtros pasaron → APPROVED ─────────────────────────────
        long total = System.currentTimeMillis() - pipelineStart;
        log.info("ModerationPipeline: APPROVED — all filters passed in {}ms", total);
        return ModerationResponse.approved(total);
    }

    /**
     * Guarda el resultado de moderación en la BD para auditoría.
     * Llamar DESPUÉS de guardar el comment/reply (para tener su UUID).
     *
     * @param response    resultado del pipeline
     * @param contentUuid UUID del comment o reply guardado
     * @param contentType COMMENT o REPLY
     */
    public void saveResult(ModerationResponse response, String contentUuid, ContentType contentType) {
        try {
            ModerationResult result = new ModerationResult();
            result.setContentUuid(contentUuid);
            result.setContentType(contentType);
            result.setDecision(response.getDecision());
            result.setFilterUsed(response.getFilterUsed());
            result.setReason(response.getReason());
            result.setConfidence(response.getConfidence());
            result.setProcessingTimeMs(response.getTotalProcessingTimeMs());

            resultRepository.save(result);
            log.debug("ModerationPipeline: result saved for contentUuid={}", contentUuid);

        } catch (Exception e) {
            // No fallar el flujo principal por un error de auditoría
            log.error("ModerationPipeline: failed to save moderation result for {}: {}",
                    contentUuid, e.getMessage());
        }
    }
}
