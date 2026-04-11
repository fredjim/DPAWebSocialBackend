package com.infsis.socialpagebackend.moderation.dtos;

import com.infsis.socialpagebackend.moderation.enums.ModerationDecision;
import com.infsis.socialpagebackend.moderation.enums.ModerationFilterType;
import lombok.Getter;

/**
 * Resultado final del pipeline de moderación.
 * Contiene la decisión, qué filtro la tomó, la razón y métricas.
 */
@Getter
public class ModerationResponse {

    private final ModerationDecision decision;
    private final ModerationFilterType filterUsed;
    private final String reason;
    private final double confidence;
    private final long totalProcessingTimeMs;

    private ModerationResponse(ModerationDecision decision, ModerationFilterType filterUsed,
                               String reason, double confidence, long totalMs) {
        this.decision = decision;
        this.filterUsed = filterUsed;
        this.reason = reason;
        this.confidence = confidence;
        this.totalProcessingTimeMs = totalMs;
    }

    public static ModerationResponse approved(long totalMs) {
        return new ModerationResponse(ModerationDecision.APPROVED, null, null, 1.0, totalMs);
    }

    public static ModerationResponse rejected(ModerationFilterType filter,
                                              String reason, double confidence, long totalMs) {
        return new ModerationResponse(ModerationDecision.REJECTED, filter, reason, confidence, totalMs);
    }

    public static ModerationResponse needsReview(ModerationFilterType filter,
                                                  String reason, double confidence, long totalMs) {
        return new ModerationResponse(ModerationDecision.NEEDS_REVIEW, filter, reason, confidence, totalMs);
    }

    public boolean isApproved()    { return decision == ModerationDecision.APPROVED; }
    public boolean isRejected()    { return decision == ModerationDecision.REJECTED; }
    public boolean isNeedsReview() { return decision == ModerationDecision.NEEDS_REVIEW; }
}
