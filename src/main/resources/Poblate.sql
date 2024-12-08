-- Insertar roles
INSERT INTO roles (nombre) VALUES
                               ('Jefe de Departamento de Informática y Sistemas'),
                               ('Secretaria'),
                               ('Portero Mensajero'),
                               ('Auxiliares de Jefatura'),
                               ('Docentes de Pizarra'),
                               ('Docentes a Dedicación Exclusiva'),
                               ('Auxiliares de Pizarra'),
                               ('Administrador de Laboratorio'),
                               ('Administrador de Laboratorio de Cómputo'),
                               ('Administrador de Laboratorio de Desarrollo'),
                               ('Administrador de Laboratorio de Mantenimiento de Hardware'),
                               ('Administrador de Laboratorio de Software'),
                               ('Auxiliar de Terminal'),
                               ('Director de Carrera de Ingeniería de Sistemas'),
                               ('Director de Carrera de Ingeniería Informática'),
                               ('Auxiliar Ad-Honorem');

INSERT INTO usuarios (nombre, email, password, disponibilidad, carga_trabajo) VALUES
                                                                                  ('Juan Pérez', 'softcraft2024@gmail.com', '12345', 'Disponible', 0),
                                                                                  ('Ana Gómez', '202201712@est.umss.edu', '12345', 'Disponible', 2),
                                                                                  ('Carlos Ruiz', 'gaspararmando44@gmail.com', '12345', 'Disponible', 5),
                                                                                  ('Laura Fernández', 'noimporta@example.com', '12345', 'No Disponible', 0),
                                                                                  ('Pedro García', 'claronoimporta@example.com', '12345', 'Disponible', 3);

select * from roles;
select * from usuarios;

INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES
                                                    (1, 20), -- Juan Pérez como Director de Carrera de Ingeniería Informática
                                                    (2, 21),  -- Ana Gómez como Jefa de Departamento de Informática y Sistemas
                                                    (3, 15),  -- Carlos Ruiz como Administrador de Laboratorio de Cómputo
                                                    (4, 8),  -- Laura Fernández como Secretaria
                                                    (5, 22);  -- Pedro García como Auxiliar Ad-Honorem


INSERT INTO permisos (nombre, descripcion) VALUES
-- Componente 1
('Control de Asistencia', 'Permiso para registrar asistencia del personal'),
('Control de Acceso a Laboratorios', 'Permiso para gestionar accesos a laboratorios'),

-- Componente 2
('Planificación de Actividades', 'Permiso para planificar actividades para el personal'),
('Evaluación de Superiores', 'Permiso para evaluar personal a cargo'),
('Gestión de Procedimientos', 'Permiso para gestionar procedimientos de no cumplimiento'),

-- Componente 3
('Gestión de Roles', 'Permiso para gestionar roles y permisos'),
('Registro de Actividades', 'Permiso para registrar actividades en el sistema'),

-- Componente 4
('Gestión de Trabajos de Mantenimiento', 'Permiso para gestionar trabajos de mantenimiento internos y externos'),

-- Componente 5
('Gestión de Reservas', 'Permiso para gestionar reservas y uso de laboratorios'),
('Gestión de Componentes', 'Permiso para gestionar pedidos de componentes');


INSERT INTO roles_permisos (rol_id, permiso_id) VALUES
-- Permisos para Directores
(20, 1), (20, 2), (20, 3), (20, 4), (20, 5), (20, 6), (20, 7),
(19, 1), (19, 2), (19, 3), (19, 4), (19, 5), (19, 6), (19, 7),

-- Permisos para Administradores de Laboratorio
(14, 2), (14, 8), (14, 9),
(15, 10), (16, 10), (17, 10), (18, 10),

-- Permisos para Jefes de Departamento
(7, 1), (7, 2), (7, 3), (7, 6),

-- Permisos para Docentes
(11, 1), (11, 3),
(12, 1), (12, 3);

