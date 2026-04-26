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
 'fcyt@fcyt.umss.edu.bo', '+591 4 4234565',
 'fcyt.umss.edu.bo',
 '/api/v1/images/inst-profile/d22882a6-5f9c-4cd3-95dc-534c5d5f05a8',
 '/api/v1/images/inst-cover/75440bfb-6cd3-4a26-8038-79fc8859f753',
 'fcyt', 'ACTIVE', 'FREE'),
(12,
 'dd000012-4000-4000-8000-000000000012',
 'DRIC - Dirección de Relaciones Internacionales y Convenios - UMSS',
 'Noticias, eventos y servicios de la Facultad de Ciencias y Tecnología de la UMSS',
 'Campus Central UMSS, Cochabamba, Bolivia',
 'Facultad universitaria',
 'fhce@fhce.umss.edu.bo', '+591 4 4234568',
 'fhce.umss.edu.bo',
 '/api/v1/images/inst-profile/7baffe0f-4f73-4a67-981a-0bcf906628a2',
 '/api/v1/images/inst-cover/bb6428a1-3daf-4dc5-a72f-d33bc99c3a9b',
 'fhce', 'ACTIVE', 'FREE')
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


-- ─────────────────────────────────────────────────────────────
-- 6. CONTENT + TEXT + MEDIA + POSTS
--    content y post usan content_seq / post_seq (Hibernate, increment=50).
--    Los UUIDs de content_id / post_id se cruzan entre tablas.
-- ─────────────────────────────────────────────────────────────

-- ── CONTENT ──────────────────────────────────────────────────
INSERT INTO public.content (id, uuid) VALUES
-- DPA
(100, '1e5294f8-9a6c-4d9e-aea7-da7e80bd5c6f'),
(101, '6i2494f8-9a6c-4d9e-aea7-da7e80bd5c6f'),
(102, '8f3194f8-9a6c-4d9e-aea7-da7e80bd5c6f'),
(103, '5l6n94f8-9a6c-4d9e-aea7-da7e80bd5c6f'),
(104, '2v4z94f8-9a6c-4d9e-aea7-da7e80bd5c6f'),
(105, '7o1b94f8-9a6c-4d9e-aea7-da7e80bd5c6f'),
(106, '3m8c52v4-9a6c-4d9e-aea7-da7e80bd5c6f'),
(107, '1om5x7n2-9a6c-4d9e-aea7-da7e80bd5c6f'),
-- FCyT
(200, 'bd8b53b2-5683-1d88-1b12-d5b78c02f8ad'),
(201, '8fdd0cab-b64c-73f3-52da-dc2744f03bf1'),
(202, 'aa91b489-88e0-2442-881d-cc1c5638597b'),
(203, '937141c5-1698-8926-8fda-17be7718ab75'),
(204, '7c381afe-5442-d755-fa47-cb4ad317d824'),
-- FHCE
(300, 'be6bbec1-c53a-782a-e690-06be20bf7dbd'),
(301, '321edf84-1395-f851-df13-1f04c1611e8b'),
(302, '602d9c05-3f6d-2ba4-8e34-3e7a3a28a643'),
(303, '4858067c-cb65-bacb-75a2-0458a63bfdef'),
(304, '0181be63-4cba-9c80-2604-03c06121475b')
ON CONFLICT DO NOTHING;

-- ── TEXT ─────────────────────────────────────────────────────
INSERT INTO public.text (id, uuid, content_id, text) VALUES
-- DPA (contenido real de data-dpa_dev.sql)
(100, '3m22c948-o3j9-4e39-92a6-fa761f2410a7', '1e5294f8-9a6c-4d9e-aea7-da7e80bd5c6f',
 'La DPA y el DDC invitan a participar del Ciclo de Conferencias: Rumbo hacia el nuevo Modelo Educativo de la UMSS. CONVERSATORIO: FLEXIBILIDAD CURRICULAR. Fecha: Jueves 20 de febrero de 2025. Hora: 9:30 a.m. Lugar: Google Meet.'),
(101, '19b6c948-f5f4-4e39-92a6-fa761f2410a7', '6i2494f8-9a6c-4d9e-aea7-da7e80bd5c6f',
 'Conversatorio: "TIC Aplicadas a la Educación Superior". Fecha: 10 de marzo del 2025. Hora: 14:00 pm. Lugar: Aula Magna de la FHCE. Espacio de diálogo sobre el impacto de las TICs en el ámbito educativo.'),
(102, '52o7c948-f5f4-4e39-92a6-fa761f2410a7', '8f3194f8-9a6c-4d9e-aea7-da7e80bd5c6f',
 'Seminario: "La IA dentro los espacios formativos". Fecha: 13 de marzo del 2025. Hora: 19:30. Lugar: Modalidad Virtual (Zoom). Un espacio de diálogo sobre el impacto de la IA en la educación.'),
(103, '25x4c948-f5f4-4e39-92a6-fa761f2410a7', '5l6n94f8-9a6c-4d9e-aea7-da7e80bd5c6f',
 'Taller de innovación curricular - Facultad de Ciencias Agrícolas. Lugar: Aula 822 (FCAPyF). Hora: 13:30 a 15:30. Marco del Año de la Innovación Curricular.'),
(104, '99c1c948-f5f4-4e39-92a6-fa761f2410a7', '2v4z94f8-9a6c-4d9e-aea7-da7e80bd5c6f',
 'Taller de innovación curricular - Facultad de Ciencias y Tecnología. Lugar: Palacio de Ciencia y Cultura. Hora: 09:30 a 12:30. Objetivo: fortalecer procesos de innovación educativa.'),
