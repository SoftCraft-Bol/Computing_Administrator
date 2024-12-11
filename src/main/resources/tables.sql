CREATE DATABASE sistema_mantenimiento2;

USE sistema_mantenimiento2;

-- Tabla de roles
CREATE TABLE roles (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       nombre VARCHAR(100) NOT NULL UNIQUE
);

-- Tabla de usuarios
CREATE TABLE usuarios (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          email VARCHAR(100) UNIQUE NOT NULL,
                          password VARCHAR(255) NOT NULL,
                          disponibilidad ENUM('Disponible', 'No Disponible') NOT NULL DEFAULT 'Disponible',
                          carga_trabajo INT DEFAULT 0
);

-- Relación muchos a muchos: usuarios y roles
CREATE TABLE usuarios_roles (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                usuario_id INT NOT NULL,
                                rol_id INT NOT NULL,
                                FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
                                FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE CASCADE,
                                UNIQUE (usuario_id, rol_id)
);

-- Tabla de permisos
CREATE TABLE permisos (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL UNIQUE,
                          descripcion TEXT
);

-- Relación muchos a muchos: roles y permisos
CREATE TABLE roles_permisos (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                rol_id INT NOT NULL,
                                permiso_id INT NOT NULL,
                                FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE CASCADE,
                                FOREIGN KEY (permiso_id) REFERENCES permisos(id) ON DELETE CASCADE
);

-- Tabla de trabajos de mantenimiento
CREATE TABLE trabajos_mantenimiento (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        tipo_trabajo ENUM('Interno', 'Externo') NOT NULL,
                                        fecha_inicio DATE NOT NULL,
                                        fecha_fin DATE,
                                        usuarios_id INT,
                                        cliente VARCHAR(100) NOT NULL,
                                        descripcion_problema TEXT,
                                        descripcion_servicio TEXT,
                                        estado ENUM('Pendiente', 'En Progreso', 'Completado', 'Cancelado') NOT NULL DEFAULT 'Pendiente',
                                        costo DECIMAL(10, 2),
                                        observaciones TEXT,
                                        nombre_responsable VARCHAR(255),
                                        FOREIGN KEY (usuarios_id) REFERENCES usuarios(id) ON DELETE SET NULL
);

-- Tabla para historial de trabajos
CREATE TABLE historial_trabajos (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    trabajo_id INT NOT NULL,
                                    fecha_inicio DATE,
                                    fecha_fin DATE,
                                    usuarios_id INT,
                                    estado ENUM('Pendiente', 'En Progreso', 'Completado', 'Cancelado') NOT NULL DEFAULT 'Pendiente',
                                    FOREIGN KEY (trabajo_id) REFERENCES trabajos_mantenimiento(id) ON DELETE CASCADE,
                                    FOREIGN KEY (usuarios_id) REFERENCES usuarios(id) ON DELETE SET NULL
);

-- Tabla de componentes
CREATE TABLE componentes (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             nombre VARCHAR(100) NOT NULL,
                             cantidad_en_stock INT NOT NULL,
                             precio DECIMAL(10, 2) NOT NULL
);

-- Anticipación de pedidos de componentes
CREATE TABLE pedidos (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         componente_id INT NOT NULL,
                         cantidad INT NOT NULL,
                         fecha_pedido DATE NOT NULL,
                         fecha_entrega DATE,
                         estado ENUM('Pendiente', 'En Proceso', 'Entregado') NOT NULL DEFAULT 'Pendiente',
                         FOREIGN KEY (componente_id) REFERENCES componentes(id) ON DELETE CASCADE
);

-- Tabla de laboratorios
CREATE TABLE laboratorio (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             nombre VARCHAR(50) NOT NULL,
                             capacidad INT NOT NULL,
                             aula VARCHAR(250) NOT NULL
);

