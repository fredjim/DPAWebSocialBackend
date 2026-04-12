-- Phase 3: Unify article_media into media

-- 1. Add article_id and deleted columns to media
ALTER TABLE public.media
    ADD COLUMN IF NOT EXISTS article_id character varying(36);

ALTER TABLE public.media
    ADD COLUMN IF NOT EXISTS deleted boolean NOT NULL DEFAULT false;

ALTER TABLE public.media
    ADD CONSTRAINT fk_media_article
    FOREIGN KEY (article_id) REFERENCES public.article(uuid)
    ON DELETE CASCADE;

-- 2. Migrate article_media rows into media
INSERT INTO public.media (uuid, file_name, file_type, number, article_id, uploaded_file_id, deleted, created_date, last_modified_date)
SELECT uuid, file_name, file_type, number, article_id, uploaded_file_id, deleted, created_date, last_modified_date
FROM public.article_media
ON CONFLICT (uuid) DO NOTHING;

-- 3. Drop article_media table and its sequence
DROP TABLE IF EXISTS public.article_media;
DROP SEQUENCE IF EXISTS public.article_media_seq;
