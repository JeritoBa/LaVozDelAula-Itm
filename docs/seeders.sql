-- =========================================================
-- Seeder: 1 fila por entidad
-- =========================================================

INSERT INTO estudiante (codigo_institucional, nombre, correo_institucional, programa_academico)
VALUES ('EST-2023001', 'Juliana Restrepo', 'juliana.restrepo@universidad.edu.co', 'Ingeniería de Sistemas');

INSERT INTO docente (nombre, departamento_o_facultad, correo_institucional)
VALUES ('Carlos Andrés Gómez', 'Facultad de Ingeniería', 'carlos.gomez@universidad.edu.co');

INSERT INTO materia (nombre_materia, facultad)
VALUES ('Bases de Datos', 'Facultad de Ingeniería');

INSERT INTO clase (jornada, codigo, horario, id_docente, id_materia)
VALUES ('Diurna', 'BD-01', 'Lunes y Miércoles 8:00-10:00', 1, 1);

INSERT INTO resena (
    calificacion_general,
    metodologia_ensenansa,
    nivel_exigencia,
    comentario,
    anonima,
    id_estudiante,
    id_clase,
    id_materia
)
VALUES (
    4.5,
    4.0,
    3.5,
    'Excelente clase, el docente explica muy bien los conceptos de bases de datos.',
    FALSE,
    1,
    1,
    NULL
);