-- Reservas de laboratorio
CREATE TABLE reservas (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          laboratorio_id INT NOT NULL,
                          usuario_id INT NOT NULL,
                          fecha DATE NOT NULL,
                          hora TIME NOT NULL,
                          topico VARCHAR(250) NOT NULL,
                          FOREIGN KEY (laboratorio_id) REFERENCES laboratorio(id) ON DELETE CASCADE,
                          FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Tabla de materias
CREATE TABLE materia (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         nombre VARCHAR(50) NOT NULL
);

-- Relación entre laboratorios y materias
CREATE TABLE laboratorio_materia (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     laboratorio_id INT NOT NULL,
                                     materia_id INT NOT NULL,
                                     capacidad_asignada INT NOT NULL,
                                     FOREIGN KEY (laboratorio_id) REFERENCES laboratorio(id) ON DELETE CASCADE,
                                     FOREIGN KEY (materia_id) REFERENCES materia(id) ON DELETE CASCADE
);

select * from asistencia;
-- Control de asistencia del personal
CREATE TABLE asistencia (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            usuario_id INT NOT NULL,
                            fecha DATE NOT NULL,
                            hora_entrada TIME NOT NULL,
                            hora_salida TIME,
                            estado ENUM('Presente', 'Ausente', 'Tarde') NOT NULL DEFAULT 'Presente',
                            observaciones TEXT,
                            FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Control de acceso a laboratorios
CREATE TABLE acceso_laboratorio (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    laboratorio_id INT NOT NULL,
                                    usuario_id INT NOT NULL,
                                    fecha_acceso DATE NOT NULL,
                                    hora_entrada TIME NOT NULL,
                                    hora_salida TIME,
                                    motivo_acceso TEXT,
                                    FOREIGN KEY (laboratorio_id) REFERENCES laboratorio(id) ON DELETE CASCADE,
                                    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);
-- Si ya existe esta columna eliminar con la siguiente:
ALTER TABLE roles DROP COLUMN id_rol_padre;
-- Si no existe esa columna insertar directamente
ALTER TABLE roles
    ADD COLUMN id_rol_padre INT NULL,
    ADD CONSTRAINT fk_roles_rol_padre FOREIGN KEY (id_rol_padre) REFERENCES roles(id) ON DELETE SET NULL;

-- Para que la columna se pueda aceptar null
ALTER TABLE asistencia MODIFY observaciones TEXT NULL;

-- Nuevas tablas
CREATE TABLE actividades (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             nombre VARCHAR(100) NOT NULL,
                             descripcion TEXT NOT NULL,
                             fecha_inicio DATE NOT NULL,
                             fecha_fin DATE,
                             estado ENUM('Planificado', 'En Progreso', 'Finalizado', 'Cancelado') NOT NULL DEFAULT 'Planificado',
                             responsable_id INT,
                             FOREIGN KEY (responsable_id) REFERENCES usuarios(id) ON DELETE SET NULL
);

CREATE TABLE evaluaciones (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              actividad_id INT,
                              evaluador_id INT NOT NULL,
                              evaluado_id INT NOT NULL,
                              fecha DATE NOT NULL,
                              puntuacion INT NOT NULL CHECK (puntuacion BETWEEN 1 AND 5),
                              comentarios TEXT,
                              FOREIGN KEY (actividad_id) REFERENCES actividades(id) ON DELETE CASCADE,
                              FOREIGN KEY (evaluador_id) REFERENCES usuarios(id) ON DELETE CASCADE,
                              FOREIGN KEY (evaluado_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE procedimientos_no_cumplimiento (
                                                id INT AUTO_INCREMENT PRIMARY KEY,
                                                actividad_id INT NOT NULL,
                                                usuario_id INT NOT NULL,
                                                descripcion TEXT NOT NULL,
                                                fecha_inicio DATE NOT NULL,
                                                fecha_fin DATE,
                                                estado ENUM('Abierto', 'En Progreso', 'Resuelto', 'Cerrado') NOT NULL DEFAULT 'Abierto',
                                                FOREIGN KEY (actividad_id) REFERENCES actividades(id) ON DELETE CASCADE,
                                                FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

ALTER TABLE usuarios ADD COLUMN es_superior BOOLEAN NOT NULL DEFAULT FALSE;

INSERT INTO roles (nombre) VALUES ('Supervisor de Actividades'), ('Responsable de Procedimientos');

ALTER TABLE actividades ADD COLUMN departamento_id INT, ADD FOREIGN KEY (departamento_id) REFERENCES departamentos(id);

CREATE TABLE actividad_usuarios (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    actividad_id INT NOT NULL,
                                    usuario_id INT NOT NULL,
                                    FOREIGN KEY (actividad_id) REFERENCES actividades(id) ON DELETE CASCADE,
                                    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);



