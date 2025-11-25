insert into role(id_role, name) values
	(1, 'ADMIN'),
    (2, 'STUDENT'),
    (3, 'MODERATOR'),
    (4, 'ADMIN_CONVENIOS'),
    (5, 'ADMIN_PROYECTOS'),
    (6, 'ADMIN_BECAS'),
    (7, 'ADMIN_CUDIE');

insert into users (id_user, uuid, name, last_name, email, phone, password, photo_cover_path, photo_profile_path) values
    (100, 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Mguzman', 'Guzman', 'micael.guzman@dpa.umss.edu.bo', '71795251', '$2a$10$lwZguPYFKEiIZYyCW4piVuyHb6hn6MGW3r7DOKg40BhPO3We5cwGu', null, 'https://devpws.cs.umss.edu.bo/api/v1/images/users/af87625e-3075-41a5-901a-cf950330b237'),
    (101, 'eab72365-d8c1-45df-9b48-274f64c65b86', 'JCahuaya', 'Jhonny Cahuaya', 'jhonny.cahuaya@umss.edu.bo', '70755958', '$2a$10$g46noOikL88uT7pNfj4Q/OVOyBtgfh6yFqixUxd4lww3UZrLeIOly', null, 'https://devpws.cs.umss.edu.bo/api/v1/images/users/e867fa32-75b7-4c3c-8943-4a2808c10ac0'),
    (110, '9bae1fe0-2c56-4091-883d-15458e051500', 'Emely', 'Fernandez Soliz', 'admin-convenios@umss.edu', '69577142', '$2a$10$ElXlBz1WzNDhanqtCmszb.bub/iKGcNQgcbr31MQe9wjsiim7Q5zG', null, 'https://devpws.cs.umss.edu.bo/api/v1/images/users/733b01ba-198e-42db-b9ca-d7ace9a7abad'),
    (111, '844b324d-2f5e-41c9-b726-0149eeb01157', 'Javier', 'Lopez Canedo', 'admin-proyectos@umss.edu', '71852821', '$2a$10$Gm0O/HK8qXeqa/XPvrm4/uAQj2.G3XdKOHg8XTxG4Gif1UYDPcbEa', null, 'https://devpws.cs.umss.edu.bo/api/v1/images/users/06b8fbe5-9d91-422b-bd20-6d89d23e5608'),
    (112, '2e121a51-24b5-4aad-92c2-150997ec4266', 'Leonardo', 'Beltran Ramirez', 'admin-becas@umss.edu', '75891148', '$2a$10$vs8E1jwz6Z3BBitXHP.Y/uVQ0pNkqHhXf0oQCFH7Nf/rjccHL2lCO', null, 'https://devpws.cs.umss.edu.bo/api/v1/images/users/b5de2b13-4720-41e7-87d2-d31abd590fac'),
    (113, 'c332c4ff-49e8-4e0e-a7f4-d59907d8cda3', 'Valeria', 'Gonzales Vargas', 'admin-cudie@umss.edu', '78823541', '$2a$10$tt2avNxEODCYrI9qHFqr5elrI5O87dVRNQZGUgAHVMUi8XtWnwXE.', null, 'https://devpws.cs.umss.edu.bo/api/v1/images/users/5e9f9c0d-4c31-4335-a11e-b9c7f6f8a2b1'),
    (114, '9f32985a-f108-4b19-9bda-cab7c501ae68', 'Jose', U&'Monta\00F1o Laura', '202001823@est.umss.edu', '72566218', '$2a$10$6HOIAlIZ2.KXIi8qL1AHxOIs3zHYydtV3QnZ.wJ9wrr18qhJEi576', null, 'https://devpws.cs.umss.edu.bo/api/v1/images/users/1bed8052-ba51-41c2-88b5-e39e78dbf420'),
    (115, 'ab6e1a7f-4494-4251-8177-dc5c4fe18740', 'Marcos', 'Illanes Martinez', '201800513@est.umss.edu', '74269527', '$2a$10$96.BNIsnUH7u.eVTeWi7Ju/exv59yfngw.TdVVOv5ladZ87KBZLaK', null, 'https://devpws.cs.umss.edu.bo/api/v1/images/users/28e3d103-76e8-4b0d-b04c-cf2e8821cf6e'),
    (116, 'f8d1c969-af76-4161-b0d7-9c2dfc47e75c', 'Adriana', 'Boza Ruiz', '201904940@est.umss.edu', '70551293', '$2a$10$LRWTNQMxRoDEGUKNeZe86OOg7.aqxc1TE/HhGUJ01zhPBB2E1O/A.', null, 'https://devpws.cs.umss.edu.bo/api/v1/images/users/f9c89556-cd27-481a-a8bd-59f4915f7dfa'),
    (117, 'd33ded75-c9f3-4fef-9762-0ba4a905efa8', 'Antonio', 'Monje Aranibar', '201604527@est.umss.edu', '62933721', '$2a$10$m8FJFWAPDKIAU8SykyKmme7FcaQqrkHtg0zczh3Gn0lSgVeTZNaAy', null, 'https://devpws.cs.umss.edu.bo/api/v1/images/users/b3a64fc9-3d28-4b92-b296-2324f9c4e061'),
    (118, 'e54bd4dc-d8f6-42e9-8e94-5d56bf42416f', 'Jeyson', 'Valdivia Bernal', 'jeyson.valdivia@gmail.com', '76834814', '$2a$10$anTAKIK4bm55/HYFWVoER.zu1eNL7Iwy0UCGi3THN.wDW77WaLCKa', null, 'https://devpws.cs.umss.edu.bo/api/v1/images/users/e326bdb8-6eee-4814-a877-9e063183f410'),
    (119, 'ae542968-1335-425e-a206-283c38a20190', 'Omar', 'Argenes Quispe', 'argenes77@gmail.com', '65692585', '$2a$10$bO6cOzFhvWjqRRwn7a1/xupq3LxGO/hG.7cLpS14JAbksNjBGJCjW', null, 'https://devpws.cs.umss.edu.bo/api/v1/images/users/004fca51-d313-41c4-94ff-5908ddb58dc2'),
    (120, '7f9264d7-ca8d-41ca-be1f-d24c9dd244a2', 'Bianca', 'Antelo Dominguez', 'bianca.dominguez@gmail.com', '73597236', '$2a$10$QUoVqY/j3bdW5rOMO8aTY.Eaq2Ifju9a8Hm2ht7qex2ALF0tWVc1q', null, 'https://devpws.cs.umss.edu.bo/api/v1/images/users/2148f392-4f16-40b6-88e7-bd0ca32bd84a');

insert into user_roles (user_id, role_id) values
    (100, 1),
    (101, 1),
    (110, 4),
    (111, 5),
    (112, 6),
    (113, 7),
    (114, 2),
    (115, 2),
    (116, 2),
    (117, 2),
    (118, 2),
    (119, 2),
    (120, 2);

insert into comment_config (id, uuid, name, configuration_type) values
	(20, '875d7d7f-7a1c-4b77-ab63-77a9f76759d0', 'Todos pueden comentar', 'FREE_COMMENTS'),
    (21, '587d7d7f-5g3n-4b77-cf98-77a9h46759d0', 'Nadie puede comentar', 'RESTRICTED_COMMENTS'),
    (22, '492d7d7f-9f4s-8g45-hy34-77a9h46759d0', 'Comentarios con moderador', 'MODERATED_COMMENTS');

insert into institution (id, uuid, name, description, location, category, email, phone, url, logo_url, background_url) values
	(10, '93j203b4-f63b-4c4a-be05-eae84cef0c0c',
        unistr('Direcci\00F3n de Planificaci\00F3n Acad\00E9mica - DPA'),
        unistr('Direcci\00F3n de Planificaci\00F3n Académica de la Universidad Mayor de San Sim\00F3n'),
        U&'Av. Oquendo Prolongación Jordán, Edif: Multiacadémico Piso 3°, Cochabamba, Bolivia',
        U&'Sitio web de educaci\00F3n',
        'dpa@umss.edu.bo',
        '+591 4 4232970',
        'dpa.umss.edu.bo',
        'https://devpws.cs.umss.edu.bo/api/v1/images/inst-profile/f1309a27-93eb-4429-a096-d786f8d16f5c',
        'https://devpws.cs.umss.edu.bo/api/v1/images/inst-cover/14bacba4-b962-40b5-9dd1-d5bf8e1e86f8');

insert into content (id, uuid) values
	(100, '1e5294f8-9a6c-4d9e-aea7-da7e80bd5c6f'),
    (101, '6i2494f8-9a6c-4d9e-aea7-da7e80bd5c6f'),
    (102, '8f3194f8-9a6c-4d9e-aea7-da7e80bd5c6f'),
    (103, '5l6n94f8-9a6c-4d9e-aea7-da7e80bd5c6f'),
    (104, '2v4z94f8-9a6c-4d9e-aea7-da7e80bd5c6f'),
    (105, '7o1b94f8-9a6c-4d9e-aea7-da7e80bd5c6f'),
    (106, '3m8c52v4-9a6c-4d9e-aea7-da7e80bd5c6f'),
    (107, '1om5x7n2-9a6c-4d9e-aea7-da7e80bd5c6f');

insert into text (id, uuid, content_id, text) values
	(100, '3m22c948-o3j9-4e39-92a6-fa761f2410a7', '1e5294f8-9a6c-4d9e-aea7-da7e80bd5c6f', 'La Universidad Mayor de San Simón, la Dirección de Planificación Académica (DPA), a través del Departamento de Desarrollo Curricular (DDC), invitan a participar del Ciclo de Conferencias y Conversatorios: Rumbo hacia el nuevo Modelo Educativo de la Universidad Mayor de San Simón. Estos eventos académicos instaurarán espacios de diálogo y construcción colectiva en el que expertos profesionales, docentes e investigadores compartirán reflexiones y propuestas innovadoras para el fortalecimiento de nuestro nuevo Modelo Educativo que está en proceso de construcción.
CONVERSATORIO: FLEXIBILIDAD CURRICULAR - EXPERIENCIA ACADÉMICA DE LA UFABC (Universidad Federal de ABC)
Fecha: Jueves 20 de febrero de 2025
Hora: 9:30 a.m. a 11:00 a.m.
Lugar: Modalidad Virtual (Google Meet)
Expositor: Comisión Académica de la UFABC (Brasil)'),
    (101, '19b6c948-f5f4-4e39-92a6-fa761f2410a7', '6i2494f8-9a6c-4d9e-aea7-da7e80bd5c6f', 'Conversatorio: "TIC Aplicadas a la Educación Superior"
Estimada comunidad académica de la UMSS,
La Dirección de Planificación Académica y el Departamento de Desarrollo Curricular tiene el honor de invitarle al conversatorio "TIC Aplicadas a la Educación Superior", un espacio de diálogo e intercambio de experiencias sobre el impacto y las oportunidades que brindan las Tecnologías de la Información y Comunicación (TICs) en el ámbito educativo.
Fecha: 10 de marzo del 2025
Hora: 14:00 pm a 16:00 pm
Lugar: Aula Magna de la FHCE
Este evento está dirigido a docentes, investigadores, estudiantes y profesionales interesados en la innovación educativa y el uso de herramientas tecnológicas para mejorar los procesos de enseñanza y aprendizaje en la educación superior.
¡Esperamos contar con su presencia y enriquecer juntos esta importante conversación!'),
    (102, '52o7c948-f5f4-4e39-92a6-fa761f2410a7', '8f3194f8-9a6c-4d9e-aea7-da7e80bd5c6f', 'Seminario: " La IA dentro los espacios formativos"
Estimada Comunidad Académica,
La Universidad Mayor de San Simón a través de la Dirección de Planificación Académica y el Departamento de Desarrollo Curricular tiene el grado de invitarle al seminario "Inteligencia Artificial en los Espacios Formativos", un espacio de diálogo y reflexión sobre el impacto de la IA en la educación y su papel en la transformación del modelo educativo universitario.
Fecha: 13 de marzo del 2025
Hora: 19:30 a 21:30 pm
Lugar: Modalidad Virtual (Zoom: https://educadventista.zoom.us/j/82478062079)
En este seminario, se dará a conocer el papel importante de la IA en la educación, se compartirá experiencias, tendencias y estrategias para la integración de la IA en los procesos de enseñanza-aprendizaje. Su participación será fundamental para enriquecer el debate y contribuir a la construcción de un modelo educativo innovador y adaptado a los desafíos del siglo XXI.
Esperamos contar con su presencia.
#InteligenciaArtificial #EducaciónDelFuturo #SeminarioVirtual #InnovaciónEducativa'),
    (103, '25x4c948-f5f4-4e39-92a6-fa761f2410a7', '5l6n94f8-9a6c-4d9e-aea7-da7e80bd5c6f', U&'Impulsando procesos de innovación educativa en carreras y programas de la UMSS.
Facultad de Ciencias Agrícolas, Pecuarias y Forestales
En el marco del “Año de la Innovación Curricular”, la Universidad Mayor de San Simón (UMSS), a través de la Dirección de Planificación Académica (DPA )y el Departamento de Desarrollo Curricular (DDC), continúa desarrollando talleres dirigidos a autoridades, docentes y representantes estudiantiles de sus distintas facultades.
Una apuesta estratégica por la transformación curricular
Este miércoles 02 de julio de 2025, la Facultad de Ciencias Agrícolas, Pecuarias y Forestales será escenario de un taller que busca fortalecer los procesos de innovación educativa en sus programas de formación. El espacio permitirá generar conciencia sobre la importancia de avanzar hacia una transformación curricular articulada, pertinente y sostenible.
Lugar: Aula 822 (FCAPyF)
Hora: 13:30 a 15:30 a.m.'),
    (104, '99c1c948-f5f4-4e39-92a6-fa761f2410a7', '2v4z94f8-9a6c-4d9e-aea7-da7e80bd5c6f', U&'Impulsando procesos de innovación educativa en carreras y programas de la UMSS.
Facultad de Ciencias y Tecnología
En el marco del “Año de la Innovación Curricular”, la Universidad Mayor de San Simón (UMSS), a través de la Dirección de Planificación Académica (DPA )y el Departamento de Desarrollo Curricular (DDC), continúa desarrollando talleres dirigidos a autoridades, docentes y representantes estudiantiles de sus distintas facultades.
Una apuesta estratégica por la transformación curricular
Este miércoles 02 de julio de 2025, la Facultad de Ciencias y Tecnología será escenario de un taller que busca fortalecer los procesos de innovación educativa en sus programas de formación. El espacio permitirá generar conciencia sobre la importancia de avanzar hacia una transformación curricular articulada, pertinente y sostenible.
Lugar: Palacio de Ciencia y Cultura
Hora: 09:30 a 12:30 a.m.'),
    (105, '4mx8c948-f5f4-4e39-92a6-fa761f2410a7', '7o1b94f8-9a6c-4d9e-aea7-da7e80bd5c6f', U&'Impulsando procesos de innovación educativa en carreras y programas de la UMSS.
Facultad de Ciencias Veterinarias
En el marco del “Año de la Innovación Curricular”, la Universidad Mayor de San Simón (UMSS), a través de la Dirección de Planificación Académica (DPA )y el Departamento de Desarrollo Curricular (DDC), continúa desarrollando talleres dirigidos a autoridades, docentes y representantes estudiantiles de sus distintas facultades.
Una apuesta estratégica por la transformación curricular
Este jueves 03 de julio de 2025, la Facultad de Ciencias Veterinarias será escenario de un taller que busca fortalecer los procesos de innovación educativa en sus programas de formación. El espacio permitirá generar conciencia sobre la importancia de avanzar hacia una transformación curricular articulada, pertinente y sostenible.
Lugar: Aula Magna (FCV)
Hora: 09:30 a 12:30 a.m.'),
    (106, '6vk5x297-f5f4-4e39-92a6-fa761f2410a7', '3m8c52v4-9a6c-4d9e-aea7-da7e80bd5c6f', U&'Impulsando procesos de innovación educativa en carreras y programas de la UMSS.
Facultad Politécnica del Valle Alto
En el marco del “Año de la Innovación Curricular”, la Universidad Mayor de San Simón (UMSS), a través de la Dirección de Planificación Académica (DPA) y el Departamento de Desarrollo Curricular (DDC), continúa desarrollando talleres dirigidos a autoridades, docentes y representantes estudiantiles de sus distintas facultades.
Una apuesta estratégica por la transformación curricular
Este viernes 04 de julio de 2025, la Facultad Politécnica del Valle Alto será escenario de un taller que busca fortalecer los procesos de innovación educativa en sus programas de formación. El espacio permitirá generar conciencia sobre la importancia de avanzar hacia una transformación curricular articulada, pertinente y sostenible.
Lugar: Auditorio de la FPVA
Hora: 09:30 a 12:30 a.m.'),
    (107, '2cm9z8n5-f5f4-4e39-92a6-fa761f2410a7', '1om5x7n2-9a6c-4d9e-aea7-da7e80bd5c6f', U&'#INVITACIÓN_OFICIAL
La #DPA y la #DDC tienen el agrado de invitar a las autoridades facultativas, directores(as) de carrera y coordinadores(as) de programa y docentes de la Universidad Mayor de San Simón a las JORNADAS ACADÉMICAS PARA LA INNOVACIÓN CURRICULAR 2025, en cumplimiento de lo establecido en las Circulares DPA 07/2025 y 08/2025.
Este importante espacio formativo tiene el objetivo Generar escenarios de reflexión y discusión académica para impulsar los procesos de innovación curricular en las carreras y programas de formación profesional de grado en el marco del Modelo Educativo de la UMSS.
Fecha de Inicio: 14 de julio al 30 julio
Horario: 8:30 a 12:00
Modalidad: Presencial
Lugar: Señalados por facultades en la circular DPA No. 08/2025');

insert into media (id, uuid, content_id, file_name, number, file_type, file_path) values
	(100, '17ucf8a1-09e0-4435-a850-0613d778897b', '1e5294f8-9a6c-4d9e-aea7-da7e80bd5c6f', 'img01', 1, 'image', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/a968e0d8-2209-4df0-aec1-4a1618d73aa4'),
    (101, '24l5f8a1-09e0-4435-a850-0613d778897b', '6i2494f8-9a6c-4d9e-aea7-da7e80bd5c6f', 'img03', 1, 'image', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/34c1bc99-453c-443c-8f2a-ca1f15650a71'),
    (102, '79a3f8a1-09e0-4435-a850-0613d778897b', '8f3194f8-9a6c-4d9e-aea7-da7e80bd5c6f', 'img04', 1, 'image', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/73228194-1485-496f-827d-7c38b62c5c7e'),
    (103, '38j1f8a1-09e0-4435-a850-0613d778897b', '5l6n94f8-9a6c-4d9e-aea7-da7e80bd5c6f', 'img05', 1, 'image', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/fbbca054-34cd-4ebe-9bfe-3b22be8bb836'),
    (104, '2k9zf8a1-09e0-4435-a850-0613d778897b', '2v4z94f8-9a6c-4d9e-aea7-da7e80bd5c6f', 'img06', 1, 'image', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/745a495d-4563-41a1-8733-f089b3b7b21d'),
    (105, '6m1vf8a1-09e0-4435-a850-0613d778897b', '7o1b94f8-9a6c-4d9e-aea7-da7e80bd5c6f', 'img07', 1, 'image', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/3b900a66-2021-49e1-9152-c6d432d1aa52'),
    (106, '17b9m4z3-09e0-4435-a850-0613d778897b', '3m8c52v4-9a6c-4d9e-aea7-da7e80bd5c6f', 'img08', 1, 'image', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/ddc5a29a-c607-44db-95f2-c66f1da21fde'),
    (107, '5jc8x7v4-09e0-4435-a850-0613d778897b', '1om5x7n2-9a6c-4d9e-aea7-da7e80bd5c6f', 'img09', 1, 'image', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/27897c0a-2503-4536-83ed-a65a596646fb');

insert into post (id, uuid, institution_id, user_id, comment_config_id, content_id, post_date, post_type) values
	(100, '5f9ab4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49','875d7d7f-7a1c-4b77-ab63-77a9f76759d0', '1e5294f8-9a6c-4d9e-aea7-da7e80bd5c6f', '2025-02-18T07:53:22', 'GENERAL'),
    (101, '7h3ab4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '875d7d7f-7a1c-4b77-ab63-77a9f76759d0', '6i2494f8-9a6c-4d9e-aea7-da7e80bd5c6f', '2025-03-05T19:31:22', 'GENERAL'),
    (102, '2k9db4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '587d7d7f-5g3n-4b77-cf98-77a9h46759d0', '8f3194f8-9a6c-4d9e-aea7-da7e80bd5c6f', '2025-03-11T08:00:22', 'GENERAL'),
    (103, '8s2ib4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '875d7d7f-7a1c-4b77-ab63-77a9f76759d0', '5l6n94f8-9a6c-4d9e-aea7-da7e80bd5c6f', '2025-07-02T08:00:22', 'GENERAL'),
    (104, '3g9ab4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '875d7d7f-7a1c-4b77-ab63-77a9f76759d0', '2v4z94f8-9a6c-4d9e-aea7-da7e80bd5c6f', '2025-07-02T10:25:22', 'CUDIE'),
    (105, '5n1ib4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '875d7d7f-7a1c-4b77-ab63-77a9f76759d0', '7o1b94f8-9a6c-4d9e-aea7-da7e80bd5c6f', '2025-07-03T08:00:12', 'PROYECTOS'),
    (106, '9v4mb4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '492d7d7f-9f4s-8g45-hy34-77a9h46759d0', '3m8c52v4-9a6c-4d9e-aea7-da7e80bd5c6f', '2025-07-04T19:30:26', 'BECAS'),
    (107, '1ib6k3c5-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '875d7d7f-7a1c-4b77-ab63-77a9f76759d0', '1om5x7n2-9a6c-4d9e-aea7-da7e80bd5c6f', '2025-07-14T10:51:18', 'CONVENIOS');

insert into emoji_type (id, uuid, emoji_name, emoji_code) values
	(100, '3f696a78-c73f-475c-80a6-f5a858648af1', 'thumbs-up', U&'\+01F44D'),
    (101, '7v236a78-c73f-475c-80a6-f5a858648af1', 'red-heart', U&'\+002764\+00FE0F'),
    (102, 'n1596a78-c73f-475c-80a6-f5a858648af1', 'crying-face', U&'\+01F622'),
    (103, '4c806a78-c73f-475c-80a6-f5a858648af1', 'angry-face', U&'\+01F620'),
    (104, 'l6m3bd82-c73f-475c-80a6-f5a858648af1', 'grinning-squinting-face', U&'\+01F606'),
    (105, 'c5n1m4f0-c73f-475c-80a6-f5a858648af1', 'astonished-face', U&'\+01F632');

insert into post_reaction (id, uuid, user_id, post_id, emoji_type_id, reaction_date) values
	(100, 'c31d5d56-b6f5-41a4-97d2-e797a6b0aa0e', '9bae1fe0-2c56-4091-883d-15458e051500', '5f9ab4e8-0856-4aad-b3aa-747e2dba76d9', '3f696a78-c73f-475c-80a6-f5a858648af1', '2024-12-02T15:23:22'),
    (101, 'e4l98068-4880-4055-987c-087f1b1f6635', '844b324d-2f5e-41c9-b726-0149eeb01157', '5f9ab4e8-0856-4aad-b3aa-747e2dba76d9', '7v236a78-c73f-475c-80a6-f5a858648af1', '2024-12-03T16:50:22'),
    (102, 'i7258068-4880-4055-987c-087f1b1f6635', 'ae542968-1335-425e-a206-283c38a20190', '5f9ab4e8-0856-4aad-b3aa-747e2dba76d9', '3f696a78-c73f-475c-80a6-f5a858648af1', '2024-12-03T12:10:22'),
    (103, 'm2c8b5z3-4880-4055-987c-087f1b1f6635', '844b324d-2f5e-41c9-b726-0149eeb01157', '7h3ab4e8-0856-4aad-b3aa-747e2dba76d9', '3f696a78-c73f-475c-80a6-f5a858648af1', '2024-12-03T12:14:22'),
    (104, 'c0m2v6h3-4880-4055-987c-087f1b1f6635', '2e121a51-24b5-4aad-92c2-150997ec4266', '7h3ab4e8-0856-4aad-b3aa-747e2dba76d9', '7v236a78-c73f-475c-80a6-f5a858648af1', '2024-12-03T18:14:22'),
    (105, 'l7s0j6v4-4880-4055-987c-087f1b1f6635', '7f9264d7-ca8d-41ca-be1f-d24c9dd244a2', '7h3ab4e8-0856-4aad-b3aa-747e2dba76d9', 'l6m3bd82-c73f-475c-80a6-f5a858648af1', '2024-12-04T20:14:22'),
    (106, 'd8j5l1x7-4880-4055-987c-087f1b1f6635', 'e54bd4dc-d8f6-42e9-8e94-5d56bf42416f', '7h3ab4e8-0856-4aad-b3aa-747e2dba76d9', '7v236a78-c73f-475c-80a6-f5a858648af1', '2024-12-05T17:14:22'),
    (107, 'a9k2n5d6-4880-4055-987c-087f1b1f6635', 'ae542968-1335-425e-a206-283c38a20190', '2k9db4e8-0856-4aad-b3aa-747e2dba76d9', '7v236a78-c73f-475c-80a6-f5a858648af1', '2024-12-03T12:14:22'),
    (108, 'c9m3aiv2-4880-4055-987c-087f1b1f6635', 'f8d1c969-af76-4161-b0d7-9c2dfc47e75c', '2k9db4e8-0856-4aad-b3aa-747e2dba76d9', '3f696a78-c73f-475c-80a6-f5a858648af1', '2024-12-04T15:14:22'),
    (109, 's7l1v5m3-4880-4055-987c-087f1b1f6635', '9bae1fe0-2c56-4091-883d-15458e051500', '2k9db4e8-0856-4aad-b3aa-747e2dba76d9', '3f696a78-c73f-475c-80a6-f5a858648af1', '2024-12-04T18:14:22'),
    (110, 'a0p1g7m2-4880-4055-987c-087f1b1f6635', 'f8d1c969-af76-4161-b0d7-9c2dfc47e75c', '8s2ib4e8-0856-4aad-b3aa-747e2dba76d9', '3f696a78-c73f-475c-80a6-f5a858648af1', '2024-12-04T13:05:22'),
    (111, 'c5m1p6s9-4880-4055-987c-087f1b1f6635', '7f9264d7-ca8d-41ca-be1f-d24c9dd244a2', '8s2ib4e8-0856-4aad-b3aa-747e2dba76d9', '3f696a78-c73f-475c-80a6-f5a858648af1', '2024-12-04T15:12:22'),
    (112, 'z9m6n2a0-4880-4055-987c-087f1b1f6635', '7f9264d7-ca8d-41ca-be1f-d24c9dd244a2', '3g9ab4e8-0856-4aad-b3aa-747e2dba76d9', '3f696a78-c73f-475c-80a6-f5a858648af1', '2024-12-04T17:01:22'),
    (113, 'k6x9l1s2-4880-4055-987c-087f1b1f6635', 'e54bd4dc-d8f6-42e9-8e94-5d56bf42416f', '3g9ab4e8-0856-4aad-b3aa-747e2dba76d9', '7v236a78-c73f-475c-80a6-f5a858648af1', '2024-12-05T21:16:22'),
    (114, 's1k4z6l5-4880-4055-987c-087f1b1f6635', '2e121a51-24b5-4aad-92c2-150997ec4266', '3g9ab4e8-0856-4aad-b3aa-747e2dba76d9', '7v236a78-c73f-475c-80a6-f5a858648af1', '2024-12-05T23:12:22'),
    (115, 'l5j6m3b8-4880-4055-987c-087f1b1f6635', 'ab6e1a7f-4494-4251-8177-dc5c4fe18740', '5n1ib4e8-0856-4aad-b3aa-747e2dba76d9', '7v236a78-c73f-475c-80a6-f5a858648af1', '2024-12-05T11:01:22'),
    (116, 'z9b9h4b1-4880-4055-987c-087f1b1f6635', 'c332c4ff-49e8-4e0e-a7f4-d59907d8cda3', '5n1ib4e8-0856-4aad-b3aa-747e2dba76d9', '7v236a78-c73f-475c-80a6-f5a858648af1', '2024-12-05T16:08:22'),
    (117, 'm6n6d9g5-4880-4055-987c-087f1b1f6635', '9bae1fe0-2c56-4091-883d-15458e051500', '9v4mb4e8-0856-4aad-b3aa-747e2dba76d9', '7v236a78-c73f-475c-80a6-f5a858648af1', '2024-12-05T20:01:22'),
    (118, 's8i5b2a2-4880-4055-987c-087f1b1f6635', 'c332c4ff-49e8-4e0e-a7f4-d59907d8cda3', '9v4mb4e8-0856-4aad-b3aa-747e2dba76d9', 'c5n1m4f0-c73f-475c-80a6-f5a858648af1', '2024-12-05T20:08:22'),
    (119, 'x4n2v7s9-4880-4055-987c-087f1b1f6635', '9f32985a-f108-4b19-9bda-cab7c501ae68', '9v4mb4e8-0856-4aad-b3aa-747e2dba76d9', '3f696a78-c73f-475c-80a6-f5a858648af1', '2024-12-05T21:10:22'),
    (120, 'v5m3b8f1-4880-4055-987c-087f1b1f6635', 'ab6e1a7f-4494-4251-8177-dc5c4fe18740', '9v4mb4e8-0856-4aad-b3aa-747e2dba76d9', '7v236a78-c73f-475c-80a6-f5a858648af1', '2024-12-05T21:16:22'),
    (121, 'a7v5n2l4-4880-4055-987c-087f1b1f6635', 'f8d1c969-af76-4161-b0d7-9c2dfc47e75c', '9v4mb4e8-0856-4aad-b3aa-747e2dba76d9', '3f696a78-c73f-475c-80a6-f5a858648af1', '2024-12-05T22:06:22'),
    (122, 'n4d9j9i6-4880-4055-987c-087f1b1f6635', 'e54bd4dc-d8f6-42e9-8e94-5d56bf42416f', '9v4mb4e8-0856-4aad-b3aa-747e2dba76d9', 'n1596a78-c73f-475c-80a6-f5a858648af1', '2024-12-05T22:28:22'),
    (123, 'g8m3b0s5-4880-4055-987c-087f1b1f6635', 'c332c4ff-49e8-4e0e-a7f4-d59907d8cda3', '1ib6k3c5-0856-4aad-b3aa-747e2dba76d9', '7v236a78-c73f-475c-80a6-f5a858648af1', '2024-12-05T20:01:22'),
    (124, 'i4n8x2n1-4880-4055-987c-087f1b1f6635', '9f32985a-f108-4b19-9bda-cab7c501ae68', '1ib6k3c5-0856-4aad-b3aa-747e2dba76d9', '7v236a78-c73f-475c-80a6-f5a858648af1', '2024-12-05T20:08:22'),
    (125, 'v0n0s8f4-4880-4055-987c-087f1b1f6635', 'ab6e1a7f-4494-4251-8177-dc5c4fe18740', '1ib6k3c5-0856-4aad-b3aa-747e2dba76d9', '3f696a78-c73f-475c-80a6-f5a858648af1', '2024-12-05T21:10:22'),
    (126, 'f0m2s8h2-4880-4055-987c-087f1b1f6635', 'e54bd4dc-d8f6-42e9-8e94-5d56bf42416f', '1ib6k3c5-0856-4aad-b3aa-747e2dba76d9', 'l6m3bd82-c73f-475c-80a6-f5a858648af1', '2024-12-05T21:16:22'),
    (127, 'h8b4h1j3-4880-4055-987c-087f1b1f6635', 'ae542968-1335-425e-a206-283c38a20190', '1ib6k3c5-0856-4aad-b3aa-747e2dba76d9', '3f696a78-c73f-475c-80a6-f5a858648af1', '2024-12-05T22:06:22'),
    (128, 'w8g8n5s0-4880-4055-987c-087f1b1f6635', 'eab72365-d8c1-45df-9b48-274f64c65b86', '1ib6k3c5-0856-4aad-b3aa-747e2dba76d9', '3f696a78-c73f-475c-80a6-f5a858648af1', '2024-12-05T22:28:22'),
    (129, 's1s3k5n9-4880-4055-987c-087f1b1f6635', 'd33ded75-c9f3-4fef-9762-0ba4a905efa8', '1ib6k3c5-0856-4aad-b3aa-747e2dba76d9', 'c5n1m4f0-c73f-475c-80a6-f5a858648af1', '2024-12-05T22:06:22'),
    (130, 'a5a9m3d5-4880-4055-987c-087f1b1f6635', 'f8d1c969-af76-4161-b0d7-9c2dfc47e75c', '1ib6k3c5-0856-4aad-b3aa-747e2dba76d9', 'c5n1m4f0-c73f-475c-80a6-f5a858648af1', '2024-12-05T22:28:22');

insert into social_network (id, uuid, institution_id, name, link) values
	(100, 'm2c5z1l3-4880-4055-987c-087f1b1f6635', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'Facebook', 'https://www.facebook.com/UMSS.DRIC');

insert into groups (id, uuid, name, status) values
	(100, '5ad28ac2-bd48-4fc5-8be0-92cd24448708', 'Fijados', 'CREATED'),
    (101, 'd7cc6017-1a74-40e5-9d9c-41d4d271259b', 'Destacados', 'CREATED');

insert into post_group (group_id, post_id) values
	('5ad28ac2-bd48-4fc5-8be0-92cd24448708', '2k9db4e8-0856-4aad-b3aa-747e2dba76d9'),
    ('5ad28ac2-bd48-4fc5-8be0-92cd24448708', '1ib6k3c5-0856-4aad-b3aa-747e2dba76d9'),
    ('d7cc6017-1a74-40e5-9d9c-41d4d271259b', '8s2ib4e8-0856-4aad-b3aa-747e2dba76d9'),
    ('d7cc6017-1a74-40e5-9d9c-41d4d271259b', '5f9ab4e8-0856-4aad-b3aa-747e2dba76d9'),
    ('d7cc6017-1a74-40e5-9d9c-41d4d271259b', '5n1ib4e8-0856-4aad-b3aa-747e2dba76d9');

insert into image_file (id, uuid, name, url_resource, status, type) values
	(100, '729b9472-e531-4362-88cb-efd9fc656f78', 'img01', 'https://devpws.cs.umss.edu.bo/api/v1/images/users/729b9472-e531-4362-88cb-efd9fc656f78', 'SAVED_SUCCESSFULLY','image/png'),
    (101, 'e867fa32-75b7-4c3c-8943-4a2808c10ac0', 'img02', 'https://devpws.cs.umss.edu.bo/api/v1/images/users/e867fa32-75b7-4c3c-8943-4a2808c10ac0', 'SAVED_SUCCESSFULLY','image/png'),
    (102, '733b01ba-198e-42db-b9ca-d7ace9a7abad', 'img03', 'https://devpws.cs.umss.edu.bo/api/v1/images/users/733b01ba-198e-42db-b9ca-d7ace9a7abad', 'SAVED_SUCCESSFULLY','image/png'),
    (103, '06b8fbe5-9d91-422b-bd20-6d89d23e5608', 'img04', 'https://devpws.cs.umss.edu.bo/api/v1/images/users/06b8fbe5-9d91-422b-bd20-6d89d23e5608', 'SAVED_SUCCESSFULLY','image/png'),
    (104, 'b5de2b13-4720-41e7-87d2-d31abd590fac', 'img05', 'https://devpws.cs.umss.edu.bo/api/v1/images/users/b5de2b13-4720-41e7-87d2-d31abd590fac', 'SAVED_SUCCESSFULLY','image/png'),
    (105, '5e9f9c0d-4c31-4335-a11e-b9c7f6f8a2b1', 'img06', 'https://devpws.cs.umss.edu.bo/api/v1/images/users/5e9f9c0d-4c31-4335-a11e-b9c7f6f8a2b1', 'SAVED_SUCCESSFULLY','image/png'),
    (106, '1bed8052-ba51-41c2-88b5-e39e78dbf420', 'img07', 'https://devpws.cs.umss.edu.bo/api/v1/images/users/1bed8052-ba51-41c2-88b5-e39e78dbf420', 'SAVED_SUCCESSFULLY','image/png'),
    (107, '28e3d103-76e8-4b0d-b04c-cf2e8821cf6e', 'img08', 'https://devpws.cs.umss.edu.bo/api/v1/images/users/28e3d103-76e8-4b0d-b04c-cf2e8821cf6e', 'SAVED_SUCCESSFULLY','image/png'),
    (108, 'f9c89556-cd27-481a-a8bd-59f4915f7dfa', 'img09', 'https://devpws.cs.umss.edu.bo/api/v1/images/users/f9c89556-cd27-481a-a8bd-59f4915f7dfa', 'SAVED_SUCCESSFULLY','image/png'),
    (109, 'b3a64fc9-3d28-4b92-b296-2324f9c4e061', 'img10', 'https://devpws.cs.umss.edu.bo/api/v1/images/users/b3a64fc9-3d28-4b92-b296-2324f9c4e061', 'SAVED_SUCCESSFULLY','image/png'),
    (110, 'e326bdb8-6eee-4814-a877-9e063183f410', 'img11', 'https://devpws.cs.umss.edu.bo/api/v1/images/users/e326bdb8-6eee-4814-a877-9e063183f410', 'SAVED_SUCCESSFULLY','image/png'),
    (111, '004fca51-d313-41c4-94ff-5908ddb58dc2', 'img12', 'https://devpws.cs.umss.edu.bo/api/v1/images/users/004fca51-d313-41c4-94ff-5908ddb58dc2', 'SAVED_SUCCESSFULLY','image/png'),
    (112, '2148f392-4f16-40b6-88e7-bd0ca32bd84a', 'img13', 'https://devpws.cs.umss.edu.bo/api/v1/images/users/2148f392-4f16-40b6-88e7-bd0ca32bd84a', 'SAVED_SUCCESSFULLY','image/png'),
    (113, 'f1309a27-93eb-4429-a096-d786f8d16f5c', 'img14', 'https://devpws.cs.umss.edu.bo/api/v1/images/inst-profile/f1309a27-93eb-4429-a096-d786f8d16f5c', 'SAVED_SUCCESSFULLY','image/png'),
    (114, '14bacba4-b962-40b5-9dd1-d5bf8e1e86f8', 'img15', 'https://devpws.cs.umss.edu.bo/api/v1/images/inst-cover/14bacba4-b962-40b5-9dd1-d5bf8e1e86f8', 'SAVED_SUCCESSFULLY','image/png'),
    (115, 'a968e0d8-2209-4df0-aec1-4a1618d73aa4', 'img16', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/a968e0d8-2209-4df0-aec1-4a1618d73aa4', 'SAVED_SUCCESSFULLY','image/jpeg'),
    (116, 'cbca5013-5556-4f35-b945-6a7de7083659', 'vid02', 'https://devpws.cs.umss.edu.bo/api/v1/videos/posts/cbca5013-5556-4f35-b945-6a7de7083659', 'SAVED_SUCCESSFULLY','video/mp4'),
    (117, '34c1bc99-453c-443c-8f2a-ca1f15650a71', 'img18', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/34c1bc99-453c-443c-8f2a-ca1f15650a71', 'SAVED_SUCCESSFULLY','image/jpeg'),
    (118, '73228194-1485-496f-827d-7c38b62c5c7e', 'img19', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/73228194-1485-496f-827d-7c38b62c5c7e', 'SAVED_SUCCESSFULLY','image/jpeg'),
    (119, 'fbbca054-34cd-4ebe-9bfe-3b22be8bb836', 'img20', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/fbbca054-34cd-4ebe-9bfe-3b22be8bb836', 'SAVED_SUCCESSFULLY','image/jpeg'),
    (120, '745a495d-4563-41a1-8733-f089b3b7b21d', 'img21', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/745a495d-4563-41a1-8733-f089b3b7b21d', 'SAVED_SUCCESSFULLY','image/jpeg'),
    (121, '3b900a66-2021-49e1-9152-c6d432d1aa52', 'img22', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/3b900a66-2021-49e1-9152-c6d432d1aa52', 'SAVED_SUCCESSFULLY','image/jpeg'),
    (122, 'ddc5a29a-c607-44db-95f2-c66f1da21fde', 'img23', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/ddc5a29a-c607-44db-95f2-c66f1da21fde', 'SAVED_SUCCESSFULLY','image/jpeg'),
    (123, '27897c0a-2503-4536-83ed-a65a596646fb', 'img24', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/27897c0a-2503-4536-83ed-a65a596646fb', 'SAVED_SUCCESSFULLY','image/jpeg'),
    (124, 'aa74ec25-6e70-4776-899c-7465c61de5bd', 'vid01', 'https://devpws.cs.umss.edu.bo/api/v1/videos/posts/aa74ec25-6e70-4776-899c-7465c61de5bd', 'SAVED_SUCCESSFULLY','video/mp4'),
    (125, '6cce68f8-76ef-4639-9828-9e5e6e819fc4', 'img26', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/6cce68f8-76ef-4639-9828-9e5e6e819fc4', 'SAVED_SUCCESSFULLY','image/jpeg'),
    (126, 'c401fbaa-7f94-41ad-a7ef-f677d2d30f3d', 'img27', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/c401fbaa-7f94-41ad-a7ef-f677d2d30f3d', 'SAVED_SUCCESSFULLY','image/jpeg'),
    (127, '526e6a41-f1cd-42e1-ab6c-e32ccbfdddb6', 'img28', 'https://devpws.cs.umss.edu.bo/api/v1/videos/posts/526e6a41-f1cd-42e1-ab6c-e32ccbfdddb6', 'SAVED_SUCCESSFULLY','video/mp4'),
    (128, 'e7629149-79db-4bc6-85c9-12fa52d35c34', 'img29', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/e7629149-79db-4bc6-85c9-12fa52d35c34', 'SAVED_SUCCESSFULLY','image/jpeg'),
    (129, 'a5a0a99c-6280-4b74-a507-12a5a2109cd5', 'img30', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/a5a0a99c-6280-4b74-a507-12a5a2109cd5', 'SAVED_SUCCESSFULLY','image/jpeg'),
    (130, 'af87625e-3075-41a5-901a-cf950330b237', 'img31', 'https://devpws.cs.umss.edu.bo/api/v1/images/users/af87625e-3075-41a5-901a-cf950330b237', 'SAVED_SUCCESSFULLY','image/png'),
    (131, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'img32', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'SAVED_SUCCESSFULLY','image/jpg'),
    (132, 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'img33', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/f47ac10b-58cc-4372-a567-0e02b2c3d479', 'SAVED_SUCCESSFULLY','image/jpg'),
    (133, '5d9a4559-7c13-4db6-8ea3-8f32a3f5c8e2', 'img34', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/5d9a4559-7c13-4db6-8ea3-8f32a3f5c8e2', 'SAVED_SUCCESSFULLY','image/jpg'),
    (134, '8f6e9b4a-1d4d-4a4c-9c2a-3e7f8d5c6b1e', 'img35', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/8f6e9b4a-1d4d-4a4c-9c2a-3e7f8d5c6b1e', 'SAVED_SUCCESSFULLY','image/jpg'),
    (135, '1b9d6bcd-bbfd-4b2d-9b5d-ab8dfbbd4bed', 'img36', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/1b9d6bcd-bbfd-4b2d-9b5d-ab8dfbbd4bed', 'SAVED_SUCCESSFULLY','image/jpg');

insert into video_file (id, uuid, name, url_resource, status, type) values
	(100, '200504be-c220-4932-9810-126fe8590a9c', 'video01', 'https://devpws.cs.umss.edu.bo/api/v1/videos/posts/200504be-c220-4932-9810-126fe8590a9c', 'SAVED_SUCCESSFULLY', 'video/mp4'),
    (101, 'aa74ec25-6e70-4776-899c-7465c61de5bd', 'video02', 'https://devpws.cs.umss.edu.bo/api/v1/videos/posts/aa74ec25-6e70-4776-899c-7465c61de5bd', 'SAVED_SUCCESSFULLY', 'video/mp4'),
    (102, 'cbca5013-5556-4f35-b945-6a7de7083659', 'video03', 'https://devpws.cs.umss.edu.bo/api/v1/videos/posts/cbca5013-5556-4f35-b945-6a7de7083659', 'SAVED_SUCCESSFULLY', 'video/mp4');

INSERT INTO comment (id, uuid, user_id, post_id, content, moderated, state, comment_date) VALUES
	(10,'d3b07384-d9a0-4c4a-8d9d-0c9a9f8b9c9c', 113, 101, 'Buenosimoooo..felicidades, una consulta... ya esta a la venta el helafoool', false, 'VISIBLE', '2024-12-02T21:48:22'),
    (11,'m4c9b0x2-d9a0-4c4a-8d9d-0c9a9f8b9c9c', 118, 101, 'Muy Bien...', false, 'VISIBLE', '2024-12-03T08:27:22'),
    (12,'z9n3d8j1-d9a0-4c4a-8d9d-0c9a9f8b9c9c', 120, 101, 'Muchas felicidades Daniela Laura una investigaci\00F3n que aporta al desarrollo de nuestro pa\00EDs, sigue por nuevos desaf\00EDos.. bendiciones.!!\+01F4AF', false, 'VISIBLE', '2024-12-03T10:07:22'),
    (13,'f8k1b6s9-d9a0-4c4a-8d9d-0c9a9f8b9c9c', 119, 102, 'Podrian grabar la entrevista por favor?', false, 'VISIBLE', '2024-12-03T10:37:22'),
    (14,'v9s7h5k2-d9a0-4c4a-8d9d-0c9a9f8b9c9c', 117, 103, 'Por que plataformas van a transmitir?', false, 'VISIBLE', '2024-12-04T11:49:22'),
    (15,'l4m6v9a0-d9a0-4c4a-8d9d-0c9a9f8b9c9c', 110, 104, 'Buena entrevista!', false, 'VISIBLE', '2024-12-04T12:25:22'),
    (16,'m5d8f8b1-d9a0-4c4a-8d9d-0c9a9f8b9c9c', 116, 105, 'Graben la entrevista por favor.', false, 'VISIBLE', '2024-12-05T15:38:12'),
    (17,'d9m2f2i5-d9a0-4c4a-8d9d-0c9a9f8b9c9c', 113, 106, 'Muy Bueno', true, 'PENDING_APPROVAL', '2024-12-05T21:30:26'),
    (18,'i4v8m1a9-d9a0-4c4a-8d9d-0c9a9f8b9c9c', 114, 106, 'Excelente charla!', true, 'PENDING_APPROVAL', '2024-12-06T08:24:26'),
    (19,'n5d9b5a8-d9a0-4c4a-8d9d-0c9a9f8b9c9c', 117, 107, 'Hasta que fecha se puede postular?', false, 'VISIBLE', '2024-12-10T13:42:18');

insert into comment_reaction (id, uuid, user_id, comment_id, emoji_type_id, reaction_date) values
	(100, '07063785-4327-49ff-86b7-2bf1e9596290', '9bae1fe0-2c56-4091-883d-15458e051500', 'd3b07384-d9a0-4c4a-8d9d-0c9a9f8b9c9c', '7v236a78-c73f-475c-80a6-f5a858648af1', '2024-12-03T15:23:22'),
    (101, 'bce50d37-3ada-45ac-a556-a17cc33464d9', '9bae1fe0-2c56-4091-883d-15458e051500', 'z9n3d8j1-d9a0-4c4a-8d9d-0c9a9f8b9c9c', '7v236a78-c73f-475c-80a6-f5a858648af1', '2024-12-03T16:20:22'),
    (102, 'l5m6v9n2-3ada-45ac-a556-a17cc33464d9', '9f32985a-f108-4b19-9bda-cab7c501ae68', 'm5d8f8b1-d9a0-4c4a-8d9d-0c9a9f8b9c9c', '3f696a78-c73f-475c-80a6-f5a858648af1', '2024-12-05T12:05:22'),
    (103, 'k4b8c4m6-3ada-45ac-a556-a17cc33464d9', 'e54bd4dc-d8f6-42e9-8e94-5d56bf42416f', 'm5d8f8b1-d9a0-4c4a-8d9d-0c9a9f8b9c9c', '3f696a78-c73f-475c-80a6-f5a858648af1', '2024-12-06T17:02:22'),
    (104, 'a9n4d8n1-3ada-45ac-a556-a17cc33464d9', '9bae1fe0-2c56-4091-883d-15458e051500', 'f8k1b6s9-d9a0-4c4a-8d9d-0c9a9f8b9c9c', '7v236a78-c73f-475c-80a6-f5a858648af1', '2024-12-06T18:20:22'),
    (105, 'v8n4m2d0-3ada-45ac-a556-a17cc33464d9', 'f8d1c969-af76-4161-b0d7-9c2dfc47e75c', 'd9m2f2i5-d9a0-4c4a-8d9d-0c9a9f8b9c9c', '7v236a78-c73f-475c-80a6-f5a858648af1', '2024-12-06T15:04:22'),
    (106, 'k5d9g5j1-3ada-45ac-a556-a17cc33464d9', 'd33ded75-c9f3-4fef-9762-0ba4a905efa8', 'i4v8m1a9-d9a0-4c4a-8d9d-0c9a9f8b9c9c', '7v236a78-c73f-475c-80a6-f5a858648af1', '2024-12-06T17:04:22');

INSERT INTO replies (reply_id, uuid, user_id, comment_id, content, created_at) VALUES
	(20,'f8d2a52b-c1dc-4919-8610-76057df842c0', 113,13, 'X2, alguien que grabe porfavor', NOW()),
    (30,'b0315608-e21b-4096-8cb9-3cf3b518892b', 114,14, 'Van a transmitir en vivo por facebook', NOW()),
    (40,'k5s9n2s6-c1dc-4919-8610-76057df842c0', 116,19, 'X2', NOW()),
    (41,'l2m6b9x3-e21b-4096-8cb9-3cf3b518892b', 118,18, 'Si me encanto igual', NOW()),
    (42,'v83k5n2s-c1dc-4919-8610-76057df842c0', 115,16, 'Yo voy a grabar, inbox para pasarte el link', NOW()),
    (43,'a9l2n6m3-e21b-4096-8cb9-3cf3b518892b', 117,19, 'Si X3', NOW()),
    (44,'d9j5n3c9-c1dc-4919-8610-76057df842c0', 112,14, 'Solo face dijeron xd', NOW());

INSERT INTO reply_reactions (id, uuid, user_id, reply_id, emoji_type_id, reaction_date) VALUES
	(10,'819d23be-25c9-49c8-9e8a-d5070bbf57ef', 119,20,100, NOW()),
    (11,'ce87a375-8fe4-4c98-a86d-2d02e0d6d6d7', 117,30,101, NOW()),
    (12,'l4m9sd83-8fe4-4c98-a86d-2d02e0d6d6d7', 115,40,104, NOW()),
    (13,'v9m3a8c0-8fe4-4c98-a86d-2d02e0d6d6d7', 120,41,100, NOW()),
    (14,'f3n5d9k2-8fe4-4c98-a86d-2d02e0d6d6d7', 118,41,100, NOW()),
    (15,'h5j23k69-8fe4-4c98-a86d-2d02e0d6d6d7', 113,43,100, NOW()),
    (16,'z5m2c5v6-8fe4-4c98-a86d-2d02e0d6d6d7', 117,44,105, NOW()),
    (17,'k3n6b7m2-8fe4-4c98-a86d-2d02e0d6d6d7', 118,41,104, NOW()),
    (18,'a9j9g5k1-8fe4-4c98-a86d-2d02e0d6d6d7', 113,43,100, NOW()),
    (19,'m3b7n3x2-8fe4-4c98-a86d-2d02e0d6d6d7', 112,44,105, NOW()),
    (20,'e4y9i1p2-8fe4-4c98-a86d-2d02e0d6d6d7', 118,41,100, NOW()),
    (21,'w9u3n6r4-8fe4-4c98-a86d-2d02e0d6d6d7', 114,43,101, NOW()),
    (22,'q9k2n4m7-8fe4-4c98-a86d-2d02e0d6d6d7', 115,44,105, NOW());

INSERT INTO followers (id, user_id, institution_id, followed_since) values
	(1, 110, 10, NOW()),
	(2, 112, 10, NOW()),
	(3, 111, 10, NOW()),
	(4, 113, 10, NOW()),
	(5, 114, 10, NOW()),
	(6, 115, 10, NOW()),
	(7, 116, 10, NOW()),
	(8, 117, 10, NOW()),
	(9, 118, 10, NOW()),
	(10, 119, 10, NOW()),
	(11, 120, 10, NOW());

-- Actualizar la secuencia para la tabla followers
SELECT setval('followers_id_seq', (SELECT MAX(id) FROM followers));

-- Añadir permisos a los roles
INSERT INTO permissions (name_permission) VALUES
	('CREATE_POST'),('DELETE_POST'),('GROUP_POST'),('UNGROUP_POST'),('UPDATE_POST'),
	('VIEW_GROUP'),('VIEW_ALL_GROUPS'),('CREATE_GROUP'),
	('VIEW_COMMENT_CONFIG'),('VIEW_ALL_COMMENT_CONFIGS'),('CREATE_COMMENT_CONFIG'),
	('CREATE_COMMENT_REACTION'),('DELETE_COMMENT_REACTION'),
	('CREATE_EMOJI_TYPE'),
	('CREATE_POST_REACTION'),('DELETE_POST_REACTION'),
	('CREATE_REPLY_REACTION'),('DELETE_REPLY_REACTION'),
	('CREATE_REPLY'),('DELETE_REPLY'),
	('CREATE_SOCIAL_NETWORK'),
	('ADD_COMMENT'),('DELETE_COMMENT'),('VIEW_MODERATED_COMMENTS'),('VIEW_REJECTED_COMMENTS'),('APPROVE_COMMENT'),('REJECT_COMMENT'),('DELETE_MODERATED_COMMENT'),('VIEW_DELETED_COMMENTS'),
	('FOLLOW_INSTITUTION'),('UNFOLLOW_INSTITUTION'),('CHECK_FOLLOWING_STATUS'),
	('VIEW_ALL_INSTITUTIONS'),('CREATE_INSTITUTION'),('UPDATE_INSTITUTION'),('DELETE_INSTITUTION'),
	('UPLOAD_DOCUMENT'),('DELETE_DOCUMENT'),
	('UPLOAD_POST_IMAGE'),('UPLOAD_INST_PROFILE_IMAGE'),('UPLOAD_INST_COVER_IMAGE'),('UPLOAD_USER_PROFILE_IMAGE'),('DELETE_POST_IMAGE'),
	('UPLOAD_VIDEO'),('DELETE_VIDEO');

-- Asignar permisos al rol de STUDENT
INSERT INTO rol_permissions (role_id, permission_id)
SELECT r.id_role, p.id_permission FROM role r, permissions p
WHERE r.name = 'STUDENT'
AND p.name_permission IN ('CREATE_COMMENT_REACTION','DELETE_COMMENT_REACTION',
                         'CREATE_POST_REACTION','DELETE_POST_REACTION',
                         'CREATE_REPLY_REACTION', 'DELETE_REPLY_REACTION',
                         'CREATE_REPLY','DELETE_REPLY',
                         'ADD_COMMENT','DELETE_COMMENT',
                         'FOLLOW_INSTITUTION','UNFOLLOW_INSTITUTION','CHECK_FOLLOWING_STATUS');

-- Asignar permisos a ADMIN
INSERT INTO rol_permissions (role_id, permission_id)
SELECT r.id_role, p.id_permission FROM role r, permissions p
WHERE r.name = 'ADMIN';

-- Asignar permisos a ADMIN CONVENIOS
INSERT INTO rol_permissions (role_id, permission_id)
SELECT r.id_role, p.id_permission FROM role r, permissions p
WHERE r.name = 'ADMIN_CONVENIOS';

-- Asignar permisos a ADMIN PROYECTOS
INSERT INTO rol_permissions (role_id, permission_id)
SELECT r.id_role, p.id_permission FROM role r, permissions p
WHERE r.name = 'ADMIN_PROYECTOS';

-- Asignar permisos a ADMIN BECAS
INSERT INTO rol_permissions (role_id, permission_id)
SELECT r.id_role, p.id_permission FROM role r, permissions p
WHERE r.name = 'ADMIN_BECAS';

-- Asignar permisos a ADMIN CUDIE
INSERT INTO rol_permissions (role_id, permission_id)
SELECT r.id_role, p.id_permission FROM role r, permissions p
WHERE r.name = 'ADMIN_CUDIE';

-- Asignar permisos al rol de MODERATOR
INSERT INTO rol_permissions (role_id, permission_id)
SELECT r.id_role, p.id_permission FROM role r, permissions p
WHERE r.name = 'MODERATOR'
AND p.name_permission IN ('VIEW_MODERATED_COMMENTS','VIEW_REJECTED_COMMENTS','APPROVE_COMMENT','REJECT_COMMENT','DELETE_MODERATED_COMMENT','VIEW_DELETED_COMMENTS');

insert into nav_item (id, uuid, user_id, institution_id, label, url, visible, item_order, created_date) values
	(200, 'd1a3f6b2-9f4a-4b2e-8c55-1a2b3c4d5e60', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', U&'Informaci\00F3n', '/informacion', true, 1, '2025-01-01T08:00:00'),
	(201, 'e2b4f7c3-0a5b-4c6d-9e66-2b3c4d5e6f71', 'eab72365-d8c1-45df-9b48-274f64c65b86', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', U&'Gu\00EDa y seguimiento de tr\00E1mites', '/guia-seguimiento-tramites', true, 2, '2025-01-02T09:30:00'),
	(202, 'f3c5g8d4-1b6c-4d7e-0f77-3c4d5e6f7a82', '9bae1fe0-2c56-4091-883d-15458e051500', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'GAIA', '/gaia', true, 3, '2025-01-03T10:45:00'),
	(203, 'a4d6h9e5-2c7d-4e8f-1g88-4d5e6f7a8b93', '844b324d-2f5e-41c9-b726-0149eeb01157', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'Contactos', '/contactos', true, 4, '2025-01-04T11:15:00');

insert into section (id, uuid, institution_id, user_id, nav_item_id, name, date) values
	(100, '919ab4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'd1a3f6b2-9f4a-4b2e-8c55-1a2b3c4d5e60', U&'Presentaci\00F3n', '2024-12-02T07:53:22'),
    (101, '923ab4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'd1a3f6b2-9f4a-4b2e-8c55-1a2b3c4d5e60', U&'Coordinaci\00F3n Acad\00E9mica', '2024-12-02T19:31:22'),
    (102, '939db4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'd1a3f6b2-9f4a-4b2e-8c55-1a2b3c4d5e60', 'Desarrollo Curricular', '2024-12-03T08:00:22'),
    (103, '942ib4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'd1a3f6b2-9f4a-4b2e-8c55-1a2b3c4d5e60', U&'Personal Acad\00E9mico', '2024-12-04T08:00:22'),
    (104, '943ib4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'd1a3f6b2-9f4a-4b2e-8c55-1a2b3c4d5e60', U&'Titulaci\00F3n Alternativa', '2024-12-05T08:00:22'),
    (105, '944ib4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'd1a3f6b2-9f4a-4b2e-8c55-1a2b3c4d5e60', U&'Seguimiento Acad\00E9mico', '2024-12-06T08:00:22'),
    (106, '945ib4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'd1a3f6b2-9f4a-4b2e-8c55-1a2b3c4d5e60', 'Registro e Inscripciones', '2024-12-07T08:00:22'),

    (107, '946ib4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'e2b4f7c3-0a5b-4c6d-9e66-2b3c4d5e6f71', U&'Gu\00EDa de tr\00E1mites', '2024-12-07T08:00:22'),
    (108, '947ib4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'e2b4f7c3-0a5b-4c6d-9e66-2b3c4d5e6f71', U&'Seguimiento de tr\00E1mites', '2024-12-07T08:00:22'),

    (109, '948ib4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'f3c5g8d4-1b6c-4d7e-0f77-3c4d5e6f7a82', 'Sistema GAIA', '2024-12-07T08:00:22'),

    (110, '949ib4e8-0856-4aad-b3aa-747e2dba76d9', '93j203b4-f63b-4c4a-be05-eae84cef0c0c', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'a4d6h9e5-2c7d-4e8f-1g88-4d5e6f7a8b93', U&'Informaci\00F3n de contactos', '2024-12-07T08:00:22');

insert into article (id, uuid, section_id, user_id, title, text, created_date) values
	(100, '819ab4e8-0856-4aad-b3aa-747e2dba76d9', '919ab4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Presentaci\00F3n', U&'De acuerdo con el Estatuto Org\00E1nico, la Direcci\00F3n de Planificaci\00F3n Acad\00E9mica define, en base a las determinaciones del Consejo Universitario las l\00EDneas generales de la pol\00EDtica acad\00E9mica universitaria y se ocupa de la incorporaci\00F3n de docentes y estudiantes de la Universidad Mayor de San Sim\00F3n.', '2024-12-02T07:53:22'),
    (101, '823ab4e8-0856-4aad-b3aa-747e2dba76d9', '919ab4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Misi\00F3n', U&'La Direcci\00F3n de Planificaci\00F3n Acad\00E9mica se constituye en la Unidad Institucional que tiene la misi\00F3n de regular y supervisar la Gesti\00F3n Acad\00E9mica de las Unidades Acad\00E9micas, contribuyendo a la integraci\00F3n de las funciones de Investigaci\00F3n e Interacci\00F3n con el proceso formativo a partir de un Nuevo Modelo Acad\00E9mico integral y comunitario en respuesta a las necesidades y o demandas sociales de formaci\00F3n profesional del contexto Regional y Nacional.', '2024-12-02T19:31:22'),
    (102, '839db4e8-0856-4aad-b3aa-747e2dba76d9', '919ab4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Visi\00F3n', U&'La Direcci\00F3n de Planificaci\00F3n Acad\00E9mica se constituye en una unidad t\00E9cnicamente din\00E1mica y eficaz que cuenta con personal altamente calificado y comprometido con el que hacer institucional y el apoyo de recursos tecnol\00F3gicos pertinentes para llevar adelante una Gesti\00F3n de Informaci\00F3n Acad\00E9mica de excelencia y calidad.', '2024-12-03T08:00:22'),
    (103, '842ib4e8-0856-4aad-b3aa-747e2dba76d9', '919ab4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Objetivo de la Unidad', U&'<ul><li>Definir los lineamientos y pol\00EDticas acad\00E9micas de la gesti\00F3n de formaci\00F3n profesional de grado para mejorar e integrar los procesos de formaci\00F3n, investigaci\00F3n e interacci\00F3n social en las Unidades Acad\00E9micas.</li><li>Regular los procesos de gesti\00F3n acad\00E9mica de formaci\00F3n profesional de grado de las Unidades Acad\00E9micas para contribuir al cumplimiento del Modelo Educativo y responder a las necesidades profesionales del contexto actual.</li><li>Planificar y regular el R\00E9gimen Acad\00E9mico Docente y Estudiantil para mejorar la calidad de la gesti\00F3n universitaria.</li></ul>', '2024-12-04T08:00:22'),
    (104, '845ib4e8-0856-4aad-b3aa-747e2dba76d9', '919ab4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Funciones principales de la Unidad', U&'<ul><li>Gestionar el proceso de admisi\00F3n, inscripci\00F3n y seguimiento de estudiantes coordinando con las Unidades Acad\00E9micas, a trav\00E9s de mecanismos, procedimientos y la normativa universitaria vigente de R\00E9gimen Estudiantil.</li><li>Gestionar y canalizar los procesos de admisi\00F3n y seguimiento del personal acad\00E9mico de las Unidades Acad\00E9micas.</li><li>Definir y gestionar las l\00EDneas generales de la pol\00EDtica curricular de la UMSS.</li><li>Definir y coordinar la elaboraci\00F3n de la planificaci\00F3n acad\00E9mica, con la Direcci\00F3n de Planificaci\00F3n Proyectos y Sistemas.</li><li>Supervisar y coordinar el proceso de administraci\00F3n acad\00E9mica de docentes y estudiantes de las Unidades Acad\00E9micas a trav\00E9s del Sistema de Informaci\00F3n San Sim\00F3n (SISS).</li><li>Definir la Pol\00EDtica de Desconcentraci\00F3n Universitaria para posibilitar el acceso de estudiantes del \00E1rea rural a la Educaci\00F3n Superior, de acuerdo a las necesidades del contexto de cada regi\00F3n.</li><li>Refrendar los procesos de gesti\00F3n acad\00E9mica y administrativa de la titulaci\00F3n de los estudiantes universitarios no graduados, seg\00FAn el reglamento, los procedimientos y el cronograma del PTAG en cumplimiento a las resoluciones emanadas por la MAE.</li><li>Proponer al Vicerrectorado proyectos referentes al R\00E9gimen docente y estudiantil, y la pol\00EDtica curricular de la UMSS para su consideraci\00F3n y aprobaci\00F3n en el Comit\00E9 Acad\00E9mico del Honorable Consejo Universitario.</li><li>Cumplir y hacer cumplir la normativa acad\00E9mica en asuntos y problem\00E1ticas relacionadas a la gesti\00F3n curricular, r\00E9gimen docente y estudiantil en las unidades concentradas y desconcentradas.</li><li>Atender a los requerimientos del Comit\00E9 Acad\00E9mico del Honorable Consejo Universitario, referente a pol\00EDticas, regulaciones y otros procesos de gesti\00F3n acad\00E9mica de grado.</li><li>Elevar informes peri\00F3dicos al Vicerrectorado sobre las actividades acad\00E9mico administrativas de sus departamentos y las unidades facultativas.</li></ul>', '2024-12-04T08:00:22'),

    (105, '846ib4e8-0856-4aad-b3aa-747e2dba76d9', '923ab4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '', '', '2024-12-05T08:00:22'),
    (106, '847ib4e8-0856-4aad-b3aa-747e2dba76d9', '923ab4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Objetivo de la Unidad', U&'<ul><li>Coordinar las actividades de planificaci\00F3n acad\00E9mica universitaria y de r\00E9gimen docente con las Direcciones Universitarias y las Unidades Acad\00E9micas.</li><li>Revisar y dar cumplimiento a la carga horaria del personal acad\00E9mico de las Unidades Facultativas en base a la normativa en vigencia.</li><li>Preparar modelos, sistemas y directrices para la planificaci\00F3n acad\00E9mica de la UMSS.</li><li>Coordinar las gestiones relacionadas a la designaci\00F3n de personal acad\00E9mico de las Facultades con las Unidades Acad\00E9micas y el Departamento de Personal Acad\00E9mico.</li><li>Apoyar en la gesti\00F3n de los tr\00E1mites relacionados a la actividad acad\00E9mica de la UMSS.</li></ul>', '2024-12-05T08:00:22'),
    (107, '848ib4e8-0856-4aad-b3aa-747e2dba76d9', '923ab4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Funciones Principales de la Unidad', U&'<ul><li>Coordinaci\00F3n en la definici\00F3n de m\00E9todos, sistemas, directrices y los marcos normativos para la formulaci\00F3n del Plan de Desarrollo Acad\00E9mico en coordinaci\00F3n con los departamentos de las diferentes Unidades Acad\00E9micas y la Direcci\00F3n de Planificaci\00F3n, Proyectos y Sistemas (DPPyS).</li><li>Participaci\00F3n en la actualizaci\00F3n y elaboraci\00F3n de la Gu\00EDa Pedag\00F3gica Universitaria con el Departamento de Desarrollo Curricular y otros Departamentos de la DPA y el CEUB.</li><li>Elaboraci\00F3n del Plan Operativo Anual del Departamento de Coordinaci\00F3n Acad\00E9mica, seg\00FAn normativas vigentes.</li><li>Elaboraci\00F3n de la propuesta del Calendario Acad\00E9mico de la UMSS, para su aprobaci\00F3n en el Comit\00E9 Acad\00E9mico y Honorable Consejo Universitario</li><li>Revisi\00F3n de la carga horaria docente y de auxiliares universitarios de las Unidades Acad\00E9micas.</li><li>Administraci\00F3n del Sistema de Gesti\00F3n de Informaci\00F3n Acad\00E9mica \2013 GAIA relativo a la carga horaria docente y auxiliares.</li><li>Verificaci\00F3n y elaboraci\00F3n de informes sobre los requisitos para el nombramiento de docentes y auxiliares universitarios por convocatoria, suplencia, designaci\00F3n por cambio de materia, etc.</li><li>Coordinaci\00F3n de todo proceso de titularizaci\00F3n y admisi\00F3n docente a requerimiento de las Unidades Acad\00E9micas, seg\00FAn la normativa universitaria vigente.</li></ul>', '2024-12-06T08:00:22'),
    (108, '849ib4e8-0856-4aad-b3aa-747e2dba76d9', '923ab4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Jefe Departamento de Coordinaci\00F3n Acad\00E9mica', U&'<ul><li>Coordinar las actividades acad\00E9micas y administrativas de planificaci\00F3n y de R\00E9gimen Docente con las Direcciones Universitarias y las Unidades Acad\00E9micas.</li><li>Proponer las directrices para la planificaci\00F3n y la formulaci\00F3n de la carga horaria del personal acad\00E9mico.</li></ul>', '2024-12-07T08:00:22'),
    (109, '850ib4e8-0856-4aad-b3aa-747e2dba76d9', '923ab4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Secretaria Departamento de Coordinaci\00F3n Acad\00E9mica', U&'<ul><li>Apoyar al Departamento de Coordinaci\00F3n Acad\00E9mica con trabajos de secretariado y el manejo de correspondencia tanto interna como externa, as\00ED como su resguardo y archivo.</li></ul>', '2024-12-08T08:00:22'),
    (110, '851ib4e8-0856-4aad-b3aa-747e2dba76d9', '923ab4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'T\00E9cnico en Procesamiento de Informaci\00F3n Acad\00E9mica', U&'<ul><li>Apoyar en la gesti\00F3n y administraci\00F3n de informaci\00F3n acad\00E9mica de los procesos de formaci\00F3n profesional de grado, a trav\00E9s del uso de las nuevas tecnolog\00EDas de informaci\00F3n y comunicaci\00F3n.</li><li>Apoyar de forma t\00E9cnica y operativa en la gesti\00F3n de coordinaci\00F3n acad\00E9mica del Departamento y las Unidades Acad\00E9micas de la Universidad.</li><li>Coordinar con los Departamentos de Personal Acad\00E9mico, Departamento de Seguimiento y Administraci\00F3n del SISS y la UPSI el manejo de la informaci\00F3n acad\00E9mica estandarizada.</li></ul>', '2024-12-09T08:00:22'),
    (111, '852ib4e8-0856-4aad-b3aa-747e2dba76d9', '923ab4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'T\00E9cnico I en Gesti\00F3n Acad\00E9mica', U&'<ul><li>Apoyar en el proceso de gesti\00F3n acad\00E9mica con las Direcciones Universitarias y las Unidades Acad\00E9micas.</li><li>Procesar los tr\00E1mites de designaci\00F3n de Docentes y Auxiliares en coordinaci\00F3n con las Direcciones Universitarias, Unidades Acad\00E9micas y Departamentos de la DPA.</li></ul>', '2024-12-10T08:00:22'),
    (112, '853ib4e8-0856-4aad-b3aa-747e2dba76d9', '923ab4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'T\00E9cnico II en Gesti\00F3n Acad\00E9mica', U&'<ul><li>Apoyar en el proceso de gesti\00F3n acad\00E9mica con las Direcciones Universitarias y las Unidades Acad\00E9micas.</li><li>Procesar los tr\00E1mites de designaci\00F3n de Docentes a Dedicaci\00F3n Exclusiva y Docentes Investigadores en coordinaci\00F3n con las Direcciones Universitarias, Unidades Acad\00E9micas y Departamentos de la DPA.</li></ul>', '2024-12-11T08:00:22'),
    (113, '854ib4e8-0856-4aad-b3aa-747e2dba76d9', '923ab4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'T\00E9cnico en Planificaci\00F3n Acad\00E9mica', U&'<ul><li>Evaluar el desarrollo de las actividades acad\00E9micas universitarias y proponer metodolog\00EDas y directrices para mejorar los procesos de formaci\00F3n profesional de grado.</li><li>Apoyar en la definici\00F3n y actualizaci\00F3n de lineamientos, procedimientos, pol\00EDticas y normativas orientadas a mejorar los procesos de planificaci\00F3n acad\00E9mica universitaria.</li><li>Coordinar el proceso de planificaci\00F3n acad\00E9mica y su implementaci\00F3n con los Departamentos, Direcciones Universitarias y las Unidades Acad\00E9micas.</li><li>Apoyar en la formulaci\00F3n del Plan Operativo Anual y la autoevaluaci\00F3n del Departamento.</li></ul>', '2024-12-12T08:00:22'),

    (114, '855ib4e8-0856-4aad-b3aa-747e2dba76d9', '939db4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '', '', '2024-12-12T08:00:23'),
    (115, '856ib4e8-0856-4aad-b3aa-747e2dba76d9', '939db4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Objetivo de la Unidad', U&'<ul><li>Proponer la pol\00EDtica curricular universitaria, normar, asesorar y evaluar los procesos de transformaci\00F3n en innovaci\00F3n curricular de las carreras y programas de formaci\00F3n profesional, de grado con criterios de calidad acad\00E9mica y pertinencia social, en el marco del Modelo Acad\00E9mico de la UMSS.</li><li>Proponer pol\00EDticas y normativas de desarrollo curricular de carreras y programas de formaci\00F3n profesional de grado, en coordinaci\00F3n con las Direcciones Universitarias, Departamentos y Unidades Acad\00E9micas, en el marco del Estatuto Org\00E1nico y el Modelo Educativo de la UMSS.</li><li>Promover la transformaci\00F3n, actualizaci\00F3n e innovaci\00F3n curricular de las Unidades Acad\00E9micas de acuerdo al Modelo Educativo de la UMSS.</li></ul>', '2024-12-12T08:00:23'),
    (116, '857ib4e8-0856-4aad-b3aa-747e2dba76d9', '939db4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Funciones Principales de la Unidad', U&'<ul><li>Proponer y actualizar pol\00EDticas y normativa curricular para elaborar el dise\00F1o, redise\00F1o curricular y ajustes parciales a los Planes de Estudio y otras innovaciones curriculares de las Carreras y/o Programas de la UMSS.</li><li>Proponer pol\00EDticas referidas al R\00E9gimen Estudiantil (a las Modalidades de Admisi\00F3n y de Graduaci\00F3n).</li><li>Proponer la pol\00EDtica acad\00E9mica de integraci\00F3n curricular de las Tecnolog\00EDas de Informaci\00F3n y Comunicaci\00F3n (TIC), en coordinaci\00F3n con las Unidades Acad\00E9micas de la UMSS.</li><li>Proponer pol\00EDticas de Evaluaci\00F3n Curricular, en coordinaci\00F3n con la DUEA.</li><li>Desarrollar procesos de capacitaci\00F3n sobre dise\00F1o, redise\00F1o curricular y ajustes parciales al Plan de Estudios de las Carreras o Programas de formaci\00F3n profesional de grado.</li><li>Asesorar a las Unidades Acad\00E9micas en la elaboraci\00F3n de dise\00F1os, redise\00F1os curriculares y ajustes parciales en los Planes de Estudio y otros aspectos relacionados con el \00E1mbito curricular.</li><li>Verificar que los dise\00F1os, redise\00F1os curriculares y ajustes parciales de las Carreras y Programas cumplan con los requisitos acad\00E9micos, en el marco de la pol\00EDtica y normativa de la UMSS y del Sistema Universitario Boliviano.</li><li>Elaborar el Plan Operativo Anual del Departamento, seg\00FAn normativa universitaria vigente.</li></ul>', '2024-12-12T08:00:24'),
    (117, '858ib4e8-0856-4aad-b3aa-747e2dba76d9', '939db4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Jefe Departamento de Desarrollo Curricular', U&'<ul><li>Proponer la definici\00F3n y actualizaci\00F3n de la pol\00EDtica curricular universitaria y avalar los procesos de elaboraci\00F3n de dise\00F1os y redise\00F1os curriculares resultado de la transformaci\00F3n e innovaci\00F3n curricular, con criterios de calidad acad\00E9mica y pertinencia social y en el marco del Modelo Educativo de la UMSS.</li></ul>', '2024-12-12T08:00:25'),
    (118, '859ib4e8-0856-4aad-b3aa-747e2dba76d9', '939db4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Secretaria Departamento de Desarrollo Curricular', U&'<ul><li>Apoyar al departamento con trabajos de secretariado relacionado con la actividad administrativo-acad\00E9mica y manejo de correspondencia tanto interna  como externa y su resguardo y archivo de la documentaci\00F3n generada.</li></ul>', '2024-12-12T08:00:26'),
    (119, '860ib4e8-0856-4aad-b3aa-747e2dba76d9', '939db4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Analista (4)', U&'<ul><li>Contribuir en la actualizaci\00F3n de la pol\00EDtica curricular universitaria para la elaboraci\00F3n de dise\00F1os, redise\00F1os curriculares, ajustes parciales de los Planes de Estudio y otras innovaciones de las carreras y/o programas de formaci\00F3n profesional de grado, en el marco de la normativa universitaria vigente.</li></ul>', '2024-12-12T08:00:27'),

    (120, '861ib4e8-0856-4aad-b3aa-747e2dba76d9', '942ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '', '', '2024-12-12T08:00:27'),
    (121, '862ib4e8-0856-4aad-b3aa-747e2dba76d9', '942ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Objetivo de la Unidad', U&'<ul><li>Administrar el personal acad\00E9mico, a trav\00E9s de la aplicaci\00F3n de la normativa nacional y pol\00EDticas universitarias en materia de Gesti\00F3n de Recursos Humanos.</li><li>Verificar la aplicaci\00F3n y cumplimiento de acciones y procedimientos del Reglamento General de Docencia e Incompatibilidad, asistencias y remuneraci\00F3n salarial, en base a las directrices acad\00E9micas emanadas por la DPA.</li></ul>', '2024-12-12T08:00:27'),
    (122, '863ib4e8-0856-4aad-b3aa-747e2dba76d9', '942ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Funciones Principales de la Unidad', U&'<ul><li>Procesar planillas de haberes del personal acad\00E9mico de las unidades concentradas y desconcentradas, cursos de temporada, Posgrado, auxiliares de docencia, consultor\00EDas y otros en coordinaci\00F3n con el Departamento de Tecnolog\00EDas de informaci\00F3n y comunicaci\00F3n DTIC.</li><li>Administrar la base de datos del curr\00EDculo docente previa aprobaci\00F3n del Comit\00E9 Acad\00E9mico del HCU.</li><li>Verificar y controlar el cumplimiento de la ejecuci\00F3n presupuestaria de la carga horaria del personal docente y auxiliar de las Unidades Acad\00E9micas, previa coordinaci\00F3n con el Departamento de Coordinaci\00F3n Acad\00E9mica.</li><li>Realizar el control, registro y ejecuci\00F3n salarial seg\00FAn el Escalaf\00F3n Docente.</li><li>Verificar la aplicaci\00F3n y cumplimiento de acciones y procedimientos del Reglamento General de Docencia e Incompatibilidad, Asistencias y Remuneraci\00F3n Salarial.</li><li>Mantener actualizado el registro de Escalaf\00F3n y el Sistema de Datos del R\00E9gimen docente con todas las acciones del personal como nombramientos, resoluciones, memor\00E1ndums, certificaciones de a\00F1os de servicios y otros.</li><li>Controlar los registros de personal acad\00E9mico, licencias, declaratorias en comisi\00F3n, con y sin goce de haberes, vacaciones y otras solicitudes, seg\00FAn la normativa universitaria vigente de r\00E9gimen docente.</li><li>Revisar y transcribir los partes diarios y mensuales de asistencia del personal acad\00E9mico para la elaboraci\00F3n de planillas mensuales y viabilizar la remuneraci\00F3n correspondiente al personal acad\00E9mico.</li><li>Procesar y migrar los descargos de RC-IVAs de los docentes y de todos los descuentos de Ley.</li><li>Cumplir y hacer cumplir las disposiciones legales vigentes como la Ley General del Trabajo, Ley SAFCO, Ley Financial, Estatuto Org\00E1nico, Reglamento General de la Docencia y otros.</li><li>Emitir certificaciones al personal acad\00E9mico e instituciones solicitantes, de acuerdo a requerimiento.</li><li>Aplicar procedimientos para el tr\00E1mite de pago de beneficios sociales para el personal acad\00E9mico de la UMSS.</li><li>Elaborar y registrar a trav\00E9s del SIS POA el Plan Operativo Anual del Departamento, seg\00FAn normativa universitaria vigente.</li></ul>', '2024-12-12T08:00:28'),
    (123, '864ib4e8-0856-4aad-b3aa-747e2dba76d9', '942ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Jefe Departamento de Personal Acad\00E9mico', U&'<ul><li>Administrar el personal acad\00E9mico mediante la aplicaci\00F3n de pol\00EDticas de gesti\00F3n de Recursos Humanos y el Reglamento General de la Docencia en coordinaci\00F3n con las unidades acad\00E9micas de la Direcci\00F3n de Planificaci\00F3n Acad\00E9mica de la Universidad.</li></ul>', '2024-12-12T08:00:29'),
    (124, '865ib4e8-0856-4aad-b3aa-747e2dba76d9', '942ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Secretaria Departamento de Personal Acad\00E9mico', U&'<ul><li>Apoyar al Departamento con trabajos de secretariado relacionado con la actividad acad\00E9mico administrativas y manejo de correspondencia tanto interna como externa y su correspondiente resguardo y archivo de la documentaci\00F3n generada por el departamento.</li></ul>', '2024-12-12T08:00:30'),
    (125, '866ib4e8-0856-4aad-b3aa-747e2dba76d9', '942ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Analista Jur\00EDdico', U&'<ul><li>Apoyo jur\00EDdico y legal en general al Departamento, velando por el cumplimiento de los derechos, beneficios y obligaciones otorgados por disposiciones legales nacionales y universitarias.</li></ul>', '2024-12-12T08:00:31'),
    (126, '867ib4e8-0856-4aad-b3aa-747e2dba76d9', '942ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Jefe Divisi\00F3n Recursos Humanos Acad\00E9micos', U&'<ul><li>Dar cumplimiento a los diferentes documentos y tr\00E1mites que llega al Departamento de Personal Acad\00E9mico, cumpliendo las normas establecidas por la legislaci\00F3n de la Universidad Mayor de San Sim\00F3n.</li></ul>', '2024-12-12T08:00:32'),
    (127, '868ib4e8-0856-4aad-b3aa-747e2dba76d9', '942ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Responsable de Planillas Acad\00E9micas', U&'<ul><li>Elaboraci\00F3n oportuna y eficientemente la planilla mensual de haberes del personal docente, cumpliendo con la ejecuci\00F3n presupuestaria y la aplicaci\00F3n de los reglamentos y normativa vigente.</li></ul>', '2024-12-12T08:00:33'),
    (128, '869ib4e8-0856-4aad-b3aa-747e2dba76d9', '942ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Encargado de Planillas Auxiliares', U&'<ul><li>Elaboraci\00F3n oportuna y eficiente de la planilla mensual de haberes del personal auxiliar y de las asignaciones familiares del sector docente, cumpliendo con la ejecuci\00F3n presupuestaria y la aplicaci\00F3n de los reglamentos y normativa vigente.</li></ul>', '2024-12-12T08:00:34'),
    (129, '870ib4e8-0856-4aad-b3aa-747e2dba76d9', '942ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Encargado de Partes Diarios de Asistencia Tiempo Horario', U&'<ul><li>Verificar y archivar la asistencia diaria, manual y biom\00E9trico, de docentes tiempo horario y auxiliares en correspondencia al parte mensual para la elaboraci\00F3n de planillas.</li></ul>', '2024-12-12T08:00:35'),
    (130, '871ib4e8-0856-4aad-b3aa-747e2dba76d9', '942ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Encargado de Planillas por Contratos de Servicio', U&'<ul><li>Elaboraci\00F3n de planillas por contratos de servicio de la instituci\00F3n, asistencia a la elaboraci\00F3n y control de planillas acad\00E9micas y/o escalaf\00F3n.</li></ul>', '2024-12-12T08:00:36'),
    (131, '872ib4e8-0856-4aad-b3aa-747e2dba76d9', '942ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Responsable de Planificaci\00F3n, Organizaci\00F3n y Registro de Escalaf\00F3n Docente', U&'<ul><li>Apoyo a dar cumplimiento a los diferentes documentos y tramites que llega al Departamento de Personal Acad\00E9mico, cumpliendo las normas establecidas por la legislaci\00F3n de la Universidad Mayor de San Sim\00F3n. Elaboraci\00F3n oportuna del escalaf\00F3n docente, el registro y control de documentos de los files personales de los docentes de las diferentes unidades acad\00E9micas de la Universidad.</li></ul>', '2024-12-12T08:00:37'),
    (132, '873ib4e8-0856-4aad-b3aa-747e2dba76d9', '942ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Encargado de la Emisi\00F3n y Recepci\00F3n de Licencias', U&'<ul><li>Registrar, verificar y archivar todas las licencias, as\00ED como el control de la AFPS y el pago de contribuciones en exceso del Personal Acad\00E9mico.</li></ul>', '2024-12-12T08:00:38'),
    (133, '874ib4e8-0856-4aad-b3aa-747e2dba76d9', '942ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Responsable de Registro y Certificaci\00F3n de Años de Servicio', U&'<ul><li>Elaborar, verificar y certificar los a\00F1os de servicio en base a planillas de haberes del personal Acad\00E9mico, tanto activos como pasivos.</li></ul>', '2024-12-12T08:00:39'),
    (134, '875ib4e8-0856-4aad-b3aa-747e2dba76d9', '942ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Responsable de Archivos del Departamento de Personal Acad\00E9mico', U&'<ul><li>Preservar, resguardar, documentos hist\00F3ricos importantes, as\00ED como el control de entradas y salidas de documentos del Departamento de Personal Acad\00E9mico.</li></ul>', '2024-12-12T08:00:40'),
    (135, '876ib4e8-0856-4aad-b3aa-747e2dba76d9', '942ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Responsable Inform\00E1tico', U&'<ul><li>Brindar asistencia inform\00E1tica y soporte t\00E9cnico. Resguardar documentos hist\00F3ricos importantes, as\00ED como el control de saldo RC-IVA del Personal Acad\00E9mico.</li></ul>', '2024-12-12T08:00:41'),
    (136, '877ib4e8-0856-4aad-b3aa-747e2dba76d9', '942ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Encargado de Control de Partes Diarios de Asistencia Tiempo Completo', U&'<ul><li>Resguardar documentos hist\00F3ricos importantes, as\00ED como el control de asistencia del plantel acad\00E9mico de tiempo completo.</li></ul>', '2024-12-12T08:00:42'),
    (137, '878ib4e8-0856-4aad-b3aa-747e2dba76d9', '942ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Responsable de Seguimiento Declaraciones Juradas de Bienes y Rentas (DJBR)', U&'<ul><li>Supervisar el cumplimiento oportuno de la Declaraci\00F3n Jurada de Bienes y Rentas de los funcionarios docentes y administrativos de la UMSS, para lo cual se remite informes trimestrales a la MAE de la UMSS.</li></ul>', '2024-12-12T08:00:43'),

    (138, '879ib4e8-0856-4aad-b3aa-747e2dba76d9', '943ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '', '', '2024-12-12T08:00:44'),
    (139, '880ib4e8-0856-4aad-b3aa-747e2dba76d9', '943ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Objetivo de la Unidad', U&'<ul><li>Coadyuvar de manera sist\00E9mica con la titulaci\00F3n de estudiantes universitarios no graduados, cumpliendo con los requisitos acad\00E9micos necesarios y proporcion\00E1ndoles herramientas, instrumentos y medios necesarios para este acometido; acreditando con la calidad del programa, la idoneidad profesional de los graduados, a trav\00E9s de los proceso acad\00E9mico administrativos del Programa de Titulaci\00F3n Alternativa y Graduaci\00F3n (PTAG).</li><li>Administrar el proceso de titulaci\00F3n de estudiantes que han concluido el Plan de estudios en las diferentes Carreras y Programas de las Unidades Acad\00E9micas de la UMSS y de las otras universidades pertenecientes al Sistema de la Universidad Boliviana.</li><li>Ejecutar dos versiones del PTAG en cada gesti\00F3n, de acuerdo a Reglamento y normativa universitaria vigente.</li><li>Realizar el seguimiento acad\00E9mico administrativo a las diferentes etapas del proceso de titulaci\00F3n, de acuerdo al cronograma de actividades previsto en cada gesti\00F3n.</li><li>Incrementar las ofertas de titulaci\00F3n a trav\00E9s del PTAG.</li></ul>', '2024-12-12T08:00:44'),
    (140, '881ib4e8-0856-4aad-b3aa-747e2dba76d9', '943ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Funciones Principales de la Unidad', U&'<ul><li>Hacer cumplir las normas y procedimientos acad\00E9mico- administrativos del proceso de titulaci\00F3n en conformidad al Estatuto Org\00E1nico de la UMSS, el Reglamento del PTAG y la normativa universitaria vigente.</li><li>Planificar y ejecutar el cronograma de actividades acad\00E9mica - administrativas de cada versi\00F3n del PTAG (dos versiones en la gesti\00F3n).</li><li>Proponer a instancias superiores y al Comit\00E9 Acad\00E9mico del HCU mejoras e innovaciones en los planes de titulaci\00F3n del PTAG en coordinaci\00F3n con la Direcci\00F3n de Planificaci\00F3n Acad\00E9mica.</li><li>Coordinar la designaci\00F3n de docentes y coordinador facultativo con las Escuelas y/o Unidades Acad\00E9micas para las diferentes etapas del proceso de titulaci\00F3n del PTAG.</li><li>Verificar y refrendar el cumplimiento de requisitos del proceso de designaci\00F3n del coordinador facultativo y de docentes, por la Escuela y/o Unidades Acad\00E9micas para las diferentes etapas del proceso de titulaci\00F3n del PTAG.</li><li>Gestionar la contrataci\00F3n de docentes para cada etapa del proceso de titulaci\00F3n, seg\00FAn las necesidades de la versi\00F3n, normativa vigente del PTAG y normativas universitarias, cuando la n\00F3mina de docentes no llegue de forma oportuna.</li><li>Avalar los documentos de los postulantes que fueron clasificados en la segunda categor\00EDa (Trabajo Dirigido Interdisciplinario) conjuntamente con el Coordinador de Escuela y/o Facultad.</li><li>Avalar los documentos de postulantes que fueron clasificados en la primera categor\00EDa (Memoria Profesional) conjuntamente con el Coordinador de Escuela y/o Facultad y un representante de la Direcci\00F3n de Planificaci\00F3n Acad\00E9mica.</li><li>Elaborar, registrar en el SISPOA y ejecutar el Plan Operativo Anual del Programa de acuerdo a las directrices y normativa universitaria vigente.</li><li>Ejecutar el presupuesto del PTAANG, seg\00FAn los procedimientos establecidos por la DAF.</li></ul>', '2024-12-12T08:00:45'),
    (141, '882ib4e8-0856-4aad-b3aa-747e2dba76d9', '943ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Jefe del Programa de Titulaci\00F3n Alternativa y Graduaci\00F3n (PTAG)', U&'<ul><li>Gestionar de manera sistem\00E1tica y efectiva la administraci\00F3n del Programa de Titulaci\00F3n Alternativa y Graduaci\00F3n en las diferentes Carreras y Programas de las Unidades Acad\00E9micas de la UMSS y del Sistema de la Universidad Boliviana.</li></ul>', '2024-12-12T08:00:46'),
    (142, '883ib4e8-0856-4aad-b3aa-747e2dba76d9', '943ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Secretaria PTAG', U&'<ul><li>Apoyar a la jefatura del PTAG con trabajos de secretariado relacionado con la actividad acad\00E9mico- administrativa.</li></ul>', '2024-12-12T08:00:47'),
    (143, '884ib4e8-0856-4aad-b3aa-747e2dba76d9', '943ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Responsable Acad\00E9mico', U&'<ul><li>Coordinar con las unidades facultativas para mejorar de manera eficiente y efectiva los procesos de gesti\00F3n acad\00E9mico-administrativo del PTAG en las modalidades presencial y virtual.</li></ul>', '2024-12-12T08:00:48'),
    (144, '885ib4e8-0856-4aad-b3aa-747e2dba76d9', '943ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Administrador PTAG', U&'<ul><li>Administrar los recursos materiales, financieros y los sistemas de administraci\00F3n universitaria precautelando los intereses del PTAG, optimizando los recursos y maximizando los resultados en el marco de los sistemas establecidos por la LEY1178 SAFCO.</li><li>Gestionar y hacer seguimiento al proceso de contrataci\00F3n del personal eventual con cargos de Coordinadores Facultativos, Docentes para M\00F3dulo de Reforzamiento, Elaboradores de T\00E9rminos de Referencia, Tutores y Tribunales.</li></ul>', '2024-12-12T08:00:49'),
    (145, '886ib4e8-0856-4aad-b3aa-747e2dba76d9', '943ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Apoyo Acad\00E9mico', U&'<ul><li>Apoyar en todas las actividades del proceso acad\00E9mico a los Responsables Acad\00E9micos del PTAG de las diferentes Facultades.</li></ul>', '2024-12-12T08:00:50'),
    (146, '887ib4e8-0856-4aad-b3aa-747e2dba76d9', '943ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Apoyo Administrativo', U&'<ul><li>Apoyar al PTAG en actividades administrativas.</li></ul>', '2024-12-12T08:00:51'),

    (147, '888ib4e8-0856-4aad-b3aa-747e2dba76d9', '944ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '', '', '2024-12-12T08:00:52'),
    (148, '889ib4e8-0856-4aad-b3aa-747e2dba76d9', '944ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Objetivo de la Unidad', U&'<ul><li>Realizar seguimiento, control y an\00E1lisis de procesos de administraci\00F3n acad\00E9mica desarrollados en el sistema de informaci\00F3n San Sim\00F3n, recopilando, sistematizando y publicando datos acad\00E9micos verificados en el sistema estad\00EDstico San Sim\00F3n, gestionando la integraci\00F3n de informaci\00F3n acad\00E9mica.</li></ul>', '2024-12-12T08:00:52'),
    (149, '890ib4e8-0856-4aad-b3aa-747e2dba76d9', '944ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Funciones Principales de la Unidad', U&'<ul><li>Registrar planes de estudio de las Carreras y/o Programas en la estructura acad\00E9mica del sistema de informaci\00F3n San Sim\00F3n, previo informe del Departamento de Desarrollo Curricular y la aprobaci\00F3n del Comit\00E9 Acad\00E9mico del Honorable Consejo Universitario.</li><li>Mejorar la administraci\00F3n de informaci\00F3n acad\00E9mica, as\00ED como actualizaci\00F3n e implementaci\00F3n de nuevas opciones en el sistema de informaci\00F3n San Sim\00F3n.</li><li>Dar seguimiento de los procesos acad\00E9micos regulares implementados, con las Direcciones Acad\00E9micas y las UTI\2019s de cada Unidad Facultativa.</li><li>Informar sobre las actividades de control, seguimiento, actualizaci\00F3n e implementaci\00F3n de nuevas opciones en el sistema de informaci\00F3n San Sim\00F3n.</li><li>Publicar informaci\00F3n acad\00E9mica a trav\00E9s del Sistema Estad\00EDstico San Sim\00F3n.</li><li>Recopilar, analizar y publicar datos estad\00EDsticos institucionales en el sistema Estad\00EDstico San Sim\00F3n en coordinaci\00F3n con el DTIC.</li><li>Realizar control y seguimiento al cumplimiento de los calendarios acad\00E9micas de unidades facultativas o escuelas.</li><li>Informar sobre el cumplimiento de calendarios acad\00E9micos aprobados por cada unidad acad\00E9mica, en relaci\00F3n al calendario acad\00E9mico Universitario.</li><li>Revisar el cumplimiento de normas y reglamentos establecidos en el proceso de admisi\00F3n de estudiantes nuevos de cada unidad acad\00E9mica.</li><li>Elaborar y registrar a trav\00E9s de los SISPOA el plan Operativo Anual del Departamento, seg\00FAn normativa Universitaria Vigente.</li></ul>', '2024-12-12T08:00:53'),
    (150, '891ib4e8-0856-4aad-b3aa-747e2dba76d9', '944ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Jefe Departamento de Seguimiento y Administraci\00F3n de Informaci\00F3n Acad\00E9mica', U&'<ul><li>Realizar procesos de seguimiento y administraci\00F3n de informaci\00F3n acad\00E9mica, supervisando la administraci\00F3n del sistema de informaci\00F3n San Sim\00F3n, del sistema estad\00EDstico San Sim\00F3n y del sistema de control de calendarios de unidades acad\00E9micas, buscando que los sistemas de informaci\00F3n acad\00E9mica se encuentren integrados, en coordinaci\00F3n con los diferentes Departamentos de la DPA, las Unidades Facultativas y DTIC.</li></ul>', '2024-12-12T08:00:54'),
    (151, '892ib4e8-0856-4aad-b3aa-747e2dba76d9', '944ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Administrador del Sistema de Informaci\00F3n San Sim\00F3n (SISS)', U&'<ul><li>Administrar el Sistema de Informaci\00F3n San Sim\00F3n (SISS) en relaci\00F3n a los procesos de administraci\00F3n acad\00E9mica, de docentes y estudiantes de la UMSS, coordinando el control y seguimiento de estos procesos con los Departamentos de la DPA, Unidades Facultativas y UTI\2019s Facultativas.</li></ul>', '2024-12-12T08:00:55'),
    (152, '893ib4e8-0856-4aad-b3aa-747e2dba76d9', '944ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Analista Estad\00EDstico', U&'<ul><li>Administrar t\00E9cnica y operativamente del Sistema Estad\00EDstico San Sim\00F3n, as\00ED como preparar, revisar y analizar la informaci\00F3n estad\00EDstica de la actividad acad\00E9mica, docente, estudiantil y administrativa de las diferentes Unidades Acad\00E9micas.</li></ul>', '2024-12-12T08:00:56'),
    (153, '894ib4e8-0856-4aad-b3aa-747e2dba76d9', '944ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Inform\00E1tico', U&'<ul><li>Analizar, dise\00F1ar, implementar y mantener sistemas inform\00E1ticos en apoyo a las actividades y procesos de administraci\00F3n de informaci\00F3n acad\00E9mica.</li></ul>', '2024-12-12T08:00:57'),
    (154, '895ib4e8-0856-4aad-b3aa-747e2dba76d9', '944ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Asistente T\00E9cnico', U&'<ul><li>Brindar apoyo administrativo, soporte t\00E9cnico y mantenimiento preventivo y correctivo en equipos inform\00E1ticos en general de todas las unidades de la DPA.</li></ul>', '2024-12-12T08:00:58'),

    (155, '896ib4e8-0856-4aad-b3aa-747e2dba76d9', '945ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '', '', '2024-12-12T08:00:59'),
    (156, '897ib4e8-0856-4aad-b3aa-747e2dba76d9', '945ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Objetivo de la Unidad', U&'<ul><li>Gestionar los procesos acad\00E9mico-administrativos del R\00E9gimen Estudiantil (admis\00F3n, permanencia y graduaci\00F3n) en base a las normas y reglamentos vigentes que regulan la UMSS.</li><li>Administrar los procesos de admisi\00F3n, registro e inscripci\00F3n de estudiantes nuevos que aprobaron alguna modalidad de admisi\00F3n en las diferentes facultades.</li><li>Administrar los procesos de seguimiento, registro y control de la permanencia estudiantil, en base a lo estipulado en los reglamentos del R\00E9gimen Estudiantil.</li><li>Administrar los procesos de Certificaci\00F3n de Estudios, Notas y Conclusi\00F3n de Plan de Estudios de acuerdo al an\00E1lisis y revisi\00F3n de los pensum registrados en el SISS.</li></ul>', '2024-12-12T08:00:59'),
    (157, '898ib4e8-0856-4aad-b3aa-747e2dba76d9', '945ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Funciones Principales de la Unidad', U&'<ul><li>Administrar los periodos de admisi\00F3n estudiantil en coordinaci\00F3n con las Unidades.</li><li>Acad\00E9micas y Direcciones Universitarias que intervienen en este proceso, en sujeci\00F3n al calendario acad\00E9mico aprobado.</li><li>Administrar la disponibilidad de recursos humanos y tecnol\00F3gicos para gestionar los procesos de admisi\00F3n, permanencia y graduaci\00F3n estudiantil.</li><li>Administrar los procesos acad\00E9mico-administrativos de R\00E9gimen Estudiantil referente a tr\00E1mites de admisi\00F3n, permanencia y graduaci\00F3n seg\00FAn procedimientos y reglamentos aprobados.</li><li>Socializar la normativa y los procesos acad\00E9mico-administrativos realizados por el Departamento de Registros e Inscripciones.</li><li>Sistematizar y digitalizar los expedientes estudiantiles, planillas de notas, documentos. referentes a la admisi\00F3n de estudiantes nuevos entre otros en el Sistema de Informaci\00F3n de digitalizaci\00F3n de archivos.</li><li>Organizar, archivar, resguardar y conservar la correspondencia, documentos generados de los procesos acad\00E9mico-administrativos, expedientes estudiantiles, planilla de notas, etc. mediante un Sistema de Plan de Archivo, infraestructura y equipamiento adecuado.</li><li>Implementar plataformas de atenci\00F3n para los estudiantes extranjeros, aplicando normas nacionales, convenios internacionales de movilidad estudiantil transitoria, convenios que rigen la permanencia estudiantil en un pa\00EDs y otros inherentes a extranjer\00EDa, con personal capacitado en leyes migratorias.</li><li>Elaborar y registrar a trav\00E9s del SISPOAS el Plan Operativo Anual del Departamento, seg\00FAn normativa universitaria vigente.</li></ul>', '2024-12-12T08:00:59'),
    (158, '899ib4e8-0856-4aad-b3aa-747e2dba76d9', '945ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Jefe de Departamento de Registros e Inscripciones', U&'<ul><li>Dirigir, supervisar, coordinar y controlar las funciones del personal profesional y t\00E9cnico, de acuerdo a lo establecido en los reglamentos normas y procedimientos que rigen el funcionamiento del Departamento de Registros e Inscripciones, para gestionar el proceso del R\00E9gimen Estudiantil (admis\00F3n, seguimiento, control y conclusi\00F3n del plan de estudios) de estudiantes nuevos y regulares de la Universidad.</li></ul>', '2024-12-12T08:00:59'),
    (159, '900ib4e8-0856-4aad-b3aa-747e2dba76d9', '945ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Secretaria', U&'<ul><li>Apoyar con trabajos de secretariado relacionado con la actividad administrativa y manejo de correspondencia tanto interna como externa y su correspondiente resguardo y archivo de la documentaci\00F3n generada por el Departamento de Registros e Inscripciones.</li></ul>', '2024-12-12T08:00:59'),
    (160, '901ib4e8-0856-4aad-b3aa-747e2dba76d9', '945ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Mensajero', U&'<ul><li>Apoyo en trabajos de porter\00EDa, mensajer\00EDa, fotocopiado y difusi\00F3n de documentos y trabajos manuales.</li></ul>', '2024-12-12T08:00:59'),
    (161, '902ib4e8-0856-4aad-b3aa-747e2dba76d9', '945ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Responsable TIC Registros', U&'<ul><li>Desarrollar sistemas que mejoren el funcionamiento del Departamento y proveer soporte t\00E9cnico inform\00E1tico a los funcionarios, respecto al registro, seguimiento y evaluaci\00F3n de datos en Sistema de Informaci\00F3n implementada en el Departamento de Registros e Inscripciones la operaci\00F3n del equipamiento.</li></ul>', '2024-12-12T08:00:59'),
    (162, '903ib4e8-0856-4aad-b3aa-747e2dba76d9', '945ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Responsable UTI Registros', U&'<ul><li>Manejar y administrar los diferentes sistemas de informaci\00F3n de la UMSS y supervisar los tr\00E1mites realizados en el Departamento de Registros e Inscripciones, permitir el correcto registro y/o modificaci\00F3n de datos en el Sistema de Informaci\00F3n San Sim\00F3n (SISS), resolver solicitudes con relaci\00F3n a la transcripci\00F3n de notas, evaluar el seguimiento acad\00E9mico de los estudiantes y emitir informes t\00E9cnicos de excelencia acad\00E9mica.</li></ul>', '2024-12-12T08:00:59'),
    (163, '904ib4e8-0856-4aad-b3aa-747e2dba76d9', '945ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Responsable de Admisiones Especiales y Cambios de Carreras', U&'<ul><li>Ejecutar los tr\00E1mites de admisiones especiales tanto para profesionales de la UMSS y del Sistema Universitario Boliviano (SUB), cambios de carrera dentro la Facultad y entre Facultades diferentes de la UMSS.</li></ul>', '2024-12-12T08:00:59'),
    (164, '905ib4e8-0856-4aad-b3aa-747e2dba76d9', '945ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Responsable de Estudios Simult\00E1neos, Traspasos y Otros Tr\00E1mites Adicionales', U&'<ul><li>Ejecutar los tr\00E1mites de estudio simult\00E1neo, traspaso de universidad, convalidaci\00F3n de materias y conversi\00F3n de notas.</li></ul>', '2024-12-12T08:00:59'),
    (165, '906ib4e8-0856-4aad-b3aa-747e2dba76d9', '945ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Responsable de Admisiones de Estudiantes Nuevos', U&'<ul><li>Ejecutar los tr\00E1mites de admisi\00F3n de alumnos nuevos de las diferentes unidades facultativas de la Universidad y de alumnos que aprobaron una modalidad de admisi\00F3n pero que ya cuentan con c\00F3digo SISS, realizar los tr\00E1mites de suspensi\00F3n de estudios, retiro de carrera y atenci\00F3n de estudiantes extranjeros.</li></ul>', '2024-12-12T08:00:59'),
    (166, '907ib4e8-0856-4aad-b3aa-747e2dba76d9', '945ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Responsable de Reincorporaciones', U&'<ul><li>Ejecutar las solicitudes o tr\00E1mites de reincorporaci\00F3n a una carrera o programa de estudiantes de la UMSS.</li></ul>', '2024-12-12T08:00:59'),
    (167, '908ib4e8-0856-4aad-b3aa-747e2dba76d9', '945ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Responsable de Certificaci\00F3n de Notas', U&'<ul><li>Ejecutar los tr\00E1mites de certificaci\00F3n de notas de estudiantes de la UMSS y gestionar el correcto registro de notas de estudiantes de otras universidades del Sistema Universitario Boliviano (SUB) que opten por la modalidad de titulaci\00F3n mediante el Programa de Titulaci\00F3n Alternativa y Graduaci\00F3n (PTAG).</li></ul>', '2024-12-12T08:00:59'),
    (168, '909ib4e8-0856-4aad-b3aa-747e2dba76d9', '945ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Responsable de Certificaci\00F3n de Conclusi\00F3n de Plan de Estudios', U&'<ul><li>Asesorar y ejecutar el tr\00E1mite de emisi\00F3n de certificado de conclusi\00F3n de plan de estudios de las diferentes Unidades Acad\00E9micas \2013 Administrativas de la UMSS y gestionar el proceso de examen de gracia.</li></ul>', '2024-12-12T08:00:59'),
    (169, '910ib4e8-0856-4aad-b3aa-747e2dba76d9', '945ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Responsable de Certificaciones de Estudios', U&'<ul><li>Ejecutar los tr\00E1mites de certificaci\00F3n de estudios de estudiantes de las diferentes unidades facultativas de la UMSS.</li></ul>', '2024-12-12T08:00:59'),
    (170, '911ib4e8-0856-4aad-b3aa-747e2dba76d9', '945ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Responsable de Informes T\00E9cnicos', U&'<ul><li>Analizar, evaluar y emitir informes t\00E9cnicos de Asesor\00EDa Legal, claustros de carrera, facultativos y universitarios, auxiliaturas, fondos IDH e informes varios a solicitud de las diferentes Unidades Acad\00E9micas.</li></ul>', '2024-12-12T08:00:59'),
    (171, '912ib4e8-0856-4aad-b3aa-747e2dba76d9', '945ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Responsable de Archivo F\00EDsico', U&'<ul><li>Apoyar al Departamento de Registros e Inscripciones con trabajo de resguardo y archivo de los expedientes de los estudiantes y documentaci\00F3n relacionada con la actividad del R\00E9gimen Estudiantil (admis\00F3n, seguimiento, control, conclusi\00F3n de estudios y planillas de notas), generada por las Unidades Acad\00E9micas-Administrativas de la UMSS y as\00ED garantizar la organizaci\00F3n y conservaci\00F3n de la documentaci\00F3n generada en este Departamento.</li></ul>', '2024-12-12T08:00:59'),
    (172, '913ib4e8-0856-4aad-b3aa-747e2dba76d9', '945ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'Responsable de Archivo Digital', U&'<ul><li>Implementar, actualizar, mantener y digitalizar los expedientes estudiantiles y otros documentos generados por las unidades Acad\00E9mico \2013 Administrativas, generando un respaldo digital de todo el archivo f\00EDsico del Departamento de Registros e Inscripciones, para facilitar el acceso a la informaci\00F3n en custodia.</li></ul>', '2024-12-12T08:00:59'),
    (173, '914ib4e8-0856-4aad-b3aa-747e2dba76d9', '945ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Apoyo en Recepci\00F3n de Documentaci\00F3n de Reincorporaci\00F3n', U&'<ul><li>Apoyar en la recepci\00F3n y verificaci\00F3n de la documentaci\00F3n solicitada por el Responsable de Reincorporaciones.</li></ul>', '2024-12-12T08:00:59'),
    (174, '915ib4e8-0856-4aad-b3aa-747e2dba76d9', '945ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Apoyo en Recepci\00F3n de Documentos de Certificados de Notas y Conclusi\00F3n de Plan de Estudios', U&'<ul><li>Apoyar en la recepci\00F3n y verificaci\00F3n de la documentaci\00F3n solicitada por los Responsables de Certificado de Notas y Certificado de Conclusi\00F3n de Plan de Estudios.</li></ul>', '2024-12-12T08:00:59'),

    (175, '916ib4e8-0856-4aad-b3aa-747e2dba76d9', '946ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'Listado de Tr\00E1mites Administrativos/Acad\00E9micos para realizar en la DPA', U&'En esta p\00E1gina se muestra el listado de tr\00E1mites m\00E1s habituales que otras Unidades Acad\00E9micas o personas relacionadas con la Universidad Mayor de San Sim\00F3n (UMSS) realizar\00E1n con la Direcci\00F3n de Planificaci\00F3n Acad\00E9mica (DPA) y sus diferentes Departamentos.', '2024-12-12T08:00:59'),

    (176, '917ib4e8-0856-4aad-b3aa-747e2dba76d9', '947ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '', U&'El Sistema de Seguimiento de Tr\00E1mites (SITRA) de la Universidad Mayor de San Sim\00F3n (UMSS) es una plataforma para que las unidades facultativas y usuarios en general puedan monitorear el estado de tr\00E1mites acad\00E9micos y administrativos, de tal manera de supervisar el flujo y tiempo de duraci\00F3n de cada tr\00E1mite.', '2024-12-12T08:00:59'),

    (177, '918ib4e8-0856-4aad-b3aa-747e2dba76d9', '948ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', '', U&'El sistema (GAIA) de la Universidad Mayor de San Sim\00F3n (UMSS) es una plataforma para que las unidades facultativas puedan gestionar y administrar informaci\00F3n acad\00E9mica.', '2024-12-12T08:00:59'),

    (178, '919ib4e8-0856-4aad-b3aa-747e2dba76d9', '949ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'DIRECCI\00D3N DE PLANIFICACI\00D3N ACAD\00C9MICA - DPA', U&'Av. Oquendo y Jord\00E1n, Zona Las Cuadras<br>Edificio Vicerrectorado Tercer Piso<br>Cochabamba - Bolivia<br>Correo electr\00F3nico: dpa@umss.edu.bo<br>Tel: (591) 4 - 4232970, 4-4221393', '2024-12-12T08:00:59'),
    (179, '920ib4e8-0856-4aad-b3aa-747e2dba76d9', '949ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'DEPARTAMENTO DE DESARROLLO CURRICULAR - DDC', U&'Direcci\00F3n: Oquendo y Jord\00E1n, edificio del Vicerrectorado Segundo Piso.<br>Correo electr\00F3nico: ddc_dpa@umss.edu.bo<br>Tel. Ip: 40525 - 40258', '2024-12-12T08:00:59'),
    (180, '921ib4e8-0856-4aad-b3aa-747e2dba76d9', '949ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'DEPARTAMENTO DE COORDINACI\00D3N ACAD\00C9MICA - DCA', U&'Direcci\00F3n: Oquendo y Jord\00E1n, edificio del Vicerrectorado Tercer Piso.<br>Correo electr\00F3nico: juan.rojas@umss.edu.bo<br>Tel. IP: 40531 - 40530', '2024-12-12T08:00:59'),
    (181, '922ib4e8-0856-4aad-b3aa-747e2dba76d9', '949ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'DEPARTAMENTO DE SEGUIMIENTO Y ADMINISTRACI\00D3N DE INFORMACI\00D3N ACAD\00C9MICA DSyAiA', U&'Direcci\00F3n: Oquendo y Jord\00E1n, edificio del Vicerrectorado Segundo Piso.<br>Correo electr\00F3nico: dpa_sye@umss.edu.bo<br>Tel. IP: 40324 - 40347', '2024-12-12T08:00:59'),
    (182, '923ib4e8-0856-4aad-b3aa-747e2dba76d9', '949ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'DEPARTAMENTO DE PERSONAL ACAD\00C9MICO - PA', U&'Direcci\00F3n: Oquendo y Jord\00E1n, edificio del Vicerrectorado Tercer Piso.<br>Correo electr\00F3nico: r.gutierrez@umss.edu.bo<br>Tel. IP: 40443 - 40408', '2024-12-12T08:00:59'),
    (183, '924ib4e8-0856-4aad-b3aa-747e2dba76d9', '949ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', 'DEPARTAMENTO DE REGISTROS E INSCRIPCIONES - DREI', U&'Direcci\00F3n: Oquendo y Jord\00E1n, edificio del Vicerrectorado Planta Baja<br>Correo electr\00F3nico: drei@umss.edu.bo<br>Tel. IP: 40531 - 40530<br>Tel. fijo: (591) 4-4666631 Fax: (591) 4-4667123<br>P\00E1gina web: www.drei.umss.edu.bo', '2024-12-12T08:00:59'),
    (184, '925ib4e8-0856-4aad-b3aa-747e2dba76d9', '949ib4e8-0856-4aad-b3aa-747e2dba76d9', 'a5f6a74c-3004-4c03-8fcb-3a7fe9d19b49', U&'PROGRAMA DE TITULACI\00D3N ALTERNATIVA Y GRADUACI\00D3N', U&'Direcci\00F3n: Oquendo y Jord\00E1n, edificio del Vicerrectorado Tercer Piso.<br>Correo electr\00F3nico: ptang@umss.edu.bo<br>Tel. IP: 40358 - 40313<br>Tel. fijo: (591) 4-4666025; (591) 4-4255605, Fax (591) 4- 4543823<br>P\00E1gina web: www.ptag.umss.edu.bo', '2024-12-12T08:00:59');

insert into article_media (id, uuid, article_id, file_name, number, file_type, file_path) values
	(100, 'a5a0a99c-6280-4b74-a507-12a5a2109cd5', '819ab4e8-0856-4aad-b3aa-747e2dba76d9', 'director-dpa', 1, 'image', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/a5a0a99c-6280-4b74-a507-12a5a2109cd5'),
    (101, 'e7629149-79db-4bc6-85c9-12fa52d35c34', '846ib4e8-0856-4aad-b3aa-747e2dba76d9', 'personal-dca', 1, 'image', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/e7629149-79db-4bc6-85c9-12fa52d35c34'),
    (102, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '855ib4e8-0856-4aad-b3aa-747e2dba76d9', 'personal-dcc', 1, 'image', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'),
    (103, 'f47ac10b-58cc-4372-a567-0e02b2c3d479', '861ib4e8-0856-4aad-b3aa-747e2dba76d9', 'jefe-dpa', 1, 'image', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/f47ac10b-58cc-4372-a567-0e02b2c3d479'),
    (104, '5d9a4559-7c13-4db6-8ea3-8f32a3f5c8e2', '879ib4e8-0856-4aad-b3aa-747e2dba76d9', 'personal-ptang', 1, 'image', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/5d9a4559-7c13-4db6-8ea3-8f32a3f5c8e2'),
    (105, '8f6e9b4a-1d4d-4a4c-9c2a-3e7f8d5c6b1e', '888ib4e8-0856-4aad-b3aa-747e2dba76d9', 'personal-ds', 1, 'image', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/8f6e9b4a-1d4d-4a4c-9c2a-3e7f8d5c6b1e'),
    (106, '1b9d6bcd-bbfd-4b2d-9b5d-ab8dfbbd4bed', '896ib4e8-0856-4aad-b3aa-747e2dba76d9', 'personal-dri', 1, 'image', 'https://devpws.cs.umss.edu.bo/api/v1/images/posts/1b9d6bcd-bbfd-4b2d-9b5d-ab8dfbbd4bed');
