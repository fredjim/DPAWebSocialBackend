-- R__dev_seed_dpa.sql
-- ════════════════════════════════════════════════════════════════
-- SEED DE DESARROLLO — 3 Instituciones para testing multitenant
-- ════════════════════════════════════════════════════════════════
-- Flyway re-ejecuta este archivo cuando su checksum cambia.
-- Solo activo en perfil DEV (application-dev.properties).
-- NUNCA en producción.
--
-- Contraseña de todos los usuarios de prueba: "12345678"
--
-- Rangos de IDs por institución (facilita depuración):
--   100-199 → DPA  (Dirección de Planificación Académica)
--   200-299 → FCyT (Facultad de Ciencias y Tecnología)
--   300-399 → FHCE (Facultad de Humanidades y Ciencias de la Educación)
-- ════════════════════════════════════════════════════════════════


-- ─────────────────────────────────────────────────────────────
-- 1. INSTITUCIONES
--    DPA ya está en V5 (id=10). Aquí solo las 2 nuevas.
-- ─────────────────────────────────────────────────────────────
INSERT INTO public.institution (id, uuid, name, description, location, category, email, phone, url, logo_url, background_url, slug, status, plan) VALUES
(11,
 'cc000011-4000-4000-8000-000000000011',
 'DUBE - Dirección Universitaria de Bienestar Estudiantil de la Universidad Mayor de San Simón',
 'Noticias, eventos y servicios de la Facultad de Ciencias y Tecnología de la UMSS',
 'Campus Central UMSS (Calle Sucre), Cochabamba, Bolivia',
 'Facultad universitaria',
 'dube@dube.umss.edu.bo', '+591 4 4234565',
 'dube.umss.edu.bo',
 '/api/v1/images/inst-profile/d22882a6-5f9c-4cd3-95dc-534c5d5f05a8',
 '/api/v1/images/inst-cover/75440bfb-6cd3-4a26-8038-79fc8859f753',
 'dube', 'ACTIVE', 'FREE'),
(12,
 'dd000012-4000-4000-8000-000000000012',
 'DRIC - Dirección de Relaciones Internacionales y Convenios - UMSS',
 'Noticias, eventos y servicios de la Facultad de Ciencias y Tecnología de la UMSS',
 'Campus Central UMSS, Cochabamba, Bolivia',
 'Facultad universitaria',
 'dric@dric.umss.edu.bo', '+591 4 4234568',
 'dric.umss.edu.bo',
 '/api/v1/images/inst-profile/7baffe0f-4f73-4a67-981a-0bcf906628a2',
 '/api/v1/images/inst-cover/bb6428a1-3daf-4dc5-a72f-d33bc99c3a9b',
 'dric', 'ACTIVE', 'FREE')
ON CONFLICT DO NOTHING;


-- ─────────────────────────────────────────────────────────────
-- 2. EMOJI TYPES (catálogo global, sin tenant)
-- ─────────────────────────────────────────────────────────────
INSERT INTO public.emoji_type (id, uuid, emoji_name, emoji_code) VALUES
(100, '3f696a78-c73f-475c-80a6-f5a858648af1', 'thumbs-up',               U&'\+01F44D'),
(101, '7v236a78-c73f-475c-80a6-f5a858648af1', 'red-heart',               U&'\+002764\+00FE0F'),
(102, 'n1596a78-c73f-475c-80a6-f5a858648af1', 'crying-face',             U&'\+01F622'),
(103, '4c806a78-c73f-475c-80a6-f5a858648af1', 'angry-face',              U&'\+01F620'),
(104, 'l6m3bd82-c73f-475c-80a6-f5a858648af1', 'grinning-squinting-face', U&'\+01F606'),
(105, 'c5n1m4f0-c73f-475c-80a6-f5a858648af1', 'astonished-face',         U&'\+01F632')
ON CONFLICT DO NOTHING;


-- ─────────────────────────────────────────────────────────────
-- 3. USUARIOS DE PRUEBA + ROLES
-- ─────────────────────────────────────────────────────────────

