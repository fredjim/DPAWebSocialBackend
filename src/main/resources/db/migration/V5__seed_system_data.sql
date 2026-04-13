-- V5__seed_system_data.sql
-- Datos obligatorios del sistema — corren en TODOS los ambientes (dev y prod)
-- Regla: solo datos que el sistema NO puede funcionar sin ellos
-- Todos los INSERTs usan ON CONFLICT DO NOTHING → idempotente

-- ─────────────────────────────────────────────────────────────
-- 1. ROLES DEL SISTEMA (solo 3)
-- ─────────────────────────────────────────────────────────────
INSERT INTO public.role (id_role, name) VALUES
    (1, 'ROOT'),
    (2, 'ADMIN'),
    (3, 'STUDENT')
ON CONFLICT DO NOTHING;

-- ─────────────────────────────────────────────────────────────
-- 2. PERMISOS (extraídos de @PreAuthorize en todos los controllers)
-- ─────────────────────────────────────────────────────────────
INSERT INTO public.permissions (id_permission, name_permission) VALUES
-- Instituciones
( 1, 'VIEW_ALL_INSTITUTIONS'),
( 2, 'CREATE_INSTITUTION'),
( 3, 'UPDATE_INSTITUTION'),
( 4, 'DELETE_INSTITUTION'),
-- Redes sociales
( 5, 'CREATE_SOCIAL_NETWORK'),
-- Comentarios — acción de usuario
( 6, 'ADD_COMMENT'),
( 7, 'DELETE_COMMENT'),
-- Comentarios — moderación
( 8, 'VIEW_MODERATED_COMMENTS'),
( 9, 'VIEW_REJECTED_COMMENTS'),
(10, 'APPROVE_COMMENT'),
(11, 'REJECT_COMMENT'),
(12, 'DELETE_MODERATED_COMMENT'),
(13, 'VIEW_DELETED_COMMENTS'),
-- Seguidores
(14, 'FOLLOW_INSTITUTION'),
(15, 'UNFOLLOW_INSTITUTION'),
(16, 'CHECK_FOLLOWING_STATUS'),
-- Reacciones a posts
(17, 'CREATE_POST_REACTION'),
(18, 'DELETE_POST_REACTION'),
-- Reacciones a comentarios
(19, 'CREATE_COMMENT_REACTION'),
(20, 'DELETE_COMMENT_REACTION'),
-- Reacciones a replies
(21, 'CREATE_REPLY_REACTION'),
(22, 'DELETE_REPLY_REACTION'),
-- Emoji
(23, 'CREATE_EMOJI_TYPE'),
-- Moderación (blacklist)
(24, 'MANAGE_BLACKLIST'),
-- Replies
(25, 'CREATE_REPLY'),
(26, 'DELETE_REPLY'),
-- Posts
(27, 'CREATE_POST'),
(28, 'DELETE_POST'),
(29, 'UPDATE_POST'),
(30, 'GROUP_POST'),
(31, 'UNGROUP_POST'),
-- Grupos
(32, 'VIEW_GROUP'),
(33, 'VIEW_ALL_GROUPS'),
(34, 'CREATE_GROUP'),
-- Configuración
(35, 'CREATE_COMMENT_CONFIG'),
-- Media: imágenes
(36, 'UPLOAD_POST_IMAGE'),
(37, 'DELETE_POST_IMAGE'),
(38, 'UPLOAD_INST_PROFILE_IMAGE'),
(39, 'UPLOAD_INST_COVER_IMAGE'),
(40, 'UPLOAD_USER_PROFILE_IMAGE'),
-- Media: videos
(41, 'UPLOAD_VIDEO'),
(42, 'DELETE_VIDEO'),
-- Media: documentos
(43, 'UPLOAD_DOCUMENT'),
(44, 'DELETE_DOCUMENT')
ON CONFLICT DO NOTHING;

-- ─────────────────────────────────────────────────────────────
-- 3. ASIGNACIÓN DE PERMISOS POR ROL
-- ─────────────────────────────────────────────────────────────

-- ROOT → todos los permisos (acceso global, sin restricción de tenant)
INSERT INTO public.rol_permissions (role_id, permission_id)
SELECT 1, id_permission FROM public.permissions
ON CONFLICT DO NOTHING;

-- ADMIN → todos los permisos dentro de su institución
INSERT INTO public.rol_permissions (role_id, permission_id)
SELECT 2, id_permission FROM public.permissions
ON CONFLICT DO NOTHING;

