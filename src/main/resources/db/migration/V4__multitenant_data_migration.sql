-- V4__multitenant_data_migration.sql
-- Fase 2: Migración de datos multitenant
-- Objetivo: poblar institution_id en tablas críticas, agregar slug, FKs y NOT NULL
-- IMPORTANTE: ejecutar DESPUÉS de V3 y con backup previo

-- ─────────────────────────────────────────────────────────────
-- 1. INSTITUTION — generar slug
-- ─────────────────────────────────────────────────────────────
UPDATE public.institution
SET slug = LOWER(REGEXP_REPLACE(
               REGEXP_REPLACE(name, '[áàäâ]', 'a', 'g'),
           '[^a-z0-9]+', '-', 'g'))
WHERE slug IS NULL;

-- Recortar guiones al inicio/fin del slug
UPDATE public.institution
SET slug = TRIM(BOTH '-' FROM slug)
WHERE slug IS NOT NULL;

-- ─────────────────────────────────────────────────────────────
-- 2. COMMENT — institution_id desde su post
-- ─────────────────────────────────────────────────────────────
UPDATE public.comment c
SET institution_id = p.institution_id
FROM public.post p
WHERE c.post_id = p.id
  AND c.institution_id IS NULL;

-- ─────────────────────────────────────────────────────────────
-- 3. ARTICLE — institution_id desde su section
-- ─────────────────────────────────────────────────────────────
UPDATE public.article a
SET institution_id = s.institution_id
FROM public.section s
WHERE a.section_id = s.uuid
  AND a.institution_id IS NULL;

-- ─────────────────────────────────────────────────────────────
-- 4. GROUPS — backfill con institución única
--    (sistema actual tiene una sola institución)
-- ─────────────────────────────────────────────────────────────
UPDATE public.groups g
SET institution_id = (SELECT uuid FROM public.institution WHERE deleted = false LIMIT 1)
WHERE g.institution_id IS NULL;

-- ─────────────────────────────────────────────────────────────
-- 5. USERS — backfill con institución única
--    Los usuarios sin roles o con email de sistema permanecen sin institution_id
--    para poder ser promovidos a ROOT en el futuro
-- ─────────────────────────────────────────────────────────────
UPDATE public.users u
SET institution_id = (SELECT uuid FROM public.institution WHERE deleted = false LIMIT 1)
WHERE u.institution_id IS NULL;

-- ─────────────────────────────────────────────────────────────
-- 6. VALIDACIÓN antes de aplicar NOT NULL
-- ─────────────────────────────────────────────────────────────
DO $$
DECLARE
    v_comments_sin_tenant  INTEGER;
    v_articles_sin_tenant  INTEGER;
    v_groups_sin_tenant    INTEGER;
BEGIN
    SELECT COUNT(*) INTO v_comments_sin_tenant FROM public.comment WHERE institution_id IS NULL;
    SELECT COUNT(*) INTO v_articles_sin_tenant FROM public.article  WHERE institution_id IS NULL;
    SELECT COUNT(*) INTO v_groups_sin_tenant   FROM public.groups   WHERE institution_id IS NULL;

    IF v_comments_sin_tenant > 0 THEN
        RAISE EXCEPTION 'MIGRACIÓN FALLIDA: % comentarios sin institution_id', v_comments_sin_tenant;
    END IF;
    IF v_articles_sin_tenant > 0 THEN
        RAISE EXCEPTION 'MIGRACIÓN FALLIDA: % artículos sin institution_id', v_articles_sin_tenant;
    END IF;
    IF v_groups_sin_tenant > 0 THEN
        RAISE EXCEPTION 'MIGRACIÓN FALLIDA: % grupos sin institution_id', v_groups_sin_tenant;
    END IF;

    RAISE NOTICE 'Validación OK: todos los registros tienen institution_id';
END;
$$;

-- ─────────────────────────────────────────────────────────────
-- 8. NOT NULL en tablas críticas
--    (users queda nullable — los ROOT tienen institution_id = NULL)
-- ─────────────────────────────────────────────────────────────
ALTER TABLE public.comment ALTER COLUMN institution_id SET NOT NULL;
ALTER TABLE public.article ALTER COLUMN institution_id SET NOT NULL;
ALTER TABLE public.groups  ALTER COLUMN institution_id SET NOT NULL;

-- ─────────────────────────────────────────────────────────────
-- 9. FOREIGN KEY constraints
-- ─────────────────────────────────────────────────────────────
ALTER TABLE public.comment
    ADD CONSTRAINT fk_comment_institution
    FOREIGN KEY (institution_id) REFERENCES public.institution(uuid);

ALTER TABLE public.article
    ADD CONSTRAINT fk_article_institution
    FOREIGN KEY (institution_id) REFERENCES public.institution(uuid);

ALTER TABLE public.groups
    ADD CONSTRAINT fk_groups_institution
    FOREIGN KEY (institution_id) REFERENCES public.institution(uuid);

ALTER TABLE public.users
    ADD CONSTRAINT fk_users_institution
    FOREIGN KEY (institution_id) REFERENCES public.institution(uuid);

-- ─────────────────────────────────────────────────────────────
-- 10. ÍNDICES de performance
-- ─────────────────────────────────────────────────────────────
CREATE INDEX IF NOT EXISTS idx_comment_institution_id ON public.comment(institution_id);
CREATE INDEX IF NOT EXISTS idx_article_institution_id ON public.article(institution_id);
CREATE INDEX IF NOT EXISTS idx_groups_institution_id  ON public.groups(institution_id);
CREATE INDEX IF NOT EXISTS idx_users_institution_id   ON public.users(institution_id);

-- Índice compuesto para búsquedas frecuentes por tenant + estado
CREATE INDEX IF NOT EXISTS idx_comment_inst_state ON public.comment(institution_id, state);

-- ─────────────────────────────────────────────────────────────
-- Consultas de verificación post-migración (ejecutar manualmente):
--
-- SELECT 'posts sin tenant'    AS tabla, COUNT(*) FROM public.post    WHERE institution_id IS NULL
-- UNION ALL
-- SELECT 'comments sin tenant',          COUNT(*) FROM public.comment  WHERE institution_id IS NULL
-- UNION ALL
-- SELECT 'articles sin tenant',          COUNT(*) FROM public.article  WHERE institution_id IS NULL
-- UNION ALL
-- SELECT 'groups sin tenant',            COUNT(*) FROM public.groups   WHERE institution_id IS NULL
-- UNION ALL
-- SELECT 'users sin tenant',             COUNT(*) FROM public.users    WHERE institution_id IS NULL;
-- ─────────────────────────────────────────────────────────────