(105, '4mx8c948-f5f4-4e39-92a6-fa761f2410a7', '7o1b94f8-9a6c-4d9e-aea7-da7e80bd5c6f',
 'Taller de innovación curricular - Facultad de Ciencias Veterinarias. Lugar: Aula Magna (FCV). Hora: 09:30 a 12:30. Espacio de reflexión sobre transformación curricular.'),
(106, '6vk5x297-f5f4-4e39-92a6-fa761f2410a7', '3m8c52v4-9a6c-4d9e-aea7-da7e80bd5c6f',
 'Taller de innovación curricular - Facultad Politécnica del Valle Alto. Lugar: Auditorio FPVA. Hora: 09:30 a 12:30. Una apuesta estratégica por la transformación curricular sostenible.'),
(107, '2cm9z8n5-f5f4-4e39-92a6-fa761f2410a7', '1om5x7n2-9a6c-4d9e-aea7-da7e80bd5c6f',
 'JORNADAS ACADÉMICAS PARA LA INNOVACIÓN CURRICULAR 2025. Fecha: 14 al 30 de julio. Horario: 8:30 a 12:00. Modalidad: Presencial. Dirigido a autoridades, docentes y representantes estudiantiles.'),
-- FCyT
(200, 'e0015352-879a-4ede-70f8-523a06a63a5d', 'bd8b53b2-5683-1d88-1b12-d5b78c02f8ad',
 'La FCyT convoca a todos los estudiantes a la Feria de Proyectos 2025. Exposición de proyectos de las carreras de Informática, Sistemas, Electrónica e Ingeniería Civil. Fecha: 20 de mayo. Lugar: Palacio de Ciencia y Cultura.'),
(201, '1c5e6a0d-15cb-1f5c-a2d8-dbe087d841ab', '8fdd0cab-b64c-73f3-52da-dc2744f03bf1',
 'Convocatoria a Auxiliares de Docencia - Semestre II/2025. Carreras: Informática y Sistemas. Requisitos: Promedio >= 65, sin materias reprobadas. Inscripciones: Secretaría de la FCyT hasta el 30 de agosto.'),
(202, 'ad16189f-cae3-93a0-f662-711dbc9cb972', 'aa91b489-88e0-2442-881d-cc1c5638597b',
 'Seminario Internacional: "Inteligencia Artificial y el Futuro de la Ingeniería". Expositores de Argentina, Chile y Bolivia. Fecha: 15 de junio. Modalidad híbrida. Certificado con valor curricular.'),
(203, '8c45b51b-2d59-b227-4543-89ea502f6158', '937141c5-1698-8926-8fda-17be7718ab75',
 'Resultados del Torneo Universitario de Programación SanSi Cup 2025. Primer lugar: equipo UMSS-Alpha. Felicitamos a todos los participantes y organizadores del evento.'),
(204, 'dbc5ed1c-f595-122b-785c-9a7e0804864b', '7c381afe-5442-d755-fa47-cb4ad317d824',
 'Apertura de inscripciones para el Curso de Formación Continua: Redes y Ciberseguridad. Inicio: 1 de julio. Modalidad: Presencial. Duración: 4 semanas. Certificado con valor curricular. Inscripciones en el Lab de Computo.'),
-- FHCE
(300, '974489df-0342-c93c-81c1-da190675d2ca', 'be6bbec1-c53a-782a-e690-06be20bf7dbd',
 'La FHCE convoca a los estudiantes al Congreso de Educación e Innovación Pedagógica 2025. Paneles temáticos en: Didáctica, Tecnología Educativa y Evaluación por Competencias. Fecha: 10 de junio.'),
(301, '3d13037f-e1ed-e92f-fd22-09017826b521', '321edf84-1395-f851-df13-1f04c1611e8b',
 'Resultados del proceso de titulación - Gestión I/2025. Se felicita a los nuevos licenciados de las carreras de Psicología, Comunicación Social y Trabajo Social. Acto de grado: 25 de julio, Salón de Honor.'),
(302, '00be2efd-9ed7-dbb7-954b-e88fc77d16b6', '602d9c05-3f6d-2ba4-8e34-3e7a3a28a643',
 'Taller: "Metodologías Activas en el Aula Universitaria". Dirigido a docentes de todas las facultades. Fecha: 18 de mayo. Hora: 14:00 a 18:00. Lugar: Aula Magna FHCE. Inscripciones hasta el 15 de mayo.'),
(303, 'e8a14646-ef4c-26f5-d999-7f0bf2057757', '4858067c-cb65-bacb-75a2-0458a63bfdef',
 'Convocatoria: Beca de Investigación en Lingüística Aplicada. Requisitos: estudiante de 4to o 5to año, promedio >= 70. Duración: 6 meses. Beneficio: estipendio mensual + acceso a laboratorio de idiomas.'),
(304, '3572c2f2-e6c0-9f83-436d-236fbe41d611', '0181be63-4cba-9c80-2604-03c06121475b',
 'Campaña de donación de libros para la Biblioteca FHCE. El material será destinado a comunidades rurales del departamento. Punto de recolección: Hall principal de la facultad. Hasta el 30 de junio.')
ON CONFLICT DO NOTHING;

