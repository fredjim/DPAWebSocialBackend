-- V17__user_management.sql
-- Agrega columna enabled a users y los 3 permisos granulares de gestión de usuarios.

-- ─────────────────────────────────────────────────────────────
-- 1. COLUMNA enabled EN users
--    DEFAULT TRUE → todos los usuarios existentes quedan habilitados.
-- ─────────────────────────────────────────────────────────────
ALTER TABLE public.users
    ADD COLUMN IF NOT EXISTS enabled BOOLEAN NOT NULL DEFAULT TRUE;

-- ─────────────────────────────────────────────────────────────
-- 2. NUEVOS PERMISOS
--    Siguiente ID libre después de id=45 (CREATE_USER_ADMIN).
-- ─────────────────────────────────────────────────────────────
INSERT INTO public.permissions (id_permission, name_permission) VALUES
    (46, 'VIEW_USERS'),
    (47, 'EDIT_USER'),
    (48, 'DELETE_USER')
ON CONFLICT DO NOTHING;

-- Marcar como system_only → solo ROOT/ADMIN pueden tenerlos,
-- impide que un admin los asigne a roles personalizados de nivel bajo.
UPDATE public.permissions
SET is_system_only = TRUE
WHERE name_permission IN ('VIEW_USERS', 'EDIT_USER', 'DELETE_USER');

SELECT setval(pg_get_serial_sequence('public.permissions', 'id_permission'),
              (SELECT MAX(id_permission) FROM public.permissions));

-- ─────────────────────────────────────────────────────────────
-- 3. ASIGNAR PERMISOS A ROOT (role_id = 1)
-- ─────────────────────────────────────────────────────────────
INSERT INTO public.rol_permissions (role_id, permission_id) VALUES
    (1, 46),   -- ROOT → VIEW_USERS
    (1, 47),   -- ROOT → EDIT_USER
    (1, 48)    -- ROOT → DELETE_USER
ON CONFLICT DO NOTHING;

-- ─────────────────────────────────────────────────────────────
-- 4. ASIGNAR PERMISOS A ADMIN (role_id = 2)
-- ─────────────────────────────────────────────────────────────
INSERT INTO public.rol_permissions (role_id, permission_id) VALUES
    (2, 46),   -- ADMIN → VIEW_USERS
    (2, 47),   -- ADMIN → EDIT_USER
    (2, 48)    -- ADMIN → DELETE_USER
ON CONFLICT DO NOTHING;
