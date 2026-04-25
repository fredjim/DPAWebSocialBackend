-- V10__admin_management_permissions.sql
-- Adds CREATE_USER_ADMIN permission and restricts institution CRUD to ROOT only.
-- All INSERTs use ON CONFLICT DO NOTHING → idempotente.

-- ─────────────────────────────────────────────────────────────
-- 1. NEW PERMISSION: CREATE_USER_ADMIN
-- ─────────────────────────────────────────────────────────────
INSERT INTO public.permissions (id_permission, name_permission) VALUES
    (45, 'CREATE_USER_ADMIN')
ON CONFLICT DO NOTHING;

SELECT setval(pg_get_serial_sequence('public.permissions', 'id_permission'),
              (SELECT MAX(id_permission) FROM public.permissions));

-- ─────────────────────────────────────────────────────────────
-- 2. ASSIGN CREATE_USER_ADMIN TO ROOT ONLY
-- ─────────────────────────────────────────────────────────────
INSERT INTO public.rol_permissions (role_id, permission_id) VALUES
    (1, 45)  -- ROOT → CREATE_USER_ADMIN
ON CONFLICT DO NOTHING;

-- ─────────────────────────────────────────────────────────────
-- 3. REMOVE INSTITUTION CRUD PERMISSIONS FROM ADMIN ROLE
--    These operations are exclusive to ROOT (global scope).
--    ADMIN operates within a single tenant and must not
--    create, modify, or delete institutions.
--      id=1  VIEW_ALL_INSTITUTIONS
--      id=2  CREATE_INSTITUTION
--      id=4  DELETE_INSTITUTION
-- ─────────────────────────────────────────────────────────────
DELETE FROM public.rol_permissions
WHERE role_id = 2
  AND permission_id IN (1, 2, 4);