-- ── MEDIA (imágenes de posts) ─────────────────────────────────
INSERT INTO public.media (id, uuid, content_id, number, file_name, file_type, file_path) VALUES
-- DPA
(100, '17ucf8a1-09e0-4435-a850-0613d778897b', '1e5294f8-9a6c-4d9e-aea7-da7e80bd5c6f', 1, 'dpa-post-01', 'image', '/api/v1/images/posts/dpa-post-img-000000000100'),
(101, '24l5f8a1-09e0-4435-a850-0613d778897b', '6i2494f8-9a6c-4d9e-aea7-da7e80bd5c6f', 1, 'dpa-post-02', 'image', '/api/v1/images/posts/dpa-post-img-000000000101'),
(102, '79a3f8a1-09e0-4435-a850-0613d778897b', '8f3194f8-9a6c-4d9e-aea7-da7e80bd5c6f', 1, 'dpa-post-03', 'image', '/api/v1/images/posts/dpa-post-img-000000000102'),
(103, '38j1f8a1-09e0-4435-a850-0613d778897b', '5l6n94f8-9a6c-4d9e-aea7-da7e80bd5c6f', 1, 'dpa-post-04', 'image', '/api/v1/images/posts/dpa-post-img-000000000103'),
(104, '2k9zf8a1-09e0-4435-a850-0613d778897b', '2v4z94f8-9a6c-4d9e-aea7-da7e80bd5c6f', 1, 'dpa-post-05', 'image', '/api/v1/images/posts/dpa-post-img-000000000104'),
(105, '6m1vf8a1-09e0-4435-a850-0613d778897b', '7o1b94f8-9a6c-4d9e-aea7-da7e80bd5c6f', 1, 'dpa-post-06', 'image', '/api/v1/images/posts/dpa-post-img-000000000105'),
(106, '17b9m4z3-09e0-4435-a850-0613d778897b', '3m8c52v4-9a6c-4d9e-aea7-da7e80bd5c6f', 1, 'dpa-post-07', 'image', '/api/v1/images/posts/dpa-post-img-000000000106'),
(107, '5jc8x7v4-09e0-4435-a850-0613d778897b', '1om5x7n2-9a6c-4d9e-aea7-da7e80bd5c6f', 1, 'dpa-post-08', 'image', '/api/v1/images/posts/dpa-post-img-000000000107'),
-- FCyT
(200, '96e70d08-9568-13b1-35f8-4b06e8ef444f', 'bd8b53b2-5683-1d88-1b12-d5b78c02f8ad', 1, 'fcy-post-01', 'image', '/api/v1/images/posts/fcy-post-img-000000000200'),
(201, '346b891e-945a-e865-8d83-e3a911f09a22', '8fdd0cab-b64c-73f3-52da-dc2744f03bf1', 1, 'fcy-post-02', 'image', '/api/v1/images/posts/fcy-post-img-000000000201'),
(202, '44ea97da-2374-1a29-cf89-eef839bf25f2', 'aa91b489-88e0-2442-881d-cc1c5638597b', 1, 'fcy-post-03', 'image', '/api/v1/images/posts/fcy-post-img-000000000202'),
(203, '430ac674-7f33-fbe2-2b47-2e98e4b637f8', '937141c5-1698-8926-8fda-17be7718ab75', 1, 'fcy-post-04', 'image', '/api/v1/images/posts/fcy-post-img-000000000203'),
(204, 'f957763c-2b5e-0a59-e5ce-82ec5bbde71f', '7c381afe-5442-d755-fa47-cb4ad317d824', 1, 'fcy-post-05', 'image', '/api/v1/images/posts/fcy-post-img-000000000204'),
-- FHCE
(300, 'fb11fddd-5a84-efbf-61f0-741d878f6577', 'be6bbec1-c53a-782a-e690-06be20bf7dbd', 1, 'fhc-post-01', 'image', '/api/v1/images/posts/fhc-post-img-000000000300'),
(301, '89ae9242-560a-6708-66f8-c25cc09fe16b', '321edf84-1395-f851-df13-1f04c1611e8b', 1, 'fhc-post-02', 'image', '/api/v1/images/posts/fhc-post-img-000000000301'),
(302, '59befcc2-99aa-c15b-d633-5c82949a344f', '602d9c05-3f6d-2ba4-8e34-3e7a3a28a643', 1, 'fhc-post-03', 'image', '/api/v1/images/posts/fhc-post-img-000000000302'),
(303, '00b2b552-68a3-afde-424a-df3c2873c926', '4858067c-cb65-bacb-75a2-0458a63bfdef', 1, 'fhc-post-04', 'image', '/api/v1/images/posts/fhc-post-img-000000000303'),
(304, 'ca5f2ddf-9ba3-c794-f3f8-21875fbc416f', '0181be63-4cba-9c80-2604-03c06121475b', 1, 'fhc-post-05', 'image', '/api/v1/images/posts/fhc-post-img-000000000304')
ON CONFLICT DO NOTHING;