-- ── DPA (id institución: 10) ─────────────────────────────────
INSERT INTO public.users (id_user, uuid, name, last_name, email, phone, password, institution_id, is_root) VALUES
(100, 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Miguel',   'Guzman',          'micael.guzman@dpa.umss.edu.bo',   '71795251', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', FALSE),
(101, 'eab72365-d8c1-45df-9b48-274f64c65b86', 'Jhonny',   'Cahuaya',         'jhonny.cahuaya@umss.edu.bo',       '70755958', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', FALSE),
(110, '9bae1fe0-2c56-4091-883d-15458e051500', 'Emely',    'Fernandez Soliz', 'admin-convenios@dpa.umss.edu.bo',  '69577142', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', FALSE),
(111, '844b324d-2f5e-41c9-b726-0149eeb01157', 'Javier',   'Lopez Canedo',    'admin-proyectos@dpa.umss.edu.bo',  '71852821', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', FALSE),
(112, '2e121a51-24b5-4aad-92c2-150997ec4266', 'Leonardo', 'Beltran Ramirez', 'admin-becas@dpa.umss.edu.bo',      '75891148', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', FALSE),
(113, 'c332c4ff-49e8-4e0e-a7f4-d59907d8cda3', 'Valeria',  'Gonzales Vargas', 'admin-cudie@dpa.umss.edu.bo',      '78823541', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', FALSE),
(114, '9f32985a-f108-4b19-9bda-cab7c501ae68', 'Jose',     'Montano Laura',   '202001823@est.umss.edu',            '72566218', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', FALSE),
(115, 'ab6e1a7f-4494-4251-8177-dc5c4fe18740', 'Marcos',   'Illanes Martinez','201800513@est.umss.edu',            '74269527', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', FALSE),
(116, 'f8d1c969-af76-4161-b0d7-9c2dfc47e75c', 'Adriana',  'Boza Ruiz',       '201904940@est.umss.edu',            '70551293', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', FALSE),
(117, 'd33ded75-c9f3-4fef-9762-0ba4a905efa8', 'Antonio',  'Monje Aranibar',  '201604527@est.umss.edu',            '62933721', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', FALSE),
(118, 'e54bd4dc-d8f6-42e9-8e94-5d56bf42416f', 'Jeyson',   'Valdivia Bernal', 'jeyson.valdivia@gmail.com',         '76834814', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', FALSE),
(119, 'ae542968-1335-425e-a206-283c38a20190', 'Omar',     'Argenes Quispe',  'argenes77@gmail.com',               '65692585', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', FALSE),
(120, '7f9264d7-ca8d-41ca-be1f-d24c9dd244a2', 'Bianca',   'Antelo Dominguez','bianca.dominguez@gmail.com',        '73597236', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', FALSE)
ON CONFLICT DO NOTHING;

-- ── FCyT (id institución: 11) ────────────────────────────────
INSERT INTO public.users (id_user, uuid, name, last_name, email, phone, password, institution_id, is_root) VALUES
(200, 'cc000200-4000-4000-8000-000000000200', 'Carlos',  'Mendez Vargas',   'admin@fcyt.umss.edu.bo',     '71000200', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', 'cc000011-4000-4000-8000-000000000011', FALSE),
(201, 'cc000201-4000-4000-8000-000000000201', 'Ana',     'Rodriguez Soto',  'admin2@fcyt.umss.edu.bo',    '71000201', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', 'cc000011-4000-4000-8000-000000000011', FALSE),
(210, 'cc000210-4000-4000-8000-000000000210', 'Luis',    'Torres Mamani',   '210001@fcyt.umss.edu.bo',    '71000210', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', 'cc000011-4000-4000-8000-000000000011', FALSE),
(211, 'cc000211-4000-4000-8000-000000000211', 'Maria',   'Flores Crespo',   '210002@fcyt.umss.edu.bo',    '71000211', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', 'cc000011-4000-4000-8000-000000000011', FALSE),
(212, 'cc000212-4000-4000-8000-000000000212', 'Pedro',   'Quispe Choque',   '210003@fcyt.umss.edu.bo',    '71000212', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', 'cc000011-4000-4000-8000-000000000011', FALSE),
(213, 'cc000213-4000-4000-8000-000000000213', 'Sofia',   'Mamani Copa',     'moderador@fcyt.umss.edu.bo', '71000213', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', 'cc000011-4000-4000-8000-000000000011', FALSE)
ON CONFLICT DO NOTHING;

-- ── FHCE (id institución: 12) ────────────────────────────────
INSERT INTO public.users (id_user, uuid, name, last_name, email, phone, password, institution_id, is_root) VALUES
(300, 'dd000300-4000-4000-8000-000000000300', 'Roberto', 'Vargas Perez',    'admin@fhce.umss.edu.bo',     '72000300', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', 'dd000012-4000-4000-8000-000000000012', FALSE),
(301, 'dd000301-4000-4000-8000-000000000301', 'Carmen',  'Salazar Lima',    'admin2@fhce.umss.edu.bo',    '72000301', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', 'dd000012-4000-4000-8000-000000000012', FALSE),
(310, 'dd000310-4000-4000-8000-000000000310', 'Hugo',    'Condori Quispe',  '310001@fhce.umss.edu.bo',    '72000310', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', 'dd000012-4000-4000-8000-000000000012', FALSE),
(311, 'dd000311-4000-4000-8000-000000000311', 'Elena',   'Pardo Villanueva','310002@fhce.umss.edu.bo',    '72000311', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', 'dd000012-4000-4000-8000-000000000012', FALSE),
(312, 'dd000312-4000-4000-8000-000000000312', 'Diego',   'Villarroel Cruz', '310003@fhce.umss.edu.bo',    '72000312', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', 'dd000012-4000-4000-8000-000000000012', FALSE),
(313, 'dd000313-4000-4000-8000-000000000313', 'Isabel',  'Arce Gutierrez',  'moderador@fhce.umss.edu.bo', '72000313', '$2a$10$udHe3pGniYc4Kg/SejLucuLJBdvlgGRUxNbpUh/hCEdWnZKySDYvS', 'dd000012-4000-4000-8000-000000000012', FALSE)
ON CONFLICT DO NOTHING;

-- Roles: 1=ROOT  2=ADMIN  3=STUDENT
INSERT INTO public.user_roles (user_id, role_id) VALUES
-- DPA
(100, 2), (101, 2),                              -- ADMIN
(110, 2), (111, 2), (112, 2), (113, 2),          -- ADMIN (antes tenían roles especializados)
(114, 3), (115, 3), (116, 3), (117, 3),          -- STUDENT
(118, 3), (119, 3), (120, 3),                    -- STUDENT
-- FCyT
(200, 2), (201, 2),                              -- ADMIN
(210, 3), (211, 3), (212, 3),                    -- STUDENT
(213, 2),                                        -- ADMIN (antes MODERATOR)
-- FHCE
(300, 2), (301, 2),                              -- ADMIN
(310, 3), (311, 3), (312, 3),                    -- STUDENT
(313, 2)                                         -- ADMIN (antes MODERATOR)
ON CONFLICT DO NOTHING;


-- ─────────────────────────────────────────────────────────────
-- 4. REDES SOCIALES
-- ─────────────────────────────────────────────────────────────
-- Usa social_network_seq (Hibernate, increment=50). IDs en rangos 100/200/300.
INSERT INTO public.social_network (id, uuid, institution_id, name, link) VALUES
(100, 'dpa-sn-100-0000-0000-000000000100', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'Facebook', 'https://www.facebook.com/UMSS.DPA'),
(101, 'dpa-sn-101-0000-0000-000000000101', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'Twitter',  'https://twitter.com/UMSS_DPA'),
(200, 'fcy-sn-200-0000-0000-000000000200', 'cc000011-4000-4000-8000-000000000011', 'Facebook', 'https://www.facebook.com/fcyt.umss'),
(201, 'fcy-sn-201-0000-0000-000000000201', 'cc000011-4000-4000-8000-000000000011', 'Instagram','https://instagram.com/fcyt.umss'),
(300, 'fhc-sn-300-0000-0000-000000000300', 'dd000012-4000-4000-8000-000000000012', 'Facebook', 'https://www.facebook.com/fhce.umss')
ON CONFLICT DO NOTHING;


-- ─────────────────────────────────────────────────────────────
-- 5. NAV ITEMS → SECTIONS → ARTICLES
-- ─────────────────────────────────────────────────────────────
-- Usa nav_item_seq, section_seq, article_seq (Hibernate, increment=50).
INSERT INTO public.nav_item (id, uuid, institution_id, user_id, label, path, item_order, visible, deleted) VALUES
(100, 'dpa-nav-100-0000-0000-000000000100', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Inicio',        'inicio',              1, TRUE, FALSE),
(101, 'dpa-nav-101-0000-0000-000000000101', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Noticias',      'noticias',      2, TRUE, FALSE),
(200, 'fcy-nav-200-0000-0000-000000000200', 'cc000011-4000-4000-8000-000000000011', 'cc000200-4000-4000-8000-000000000200', 'Inicio',        'inicio',              1, TRUE, FALSE),
(201, 'fcy-nav-201-0000-0000-000000000201', 'cc000011-4000-4000-8000-000000000011', 'cc000200-4000-4000-8000-000000000200', 'Noticias',      'noticias',      2, TRUE, FALSE),
(300, 'fhc-nav-300-0000-0000-000000000300', 'dd000012-4000-4000-8000-000000000012', 'dd000300-4000-4000-8000-000000000300', 'Inicio',        'inicio',              1, TRUE, FALSE),
(301, 'fhc-nav-301-0000-0000-000000000301', 'dd000012-4000-4000-8000-000000000012', 'dd000300-4000-4000-8000-000000000300', 'Post', 'Page', 2, TRUE, FALSE)
ON CONFLICT DO NOTHING;

INSERT INTO public.section (id, uuid, institution_id, user_id, nav_item_id, name, path, date, deleted, created_date) VALUES
(100, 'dpa-sec-100-0000-0000-000000000100', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'dpa-nav-101-0000-0000-000000000101', 'Noticias DPA',       'noticias-dpa',       '2025-01-01 00:00:00', FALSE, '2025-01-01 00:00:00'),
(101, 'dpa-sec-101-0000-0000-000000000101', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'dpa-nav-101-0000-0000-000000000101', 'Eventos DPA',        'eventos-dpa',        '2025-01-01 00:00:00', FALSE, '2025-02-01 00:00:00'), 
(200, 'fcy-sec-200-0000-0000-000000000200', 'cc000011-4000-4000-8000-000000000011', 'cc000200-4000-4000-8000-000000000200', 'fcy-nav-201-0000-0000-000000000201', 'Noticias FCyT',      'noticias-fcyt',      '2025-01-01 00:00:00', FALSE, '2025-03-01 00:00:00'),
(300, 'fhc-sec-300-0000-0000-000000000300', 'dd000012-4000-4000-8000-000000000012', 'dd000300-4000-4000-8000-000000000300', 'fhc-nav-301-0000-0000-000000000301', 'Publicaciones FHCE', 'publicaciones-fhce', '2025-01-01 00:00:00', FALSE, '2025-04-01 00:00:00')
ON CONFLICT DO NOTHING;

INSERT INTO public.article (id, uuid, section_id, user_id, institution_id, title, text, date, deleted) VALUES
(100, 'dpa-art-100-0000-0000-000000000100',
 'dpa-sec-100-0000-0000-000000000100', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '93j203b4-f63b-4c4a-be05-eae84cef0c0c',
 'Nuevo Modelo Educativo UMSS 2025',
 'La Dirección de Planificación Académica presenta el nuevo Modelo Educativo de la UMSS para la gestión 2025, orientado a la innovación curricular y la formación integral del estudiante universitario.',
 '2025-03-01 09:00:00', FALSE),
(101, 'dpa-art-101-0000-0000-000000000101',
 'dpa-sec-101-0000-0000-000000000101', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '93j203b4-f63b-4c4a-be05-eae84cef0c0c',
 'Jornadas de Innovación Curricular 2025',
 'Convocatoria a autoridades, docentes y estudiantes para participar en las jornadas académicas de innovación curricular, un espacio de reflexión y construcción colectiva del nuevo modelo educativo.',
 '2025-07-14 08:00:00', FALSE),
(200, 'fcy-art-200-0000-0000-000000000200',
 'fcy-sec-200-0000-0000-000000000200', 'cc000200-4000-4000-8000-000000000200', 'cc000011-4000-4000-8000-000000000011',
 'Apertura de Laboratorios de Inteligencia Artificial',
 'La FCyT anuncia la apertura de tres nuevos laboratorios equipados con hardware de última generación para investigación en Inteligencia Artificial y Machine Learning.',
 '2025-04-10 10:00:00', FALSE),
(300, 'fhc-art-300-0000-0000-000000000300',
 'fhc-sec-300-0000-0000-000000000300', 'dd000300-4000-4000-8000-000000000300', 'dd000012-4000-4000-8000-000000000012',
 'Convocatoria a Becas de Investigación Humanística 2025',
 'La Facultad de Humanidades y Ciencias de la Educación abre convocatoria para becas de investigación en áreas de lingüística, filosofía y ciencias de la educación.',
 '2025-05-05 09:00:00', FALSE)
ON CONFLICT DO NOTHING;



-- ********************************************************************************************************************************
-- Phase 1: Insert directly into uploaded_file
-- Ya no insertamos en image_file porque luego se elimina

INSERT INTO public.uploaded_file (
    id,
    uuid,
    name,
    url_resource,
    category,
    mime_type,
    status
) VALUES
    (
        113,
        'f1309a27-93eb-4429-a096-d786f8d16f5c',
        'img14',
        '/api/v1/images/inst-profile/f1309a27-93eb-4429-a096-d786f8d16f5c',
        'IMAGE',
        'image/png',
        'SAVED_SUCCESSFULLY'
    ),
    (
        114,
        '14bacba4-b962-40b5-9dd1-d5bf8e1e86f8',
        'img15',
        '/api/v1/images/inst-cover/14bacba4-b962-40b5-9dd1-d5bf8e1e86f8',
        'IMAGE',
        'image/png',
        'SAVED_SUCCESSFULLY'
    ),
    (
        115,
        'd22882a6-5f9c-4cd3-95dc-534c5d5f05a8',
        'img14',
        '/api/v1/images/inst-profile/d22882a6-5f9c-4cd3-95dc-534c5d5f05a8',
        'IMAGE',
        'image/png',
        'SAVED_SUCCESSFULLY'
    ),
    (
        116,
        '75440bfb-6cd3-4a26-8038-79fc8859f753',
        'img15',
        '/api/v1/images/inst-cover/75440bfb-6cd3-4a26-8038-79fc8859f753',
        'IMAGE',
        'image/png',
        'SAVED_SUCCESSFULLY'
    ),
    (
        117,
        '7baffe0f-4f73-4a67-981a-0bcf906628a2',
        'img14',
        '/api/v1/images/inst-profile/7baffe0f-4f73-4a67-981a-0bcf906628a2',
        'IMAGE',
        'image/png',
        'SAVED_SUCCESSFULLY'
    ),
    (
        118,
        'bb6428a1-3daf-4dc5-a72f-d33bc99c3a9b',
        'img15',
        '/api/v1/images/inst-cover/bb6428a1-3daf-4dc5-a72f-d33bc99c3a9b',
        'IMAGE',
        'image/png',
        'SAVED_SUCCESSFULLY'
    )
ON CONFLICT (uuid) DO NOTHING;


-- ********************************************************************************************************************************
-- Phase 2: Add FK uploaded_file_id to media and article_media tables

-- Populate uploaded_file_id from matching url_resource
UPDATE public.media m
SET uploaded_file_id = uf.id
FROM public.uploaded_file uf
WHERE m.file_path = uf.url_resource;

-- Drop old file_path column
ALTER TABLE public.media
DROP COLUMN IF EXISTS file_path;

-- Drop deprecated tables
DROP TABLE IF EXISTS public.image_file;
DROP TABLE IF EXISTS public.video_file;
DROP TABLE IF EXISTS public.document_file;
-- ********************************************************************************************************************************

-- ═══════════════════════════════════════════════════════════════
-- 14. RESET DE SECUENCIAS
--     Los INSERTs con ID explícito no avanzan la secuencia.
--     Sin este bloque, la app generaría IDs que colisionan con
--     los datos de seed en la primera inserción real.
-- ═══════════════════════════════════════════════════════════════

-- ── Tablas con GENERATED BY DEFAULT AS IDENTITY ───────────────
SELECT setval(pg_get_serial_sequence('public.users',          'id_user'),   (SELECT MAX(id_user)   FROM public.users));
SELECT setval(pg_get_serial_sequence('public.emoji_type',     'id'),        (SELECT MAX(id)        FROM public.emoji_type));
SELECT setval(pg_get_serial_sequence('public.groups',         'id'),        (SELECT MAX(id)        FROM public.groups));
SELECT setval(pg_get_serial_sequence('public.replies',        'reply_id'),  (SELECT MAX(reply_id)  FROM public.replies));
SELECT setval(pg_get_serial_sequence('public.reply_reactions','id'),        (SELECT MAX(id)        FROM public.reply_reactions));
SELECT setval(pg_get_serial_sequence('public.followers',      'id'),        (SELECT MAX(id)        FROM public.followers));

-- ── Secuencias Hibernate (INCREMENT BY 50) ────────────────────
-- setval(seq, X) → próximo nextval() retorna X+50
-- Con IDs hasta 304, setval=400 garantiza que Hibernate genere desde 450+
SELECT setval('public.content_seq',          400);
SELECT setval('public.text_seq',             400);
SELECT setval('public.media_seq',            400);
SELECT setval('public.post_seq',             400);
SELECT setval('public.comment_seq',          400);
SELECT setval('public.comment_reaction_seq', 400);
SELECT setval('public.post_reaction_seq',    400);
SELECT setval('public.social_network_seq',   400);
SELECT setval('public.nav_item_seq',         400);
SELECT setval('public.section_seq',          400);
SELECT setval('public.article_seq',          400);
-- institution_seq ya se resetea en V5 a 200 (DPA=10, DUBE=11, DRIC=12 → todo bajo 200)

