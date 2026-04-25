-- V11__role_and_permission_flags.sql

ALTER TABLE public.permissions
    ADD COLUMN IF NOT EXISTS is_system_only BOOLEAN NOT NULL DEFAULT FALSE;

UPDATE public.permissions
SET is_system_only = TRUE
WHERE name_permission IN (
    'CREATE_USER_ADMIN',
    'VIEW_ALL_INSTITUTIONS',
    'CREATE_INSTITUTION',
    'DELETE_INSTITUTION'
);

ALTER TABLE public.role
    ADD COLUMN IF NOT EXISTS is_system_role BOOLEAN NOT NULL DEFAULT FALSE;

UPDATE public.role SET is_system_role = TRUE
WHERE name IN ('ROOT', 'ADMIN', 'STUDENT');