-- ── POSTS ─────────────────────────────────────────────────────
-- comment_config UUIDs: FREE='875d7d7f-...' RESTRICTED='587d7d7f-...' MODERATED='492d7d7f-...'
INSERT INTO public.post (id, uuid, institution_id, user_id, comment_config_id, content_id, post_date, post_type, deleted) VALUES
-- DPA
(100, '5f9ab4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '875d7d7f-7a1c-4b77-ab63-77a9f76759d0', '1e5294f8-9a6c-4d9e-aea7-da7e80bd5c6f', '2025-02-18 07:53:22', 'GENERAL',   FALSE),
(101, '7h3ab4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '875d7d7f-7a1c-4b77-ab63-77a9f76759d0', '6i2494f8-9a6c-4d9e-aea7-da7e80bd5c6f', '2025-03-05 19:31:22', 'GENERAL',   FALSE),
(102, '2k9db4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '587d7d7f-5g3n-4b77-cf98-77a9h46759d0', '8f3194f8-9a6c-4d9e-aea7-da7e80bd5c6f', '2025-03-11 08:00:22', 'GENERAL',   FALSE),
(103, '8s2ib4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '875d7d7f-7a1c-4b77-ab63-77a9f76759d0', '5l6n94f8-9a6c-4d9e-aea7-da7e80bd5c6f', '2025-07-02 08:00:22', 'GENERAL',   FALSE),
(104, '3g9ab4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '875d7d7f-7a1c-4b77-ab63-77a9f76759d0', '2v4z94f8-9a6c-4d9e-aea7-da7e80bd5c6f', '2025-07-02 10:25:22', 'CUDIE',     FALSE),
(105, '5n1ib4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '875d7d7f-7a1c-4b77-ab63-77a9f76759d0', '7o1b94f8-9a6c-4d9e-aea7-da7e80bd5c6f', '2025-07-03 08:00:12', 'PROYECTOS', FALSE),
(106, '9v4mb4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '492d7d7f-9f4s-8g45-hy34-77a9h46759d0', '3m8c52v4-9a6c-4d9e-aea7-da7e80bd5c6f', '2025-07-04 19:30:26', 'BECAS',     FALSE),
(107, '1ib6k3c5-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '875d7d7f-7a1c-4b77-ab63-77a9f76759d0', '1om5x7n2-9a6c-4d9e-aea7-da7e80bd5c6f', '2025-07-14 10:51:18', 'CONVENIOS', FALSE),
-- FCyT
(200, 'a6a74cbc-b1b2-2679-9ea1-e241c8448eab', 'cc000011-4000-4000-8000-000000000011', 'cc000200-4000-4000-8000-000000000200', '875d7d7f-7a1c-4b77-ab63-77a9f76759d0', 'bd8b53b2-5683-1d88-1b12-d5b78c02f8ad', '2025-04-20 09:00:00', 'GENERAL', FALSE),
(201, '4fb98da0-cdd9-320b-0895-63358617c972', 'cc000011-4000-4000-8000-000000000011', 'cc000200-4000-4000-8000-000000000200', '875d7d7f-7a1c-4b77-ab63-77a9f76759d0', '8fdd0cab-b64c-73f3-52da-dc2744f03bf1', '2025-05-15 10:00:00', 'GENERAL', FALSE),
(202, 'e67ef701-e045-3208-948e-cd87f4b0c31f', 'cc000011-4000-4000-8000-000000000011', 'cc000201-4000-4000-8000-000000000201', '492d7d7f-9f4s-8g45-hy34-77a9h46759d0', 'aa91b489-88e0-2442-881d-cc1c5638597b', '2025-06-01 08:30:00', 'GENERAL', FALSE),
(203, '00f1a699-ad5a-ca0b-447a-8df452bb813d', 'cc000011-4000-4000-8000-000000000011', 'cc000201-4000-4000-8000-000000000201', '875d7d7f-7a1c-4b77-ab63-77a9f76759d0', '937141c5-1698-8926-8fda-17be7718ab75', '2025-06-20 14:00:00', 'GENERAL', FALSE),
(204, 'a0ef86d4-a6cd-1b0e-2d9e-364435f77877', 'cc000011-4000-4000-8000-000000000011', 'cc000200-4000-4000-8000-000000000200', '875d7d7f-7a1c-4b77-ab63-77a9f76759d0', '7c381afe-5442-d755-fa47-cb4ad317d824', '2025-07-05 09:00:00', 'GENERAL', FALSE),
-- FHCE
(300, 'b00c220d-27e1-c4f9-38e2-0f3bc78001d8', 'dd000012-4000-4000-8000-000000000012', 'dd000300-4000-4000-8000-000000000300', '875d7d7f-7a1c-4b77-ab63-77a9f76759d0', 'be6bbec1-c53a-782a-e690-06be20bf7dbd', '2025-04-25 09:00:00', 'GENERAL', FALSE),
(301, 'b8c0a884-0010-e165-da5e-ba3dd06a8949', 'dd000012-4000-4000-8000-000000000012', 'dd000300-4000-4000-8000-000000000300', '875d7d7f-7a1c-4b77-ab63-77a9f76759d0', '321edf84-1395-f851-df13-1f04c1611e8b', '2025-05-25 10:00:00', 'GENERAL', FALSE),
(302, '04a319b5-c4ac-8d40-0d60-915b6a4ffb94', 'dd000012-4000-4000-8000-000000000012', 'dd000301-4000-4000-8000-000000000301', '492d7d7f-9f4s-8g45-hy34-77a9h46759d0', '602d9c05-3f6d-2ba4-8e34-3e7a3a28a643', '2025-06-10 08:00:00', 'GENERAL', FALSE),
(303, 'f3c86fa7-f528-392b-2eb1-5864e0535060', 'dd000012-4000-4000-8000-000000000012', 'dd000301-4000-4000-8000-000000000301', '875d7d7f-7a1c-4b77-ab63-77a9f76759d0', '4858067c-cb65-bacb-75a2-0458a63bfdef', '2025-06-25 14:00:00', 'GENERAL', FALSE),
(304, '5427d5a8-3392-8c27-ea30-f133d154f442', 'dd000012-4000-4000-8000-000000000012', 'dd000300-4000-4000-8000-000000000300', '875d7d7f-7a1c-4b77-ab63-77a9f76759d0', '0181be63-4cba-9c80-2604-03c06121475b', '2025-07-08 09:00:00', 'GENERAL', FALSE)
ON CONFLICT DO NOTHING;


-- ─────────────────────────────────────────────────────────────
-- 7. GRUPOS + POST_GROUP
-- ─────────────────────────────────────────────────────────────
-- groups usa GENERATED BY DEFAULT AS IDENTITY
INSERT INTO public.groups (id, uuid, name, status, institution_id) VALUES
(100, 'dpa-grp-100-0000-0000-000000000100', 'Fijados',   'CREATED', '93j203b4-f63b-4c4a-be05-eae84cef0c0c'),
(101, 'dpa-grp-101-0000-0000-000000000101', 'Destacados', 'CREATED', '93j203b4-f63b-4c4a-be05-eae84cef0c0c'),
(200, 'fcy-grp-200-0000-0000-000000000200', 'Fijados',   'CREATED', 'cc000011-4000-4000-8000-000000000011'),
(201, 'fcy-grp-201-0000-0000-000000000201', 'Destacados', 'CREATED', 'cc000011-4000-4000-8000-000000000011'),
(300, 'fhc-grp-300-0000-0000-000000000300', 'Fijados',   'CREATED', 'dd000012-4000-4000-8000-000000000012'),
(301, 'fhc-grp-301-0000-0000-000000000301', 'Destacados', 'CREATED', 'dd000012-4000-4000-8000-000000000012')
ON CONFLICT DO NOTHING;

INSERT INTO public.post_group (group_id, post_id) VALUES
-- DPA: fijados → posts 102 y 107; destacados → posts 100 y 104
('dpa-grp-100-0000-0000-000000000100', '2k9db4e8-0856-4aad-b3aa-747e2dba76d9'),
('dpa-grp-100-0000-0000-000000000100', '1ib6k3c5-0856-4aad-b3aa-747e2dba76d9'),
('dpa-grp-101-0000-0000-000000000101', '5f9ab4e8-0856-4aad-b3aa-747e2dba76d9'),
('dpa-grp-101-0000-0000-000000000101', '3g9ab4e8-0856-4aad-b3aa-747e2dba76d9'),
-- FCyT: fijados → post 202; destacados → post 200
('fcy-grp-200-0000-0000-000000000200', 'e67ef701-e045-3208-948e-cd87f4b0c31f'),
('fcy-grp-201-0000-0000-000000000201', 'a6a74cbc-b1b2-2679-9ea1-e241c8448eab'),
-- FHCE: fijados → post 302; destacados → post 300
('fhc-grp-300-0000-0000-000000000300', '04a319b5-c4ac-8d40-0d60-915b6a4ffb94'),
('fhc-grp-301-0000-0000-000000000301', 'b00c220d-27e1-c4f9-38e2-0f3bc78001d8')
ON CONFLICT DO NOTHING;


-- ─────────────────────────────────────────────────────────────
-- 8. FOLLOWERS
--    institution_id = integer (id de institution), user_id = integer (id_user)
--    Incluye un caso cross-tenant: usuario FCyT siguiendo a DPA
-- ─────────────────────────────────────────────────────────────
INSERT INTO public.followers (institution_id, user_id, followed_since) VALUES
(10, 114, '2025-01-15 10:00:00'),
(10, 115, '2025-01-20 11:00:00'),
(10, 116, '2025-02-01 09:00:00'),
(10, 117, '2025-02-10 08:30:00'),
(10, 118, '2025-03-05 14:00:00'),
(11, 210, '2025-02-01 09:00:00'),
(11, 211, '2025-02-15 10:30:00'),
(11, 212, '2025-03-01 11:00:00'),
(12, 310, '2025-02-20 09:00:00'),
(12, 311, '2025-03-10 10:00:00'),
(12, 312, '2025-04-01 08:00:00'),
-- Cross-tenant: usuario FCyT siguiendo DPA (caso válido de testing)
(10, 210, '2025-03-15 15:00:00')
ON CONFLICT DO NOTHING;


-- ─────────────────────────────────────────────────────────────
-- 9. COMENTARIOS
--    Mix de estados para testing del módulo de moderación:
--    VISIBLE → comentario normal aprobado
--    PENDING_APPROVAL → retenido por el moderador (moderated=true)
--    REJECTED → rechazado por el moderador
--    REMOVED → eliminado por el autor o admin
-- ─────────────────────────────────────────────────────────────
INSERT INTO public.comment (id, uuid, post_id, user_id, institution_id, content, moderated, state, comment_date) VALUES
-- DPA — post 100 (FREE_COMMENTS)
(100, 'c01a0100-d9a0-4c4a-8d9d-000000000100', 100, 114, '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'A qué hora empieza el conversatorio?', FALSE, 'VISIBLE', '2025-02-18 10:30:00'),
(101, 'c01a0101-d9a0-4c4a-8d9d-000000000101', 100, 118, '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'Transmitirán por Facebook Live?', FALSE, 'VISIBLE', '2025-02-18 12:15:00'),
(102, 'c01a0102-d9a0-4c4a-8d9d-000000000102', 100, 120, '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'Estaré ahí sin falta!', FALSE, 'VISIBLE', '2025-02-19 08:00:00'),
-- DPA — post 101
(103, 'c01a0103-d9a0-4c4a-8d9d-000000000103', 101, 119, '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'Hay algún descuento para externos?', FALSE, 'VISIBLE', '2025-03-06 09:20:00'),
(104, 'c01a0104-d9a0-4c4a-8d9d-000000000104', 101, 117, '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'En qué horarios puedo inscribirme?', FALSE, 'VISIBLE', '2025-03-06 11:40:00'),
-- DPA — post 105 (MODERATED): todos los comentarios pasan por moderador
(105, 'c01a0105-d9a0-4c4a-8d9d-000000000105', 105, 110, '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'Excelente iniciativa de la DPA!', TRUE, 'VISIBLE', '2025-07-03 10:00:00'),
(106, 'c01a0106-d9a0-4c4a-8d9d-000000000106', 105, 116, '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'Dónde puedo inscribirme por favor?', TRUE, 'PENDING_APPROVAL', '2025-07-03 13:37:00'),
(107, 'c01a0107-d9a0-4c4a-8d9d-000000000107', 106, 113, '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'Palabras inapropiadas eliminadas por el moderador', TRUE, 'REJECTED', '2025-07-04 21:53:00'),
(108, 'c01a0108-d9a0-4c4a-8d9d-000000000108', 106, 114, '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'No pude ir, qué números salieron premiados?', TRUE, 'PENDING_APPROVAL', '2025-07-05 15:04:00'),
(109, 'c01a0109-d9a0-4c4a-8d9d-000000000109', 107, 115, '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'Comentario eliminado por el autor', FALSE, 'REMOVED', '2025-07-14 16:31:00'),
-- FCyT — post 200
(200, 'c02a0200-d9a0-4c4a-8d9d-000000000200', 200, 210, 'cc000011-4000-4000-8000-000000000011', 'A qué hora comienza la Feria?', FALSE, 'VISIBLE', '2025-04-21 09:00:00'),
(201, 'c02a0201-d9a0-4c4a-8d9d-000000000201', 200, 211, 'cc000011-4000-4000-8000-000000000011', 'Es obligatorio exponer un proyecto?', FALSE, 'VISIBLE', '2025-04-21 10:30:00'),
-- FCyT — post 201
(202, 'c02a0202-d9a0-4c4a-8d9d-000000000202', 201, 212, 'cc000011-4000-4000-8000-000000000011', 'Cuáles son los requisitos exactos?', FALSE, 'VISIBLE', '2025-05-16 11:00:00'),
(203, 'c02a0203-d9a0-4c4a-8d9d-000000000203', 201, 210, 'cc000011-4000-4000-8000-000000000011', 'Spam detectado por el moderador', TRUE, 'REJECTED', '2025-05-17 08:00:00'),
-- FCyT — post 202 (MODERATED)
(204, 'c02a0204-d9a0-4c4a-8d9d-000000000204', 202, 211, 'cc000011-4000-4000-8000-000000000011', 'Habrá transmisión virtual?', TRUE, 'PENDING_APPROVAL', '2025-06-01 14:00:00'),
(205, 'c02a0205-d9a0-4c4a-8d9d-000000000205', 202, 212, 'cc000011-4000-4000-8000-000000000011', 'Comentario eliminado', FALSE, 'REMOVED', '2025-06-02 09:00:00'),
-- FHCE — post 300
(300, 'c03a0300-d9a0-4c4a-8d9d-000000000300', 300, 310, 'dd000012-4000-4000-8000-000000000012', 'El congreso es solo para docentes?', FALSE, 'VISIBLE', '2025-04-26 10:00:00'),
(301, 'c03a0301-d9a0-4c4a-8d9d-000000000301', 300, 311, 'dd000012-4000-4000-8000-000000000012', 'Qué carrera organiza el evento?', FALSE, 'VISIBLE', '2025-04-26 11:30:00'),
-- FHCE — post 301
(302, 'c03a0302-d9a0-4c4a-8d9d-000000000302', 301, 312, 'dd000012-4000-4000-8000-000000000012', 'Felicitaciones a los nuevos licenciados!', FALSE, 'VISIBLE', '2025-05-26 09:00:00'),
-- FHCE — post 303 (MODERATED)
(303, 'c03a0303-d9a0-4c4a-8d9d-000000000303', 303, 310, 'dd000012-4000-4000-8000-000000000012', 'Cuál es el promedio mínimo requerido?', TRUE, 'VISIBLE', '2025-06-26 10:00:00'),
(304, 'c03a0304-d9a0-4c4a-8d9d-000000000304', 303, 311, 'dd000012-4000-4000-8000-000000000012', 'Comentario en revisión por moderador', TRUE, 'PENDING_APPROVAL', '2025-06-26 14:00:00'),
(305, 'c03a0305-d9a0-4c4a-8d9d-000000000305', 303, 312, 'dd000012-4000-4000-8000-000000000012', 'Contenido inapropiado rechazado', TRUE, 'REJECTED', '2025-06-27 08:00:00')
ON CONFLICT DO NOTHING;


-- ─────────────────────────────────────────────────────────────
-- 10. REPLIES (respuestas a comentarios)
--     reply_id GENERATED BY DEFAULT AS IDENTITY
--     comment_id y user_id son enteros
-- ─────────────────────────────────────────────────────────────
INSERT INTO public.replies (reply_id, comment_id, parent_reply_id, user_id, uuid, content, created_at) VALUES
-- DPA: respuestas al comentario 100
(100, 100, NULL, 115, '74cb6ca9-69c8-be93-8443-508c8622026a', 'Gracias por la información!', '2025-02-18 11:00:00+00'),
(101, 100, 100,  117, 'ee508d0b-c798-e232-0a63-0de1e1ab46c9', 'De nada, nos vemos allá!', '2025-02-18 11:30:00+00'),
-- DPA: respuesta al comentario 103
(102, 103, NULL, 119, '08371267-8bb9-c9db-1e85-e41dd21683fa', 'Exacto, muy útil la convocatoria', '2025-03-07 09:00:00+00'),
-- FCyT: respuesta al comentario 200
(200, 200, NULL, 211, '38773245-881e-10e8-8bf2-29c8cfff94e3', 'Yo también quiero saber el horario', '2025-04-22 10:00:00+00'),
(201, 200, 200,  210, 'c6bd81ab-4a61-2273-1440-b9843874194c', 'Parece que es a las 9am según el afiche', '2025-04-22 11:00:00+00'),
-- FHCE: respuesta al comentario 300
(300, 300, NULL, 311, 'c40e884c-9f67-3afc-bd5c-b595b3ea689b', 'Buen punto, yo también tengo esa duda', '2025-04-27 10:00:00+00')
ON CONFLICT DO NOTHING;


-- ─────────────────────────────────────────────────────────────
-- 11. REACCIONES A POSTS
--     post_id, user_id, emoji_type_id → todos varchar(36) UUID
-- ─────────────────────────────────────────────────────────────
INSERT INTO public.post_reaction (id, uuid, post_id, user_id, emoji_type_id, reaction_date, deleted) VALUES
-- DPA reacciones al post 100
(100, '8aaadcfa-0729-fbad-1d49-fb85687d81cc', '5f9ab4e8-0856-4aad-b3aa-747e2dba76d9', '9bae1fe0-2c56-4091-883d-15458e051500', '3f696a78-c73f-475c-80a6-f5a858648af1', '2025-02-18 14:00:00', FALSE),
(101, '903f3cf4-5a61-c814-789e-a83e13d9d261', '5f9ab4e8-0856-4aad-b3aa-747e2dba76d9', '844b324d-2f5e-41c9-b726-0149eeb01157', '7v236a78-c73f-475c-80a6-f5a858648af1', '2025-02-18 15:00:00', FALSE),
(102, '1ee35214-e5c9-6b11-3c24-db7c196e5ade', '5f9ab4e8-0856-4aad-b3aa-747e2dba76d9', 'ae542968-1335-425e-a206-283c38a20190', '3f696a78-c73f-475c-80a6-f5a858648af1', '2025-02-19 09:00:00', FALSE),
-- DPA reacciones al post 101
(103, '3a56cd7a-39ae-2bc6-6a10-bca5ecb68fa1', '7h3ab4e8-0856-4aad-b3aa-747e2dba76d9', '844b324d-2f5e-41c9-b726-0149eeb01157', '3f696a78-c73f-475c-80a6-f5a858648af1', '2025-03-06 10:00:00', FALSE),
(104, '91ee041d-dad0-247a-bb36-f6827dc5fe8f', '7h3ab4e8-0856-4aad-b3aa-747e2dba76d9', '2e121a51-24b5-4aad-92c2-150997ec4266', '7v236a78-c73f-475c-80a6-f5a858648af1', '2025-03-06 11:00:00', FALSE),
-- DPA reacciones al post 104
(105, 'a2e82cd4-5d03-dd61-d983-14eebd6a855b', '3g9ab4e8-0856-4aad-b3aa-747e2dba76d9', 'e54bd4dc-d8f6-42e9-8e94-5d56bf42416f', 'l6m3bd82-c73f-475c-80a6-f5a858648af1', '2025-07-03 12:00:00', FALSE),
(106, '6eabbc2d-2da0-00a8-a177-7675260b1b66', '3g9ab4e8-0856-4aad-b3aa-747e2dba76d9', 'ab6e1a7f-4494-4251-8177-dc5c4fe18740', '3f696a78-c73f-475c-80a6-f5a858648af1', '2025-07-03 13:00:00', FALSE),
-- FCyT reacciones al post 200
(200, 'e09f36ea-b5e8-f9b2-278e-4bb13a2d08e3', 'a6a74cbc-b1b2-2679-9ea1-e241c8448eab', 'cc000210-4000-4000-8000-000000000210', '3f696a78-c73f-475c-80a6-f5a858648af1', '2025-04-20 10:00:00', FALSE),
(201, 'e2681423-bb2c-099d-4ed5-64aec6b68f14', 'a6a74cbc-b1b2-2679-9ea1-e241c8448eab', 'cc000211-4000-4000-8000-000000000211', '7v236a78-c73f-475c-80a6-f5a858648af1', '2025-04-20 11:00:00', FALSE),
(202, '11fe8a38-46dc-4df3-ce7c-f55020afc1e5', '4fb98da0-cdd9-320b-0895-63358617c972', 'cc000212-4000-4000-8000-000000000212', 'l6m3bd82-c73f-475c-80a6-f5a858648af1', '2025-05-16 09:00:00', FALSE),
-- FHCE reacciones al post 300
(300, '9b791d35-8aca-6b79-a4d1-6fa903ba75c2', 'b00c220d-27e1-c4f9-38e2-0f3bc78001d8', 'dd000310-4000-4000-8000-000000000310', '3f696a78-c73f-475c-80a6-f5a858648af1', '2025-04-25 10:00:00', FALSE),
(301, 'f05b0898-c07e-4aca-c42a-290f34a475bb', 'b00c220d-27e1-c4f9-38e2-0f3bc78001d8', 'dd000311-4000-4000-8000-000000000311', '7v236a78-c73f-475c-80a6-f5a858648af1', '2025-04-25 11:00:00', FALSE),
(302, 'ed320321-a08c-ea7c-0d08-a59e91a83fc2', 'b8c0a884-0010-e165-da5e-ba3dd06a8949', 'dd000312-4000-4000-8000-000000000312', '3f696a78-c73f-475c-80a6-f5a858648af1', '2025-05-26 10:00:00', FALSE)
ON CONFLICT DO NOTHING;


-- ─────────────────────────────────────────────────────────────
-- 12. REACCIONES A COMENTARIOS
--     comment_id → uuid del comentario (varchar)
--     user_id, emoji_type_id → uuid (varchar)
-- ─────────────────────────────────────────────────────────────
INSERT INTO public.comment_reaction (id, uuid, comment_id, user_id, emoji_type_id, reaction_date, deleted) VALUES
-- DPA: reacciones al comentario 100
(100, 'dpa-creact-100-0000-000000000100', 'c01a0100-d9a0-4c4a-8d9d-000000000100', '844b324d-2f5e-41c9-b726-0149eeb01157', '3f696a78-c73f-475c-80a6-f5a858648af1', '2025-02-18 13:00:00', FALSE),
(101, 'dpa-creact-101-0000-000000000101', 'c01a0100-d9a0-4c4a-8d9d-000000000100', '9f32985a-f108-4b19-9bda-cab7c501ae68', '7v236a78-c73f-475c-80a6-f5a858648af1', '2025-02-18 14:30:00', FALSE),
-- FCyT: reacciones al comentario 200
(200, 'fcy-creact-200-0000-000000000200', 'c02a0200-d9a0-4c4a-8d9d-000000000200', 'cc000211-4000-4000-8000-000000000211', '3f696a78-c73f-475c-80a6-f5a858648af1', '2025-04-21 12:00:00', FALSE),
-- FHCE: reacciones al comentario 300
(300, 'fhc-creact-300-0000-000000000300', 'c03a0300-d9a0-4c4a-8d9d-000000000300', 'dd000311-4000-4000-8000-000000000311', '3f696a78-c73f-475c-80a6-f5a858648af1', '2025-04-26 15:00:00', FALSE)
ON CONFLICT DO NOTHING;


-- ─────────────────────────────────────────────────────────────
-- 13. REACCIONES A REPLIES
--     reply_id → integer, user_id → integer, emoji_type_id → integer
-- ─────────────────────────────────────────────────────────────
INSERT INTO public.reply_reactions (id, reply_id, user_id, emoji_type_id, uuid, reaction_date) VALUES
(100, 100, 116, 100, 'dpa-rreact-100-0000-000000000100', '2025-02-18 12:00:00'),
(200, 200, 212, 100, 'fcy-rreact-200-0000-000000000200', '2025-04-22 12:00:00')
ON CONFLICT DO NOTHING;

insert into image_file (id, uuid, name, url_resource, status, type) values
    (113, 'f1309a27-93eb-4429-a096-d786f8d16f5c', 'img14', '/api/v1/images/inst-profile/f1309a27-93eb-4429-a096-d786f8d16f5c', 'SAVED_SUCCESSFULLY','image/png'),
    (114, '14bacba4-b962-40b5-9dd1-d5bf8e1e86f8', 'img15', '/api/v1/images/inst-cover/14bacba4-b962-40b5-9dd1-d5bf8e1e86f8', 'SAVED_SUCCESSFULLY','image/png'),
    (115, 'd22882a6-5f9c-4cd3-95dc-534c5d5f05a8', 'img14', '/api/v1/images/inst-profile/d22882a6-5f9c-4cd3-95dc-534c5d5f05a8', 'SAVED_SUCCESSFULLY','image/png'),
    (116, '75440bfb-6cd3-4a26-8038-79fc8859f753', 'img15', '/api/v1/images/inst-cover/75440bfb-6cd3-4a26-8038-79fc8859f753', 'SAVED_SUCCESSFULLY','image/png'),
    (117, '7baffe0f-4f73-4a67-981a-0bcf906628a2', 'img14', '/api/v1/images/inst-profile/7baffe0f-4f73-4a67-981a-0bcf906628a2', 'SAVED_SUCCESSFULLY','image/png'),
    (118, 'bb6428a1-3daf-4dc5-a72f-d33bc99c3a9b', 'img15', '/api/v1/images/inst-cover/bb6428a1-3daf-4dc5-a72f-d33bc99c3a9b', 'SAVED_SUCCESSFULLY','image/png')
    ON CONFLICT DO NOTHING;




-- ********************************************************************************************************************************
-- Phase 1: Add new table uploaded_file and migrate data from media.file_path
-- Migrar datos existentes de las tres tablas redundantes
INSERT INTO public.uploaded_file (uuid, name, url_resource, category, mime_type, status)
SELECT uuid, name, url_resource, 'IMAGE', type, status
FROM public.image_file
ON CONFLICT (uuid) DO NOTHING;


-- Phase 2: Add FK uploaded_file_id to media and article_media tables

-- 2. Populate uploaded_file_id from matching url_resource
    UPDATE public.media m
    SET uploaded_file_id = uf.id
    FROM public.uploaded_file uf
    WHERE m.file_path = uf.url_resource;

-- 3. Drop file_path from media
    ALTER TABLE public.media
    DROP COLUMN IF EXISTS file_path;

-- 4 Delete tables image_file, video_file and document_file
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
-- institution_seq ya se resetea en V5 a 200 (DPA=10, FCyT=11, FHCE=12 → todo bajo 200)

