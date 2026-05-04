-- Add FK columns linking institution and users to their uploaded profile/cover files.
-- Columns are nullable to preserve existing rows and allow ON DELETE SET NULL behavior.

ALTER TABLE institution
    ADD COLUMN logo_file_id        INTEGER UNIQUE REFERENCES uploaded_file(id) ON DELETE SET NULL,
    ADD COLUMN background_file_id  INTEGER UNIQUE REFERENCES uploaded_file(id) ON DELETE SET NULL;

-- Backfill FKs from existing string paths where a matching uploaded_file record exists
UPDATE institution i
SET logo_file_id = (
    SELECT uf.id FROM uploaded_file uf WHERE uf.url_resource = i.logo_url LIMIT 1
)
WHERE i.logo_url IS NOT NULL AND i.logo_url != '';

UPDATE institution i
SET background_file_id = (
    SELECT uf.id FROM uploaded_file uf WHERE uf.url_resource = i.background_url LIMIT 1
)
WHERE i.background_url IS NOT NULL AND i.background_url != '';

ALTER TABLE users
    ADD COLUMN photo_profile_file_id  INTEGER UNIQUE REFERENCES uploaded_file(id) ON DELETE SET NULL,
    ADD COLUMN photo_cover_file_id    INTEGER UNIQUE REFERENCES uploaded_file(id) ON DELETE SET NULL;

UPDATE users u
SET photo_profile_file_id = (
    SELECT uf.id FROM uploaded_file uf WHERE uf.url_resource = u.photo_profile_path LIMIT 1
)
WHERE u.photo_profile_path IS NOT NULL AND u.photo_profile_path != '';

UPDATE users u
SET photo_cover_file_id = (
    SELECT uf.id FROM uploaded_file uf WHERE uf.url_resource = u.photo_cover_path LIMIT 1
)
WHERE u.photo_cover_path IS NOT NULL AND u.photo_cover_path != '';
