CREATE TABLE IF NOT EXISTS blacklist_word (
    id                BIGSERIAL PRIMARY KEY,
    word              VARCHAR(200)  NOT NULL UNIQUE,
    category          VARCHAR(20)   NOT NULL CHECK (category IN ('PROFANITY', 'HATE_SPEECH', 'SPAM', 'CUSTOM')),
    active            BOOLEAN       NOT NULL DEFAULT TRUE,
    exclude_from_file BOOLEAN       NOT NULL DEFAULT FALSE,
    notes             VARCHAR(300),
    created_at        TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_blacklist_word_active   ON blacklist_word(active);
CREATE INDEX idx_blacklist_word_category ON blacklist_word(category);
