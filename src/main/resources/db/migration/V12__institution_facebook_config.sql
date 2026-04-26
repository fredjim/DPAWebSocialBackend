-- V12__institution_facebook_config.sql
-- Crea la tabla de credenciales de Facebook por institución.
-- El permiso MANAGE_FACEBOOK_CONFIG es exclusivo del rol ROOT.

-- ─────────────────────────────────────────────────────────────
-- 1. TABLA DE CREDENCIALES
-- ─────────────────────────────────────────────────────────────
CREATE SEQUENCE IF NOT EXISTS public.institution_facebook_config_seq
    START WITH 1 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS public.institution_facebook_config (
    id                  INTEGER      NOT NULL PRIMARY KEY DEFAULT nextval('public.institution_facebook_config_seq'),
    uuid                VARCHAR(36)  NOT NULL UNIQUE,
    institution_id      VARCHAR(36)  NOT NULL
        REFERENCES public.institution(uuid) ON DELETE CASCADE,
    page_id             VARCHAR(50)  NOT NULL,
    access_token        TEXT         NOT NULL,
    enabled             BOOLEAN      NOT NULL DEFAULT true,
    created_date        TIMESTAMP(6) WITHOUT TIME ZONE,
    last_modified_date  TIMESTAMP(6) WITHOUT TIME ZONE,
    CONSTRAINT uq_institution_facebook UNIQUE (institution_id)
);

-- ─────────────────────────────────────────────────────────────
-- 2. PERMISO NUEVO
-- ─────────────────────────────────────────────────────────────
INSERT INTO public.permissions (id_permission, name_permission) VALUES
    (46, 'MANAGE_FACEBOOK_CONFIG')
ON CONFLICT DO NOTHING;

SELECT setval(
    pg_get_serial_sequence('public.permissions', 'id_permission'),
    (SELECT MAX(id_permission) FROM public.permissions)
);

-- ─────────────────────────────────────────────────────────────
-- 3. ASIGNAR PERMISO SOLO A ROOT (role_id = 1)
-- ─────────────────────────────────────────────────────────────
INSERT INTO public.rol_permissions (role_id, permission_id) VALUES
    (1, 46)
ON CONFLICT DO NOTHING;
