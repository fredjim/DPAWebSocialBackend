CREATE TABLE IF NOT EXISTS moderation_result (
    id                 BIGSERIAL    PRIMARY KEY,
    uuid               VARCHAR(36)  NOT NULL UNIQUE,
    content_uuid       VARCHAR(36)  NOT NULL,
    content_type       VARCHAR(10)  NOT NULL CHECK (content_type IN ('COMMENT', 'REPLY')),
    decision           VARCHAR(15)  NOT NULL CHECK (decision IN ('APPROVED', 'REJECTED', 'NEEDS_REVIEW')),
    filter_used        VARCHAR(15)  CHECK (filter_used IN ('BLACKLIST', 'TOXICITY_API', 'LLM', 'MANUAL')),
    reason             VARCHAR(500),
    confidence         DOUBLE PRECISION,
    processing_time_ms BIGINT,
    created_at         TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_moderation_result_content_uuid ON moderation_result(content_uuid);
CREATE INDEX idx_moderation_result_decision      ON moderation_result(decision);
CREATE INDEX idx_moderation_result_content_type  ON moderation_result(content_type);