-- STUDENT → solo acciones de usuario final (leer, interactuar, seguir)
--   NO puede: crear posts, moderar, gestionar institución, subir medios de posts
INSERT INTO public.rol_permissions (role_id, permission_id) VALUES
    (3,  6),  -- ADD_COMMENT
    (3,  7),  -- DELETE_COMMENT (solo el propio, controlado en servicio)
    (3, 14),  -- FOLLOW_INSTITUTION
    (3, 15),  -- UNFOLLOW_INSTITUTION
    (3, 16),  -- CHECK_FOLLOWING_STATUS
    (3, 17),  -- CREATE_POST_REACTION
    (3, 18),  -- DELETE_POST_REACTION
    (3, 19),  -- CREATE_COMMENT_REACTION
    (3, 20),  -- DELETE_COMMENT_REACTION
    (3, 21),  -- CREATE_REPLY_REACTION
    (3, 22),  -- DELETE_REPLY_REACTION
    (3, 25),  -- CREATE_REPLY
    (3, 26),  -- DELETE_REPLY (solo el propio, controlado en servicio)
    (3, 40)   -- UPLOAD_USER_PROFILE_IMAGE
ON CONFLICT DO NOTHING;

-- ─────────────────────────────────────────────────────────────
-- 4. CONFIGURACIONES DE COMENTARIOS (catálogo global)
-- ─────────────────────────────────────────────────────────────
INSERT INTO public.comment_config (id, uuid, name, configuration_type) VALUES
    (20, '875d7d7f-7a1c-4b77-ab63-77a9f76759d0', 'Todos pueden comentar',     'FREE_COMMENTS'),
    (21, '587d7d7f-5g3n-4b77-cf98-77a9h46759d0', 'Nadie puede comentar',      'RESTRICTED_COMMENTS'),
    (22, '492d7d7f-9f4s-8g45-hy34-77a9h46759d0', 'Comentarios con moderador', 'MODERATED_COMMENTS')
ON CONFLICT DO NOTHING;

-- ─────────────────────────────────────────────────────────────
-- 5. INSTITUCIÓN DPA — tenant real de producción
-- ─────────────────────────────────────────────────────────────
INSERT INTO public.institution (id, uuid, name, description, location, category, email, phone, url, logo_url, background_url, slug, status, plan)
VALUES (
    10,
    '93j203b4-f63b-4c4a-be05-eae84cef0c0c',
    'Dirección de Planificación Académica - DPA',
    'Dirección de Planificación Académica de la Universidad Mayor de San Simón',
    'Av. Oquendo Prolongación Jordán, Edif: Multiacadémico Piso 3°, Cochabamba, Bolivia',
    'Sitio web de educación',
    'dpa@umss.edu.bo',
    '+591 4 4232970',
    'dpa.umss.edu.bo',
    '/api/v1/images/inst-profile/f1309a27-93eb-4429-a096-d786f8d16f5c',
    '/api/v1/images/inst-cover/14bacba4-b962-40b5-9dd1-d5bf8e1e86f8',
    'dpa',
    'ACTIVE',
    'FREE'
) ON CONFLICT DO NOTHING;

-- ─────────────────────────────────────────────────────────────
-- 6. USUARIO ROOT DEL SISTEMA
--    institution_id = NULL → acceso global a todos los tenants
--    Contraseña: root1234 (cambiar en producción)
-- ─────────────────────────────────────────────────────────────
INSERT INTO public.users (id_user, uuid, name, last_name, email, password, institution_id, is_root)
VALUES (
    1,
    '00000000-0000-0000-0000-000000000001',
    'Root',
    'System',
    'root@system.local',
    '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS',
    NULL,
    TRUE
) ON CONFLICT DO NOTHING;

INSERT INTO public.user_roles (user_id, role_id)
VALUES (1, 1)  -- ROOT user → rol ROOT (id=1)
ON CONFLICT DO NOTHING;

-- ─────────────────────────────────────────────────────────────
-- 7. RESET DE SECUENCIAS
-- ─────────────────────────────────────────────────────────────

-- Tablas con GENERATED BY DEFAULT AS IDENTITY
SELECT setval(pg_get_serial_sequence('public.role',        'id_role'),       (SELECT MAX(id_role)       FROM public.role));
SELECT setval(pg_get_serial_sequence('public.permissions', 'id_permission'), (SELECT MAX(id_permission) FROM public.permissions));
SELECT setval(pg_get_serial_sequence('public.comment_config', 'id'),         (SELECT MAX(id)            FROM public.comment_config));
SELECT setval(pg_get_serial_sequence('public.users',       'id_user'),       (SELECT MAX(id_user)       FROM public.users));

-- institution usa secuencia Hibernate (INCREMENT BY 50): id=10 → avanzamos a 200
SELECT setval('public.institution_seq', 200);
