-- Phase 2: Add FK uploaded_file_id to media and article_media tables

-- 1. Add nullable uploaded_file_id column to media
ALTER TABLE public.media
    ADD COLUMN IF NOT EXISTS uploaded_file_id integer;

ALTER TABLE public.media
    ADD CONSTRAINT fk_media_uploaded_file
    FOREIGN KEY (uploaded_file_id) REFERENCES public.uploaded_file(id)
    ON DELETE SET NULL;

-- 2. Populate uploaded_file_id from matching url_resource
-- UPDATE public.media m
-- SET uploaded_file_id = uf.id
-- FROM public.uploaded_file uf
-- WHERE m.file_path = uf.url_resource;

-- 3. Drop file_path from media
-- ALTER TABLE public.media
   -- DROP COLUMN IF EXISTS file_path;

-- 4. Add nullable uploaded_file_id column to article_media
ALTER TABLE public.article_media
    ADD COLUMN IF NOT EXISTS uploaded_file_id integer;

ALTER TABLE public.article_media
    ADD CONSTRAINT fk_article_media_uploaded_file
    FOREIGN KEY (uploaded_file_id) REFERENCES public.uploaded_file(id)
    ON DELETE SET NULL;

-- 5. Populate uploaded_file_id from matching url_resource
-- UPDATE public.article_media am
-- SET uploaded_file_id = uf.id
-- FROM public.uploaded_file uf
-- WHERE am.file_path = uf.url_resource;

-- 6. Drop file_path from article_media
-- ALTER TABLE public.article_media
   -- DROP COLUMN IF EXISTS file_path;
