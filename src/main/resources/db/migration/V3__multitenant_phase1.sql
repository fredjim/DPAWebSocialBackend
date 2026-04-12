-- V3__multitenant_phase1.sql
-- Fase 1: Preparación del schema multitenant (Shared DB + Shared Schema)
-- Estrategia: institution como entidad raíz de tenant (institution_id como discriminador)
-- Todos los cambios son nullable en esta fase — no rompe funcionalidad existente

-- ─────────────────────────────────────────────────────────────
-- 1. TABLA institution — campos multitenant
-- ─────────────────────────────────────────────────────────────
ALTER TABLE public.institution
    ADD COLUMN IF NOT EXISTS slug          VARCHAR(100),
    ADD COLUMN IF NOT EXISTS custom_domain VARCHAR(200),
    ADD COLUMN IF NOT EXISTS status        VARCHAR(20)  DEFAULT 'ACTIVE',
    ADD COLUMN IF NOT EXISTS plan          VARCHAR(30)  DEFAULT 'FREE',
    ADD COLUMN IF NOT EXISTS max_users     INTEGER      DEFAULT 100,
    ADD COLUMN IF NOT EXISTS settings      JSONB        DEFAULT '{}';

-- Índices únicos en institution
CREATE UNIQUE INDEX IF NOT EXISTS idx_institution_slug
    ON public.institution(slug)
    WHERE slug IS NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS idx_institution_domain
    ON public.institution(custom_domain)
    WHERE custom_domain IS NOT NULL;

-- ─────────────────────────────────────────────────────────────
-- 2. TABLA users — tenant discriminator
-- ─────────────────────────────────────────────────────────────
ALTER TABLE public.users
    ADD COLUMN IF NOT EXISTS institution_id VARCHAR(36),
    ADD COLUMN IF NOT EXISTS is_root        BOOLEAN DEFAULT FALSE NOT NULL;

-- institution_id = NULL → usuario ROOT (acceso global)
-- institution_id = 'uuid' → usuario pertenece a esa institución

CREATE INDEX IF NOT EXISTS idx_users_institution_id
    ON public.users(institution_id);

-- ─────────────────────────────────────────────────────────────
-- 3. TABLAS CRÍTICAS — agregar institution_id (nullable)
-- ─────────────────────────────────────────────────────────────
ALTER TABLE public.event   ADD COLUMN IF NOT EXISTS institution_id VARCHAR(36);
ALTER TABLE public.groups  ADD COLUMN IF NOT EXISTS institution_id VARCHAR(36);
ALTER TABLE public.comment ADD COLUMN IF NOT EXISTS institution_id VARCHAR(36);
ALTER TABLE public.article ADD COLUMN IF NOT EXISTS institution_id VARCHAR(36);
ALTER TABLE public.link    ADD COLUMN IF NOT EXISTS institution_id VARCHAR(36);

-- ─────────────────────────────────────────────────────────────
-- 4. ROL ROOT
-- ─────────────────────────────────────────────────────────────
INSERT INTO public.role (name)
VALUES ('ROOT')
ON CONFLICT DO NOTHING;

-- ─────────────────────────────────────────────────────────────
-- 5. TABLA tenant_config — configuración por institución
-- ─────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS public.tenant_config (
    id             BIGINT      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    institution_id VARCHAR(36) NOT NULL,
    config_key     VARCHAR(100) NOT NULL,
    config_value   TEXT        NOT NULL,
    data_type      VARCHAR(20) DEFAULT 'STRING',
    description    VARCHAR(300),
    created_at     TIMESTAMPTZ DEFAULT NOW(),
    updated_at     TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(institution_id, config_key)
);

CREATE INDEX IF NOT EXISTS idx_tenant_config_institution
    ON public.tenant_config(institution_id);

-- ─────────────────────────────────────────────────────────────
-- 6. TABLA tenant_invitation — invitaciones de usuarios
-- ─────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS public.tenant_invitation (
    id             BIGINT      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    institution_id VARCHAR(36) NOT NULL,
    email          VARCHAR(100) NOT NULL,
    token          VARCHAR(200) NOT NULL UNIQUE,
    role_id        BIGINT,
    status         VARCHAR(20) DEFAULT 'PENDING',
    expires_at     TIMESTAMPTZ NOT NULL,
    created_at     TIMESTAMPTZ DEFAULT NOW(),
    accepted_at    TIMESTAMPTZ
);

CREATE INDEX IF NOT EXISTS idx_tenant_invitation_institution
    ON public.tenant_invitation(institution_id);

CREATE INDEX IF NOT EXISTS idx_tenant_invitation_email
    ON public.tenant_invitation(email);

-- ─────────────────────────────────────────────────────────────
-- Validación sugerida tras ejecutar esta migración:
-- SELECT column_name FROM information_schema.columns
--   WHERE table_name = 'institution' AND column_name = 'slug';
-- SELECT column_name FROM information_schema.columns
--   WHERE table_name = 'users' AND column_name = 'institution_id';
-- SELECT COUNT(*) FROM role WHERE name = 'ROOT';
-- ─────────────────────────────────────────────────────────────
