CREATE TABLE estudiante (
    id_estudiante        SERIAL PRIMARY KEY,
    codigo_institucional  VARCHAR(50)  NOT NULL UNIQUE,
    nombre                VARCHAR(150) NOT NULL,
    correo_institucional  VARCHAR(150) NOT NULL UNIQUE,
    programa_academico    VARCHAR(150)
);

CREATE TABLE docente (
    id_docente             SERIAL PRIMARY KEY,
    nombre                  VARCHAR(150) NOT NULL,
    departamento_o_facultad VARCHAR(150),
    correo_institucional    VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE materia (
    id_materia     SERIAL PRIMARY KEY,
    nombre_materia VARCHAR(150) NOT NULL,
    facultad       VARCHAR(150)
);

CREATE TABLE clase (
    id_clase   SERIAL PRIMARY KEY,
    jornada    VARCHAR(50),
    codigo     VARCHAR(50) NOT NULL,
    horario    VARCHAR(100),
    id_docente INTEGER NOT NULL REFERENCES docente(id_docente),
    id_materia INTEGER NOT NULL REFERENCES materia(id_materia)
);

CREATE TABLE resena (
    id_resena             SERIAL PRIMARY KEY,
    calificacion_general  NUMERIC(3,1) NOT NULL,
    metodologia_ensenansa NUMERIC(3,1),
    nivel_exigencia       NUMERIC(3,1),
    comentario            TEXT,
    fecha_creacion        TIMESTAMP NOT NULL DEFAULT NOW(),
    anonima               BOOLEAN NOT NULL DEFAULT FALSE,

    id_estudiante         INTEGER NOT NULL REFERENCES estudiante(id_estudiante),
    id_clase              INTEGER REFERENCES clase(id_clase),
    id_materia            INTEGER REFERENCES materia(id_materia),

    -- Exactamente uno de los dos debe ser NULL
    CONSTRAINT chk_clase_xor_materia CHECK (
        (id_clase IS NULL AND id_materia IS NOT NULL)
        OR
        (id_clase IS NOT NULL AND id_materia IS NULL)
    )
);

-- Búsquedas de reseñas por estudiante
CREATE INDEX idx_resena_id_estudiante ON resena(id_estudiante);

-- Búsquedas de reseñas por clase
CREATE INDEX idx_resena_id_clase ON resena(id_clase);

-- Búsquedas de reseñas por materia
CREATE INDEX idx_resena_id_materia ON resena(id_materia);

-- Ordenar/filtrar reseñas por fecha (ej. más recientes primero)
CREATE INDEX idx_resena_fecha_creacion ON resena(fecha_creacion DESC);

-- Búsquedas de clases por docente
CREATE INDEX idx_clase_id_docente ON clase(id_docente);

-- Búsquedas de clases por materia
CREATE INDEX idx_clase_id_materia ON clase(id_materia);

-- Búsqueda de clase por código (ej. filtrar por código de sección)
CREATE INDEX idx_clase_codigo ON clase(codigo);

-- Búsqueda de materias por facultad
CREATE INDEX idx_materia_facultad ON materia(facultad);

-- Búsqueda de docentes por departamento/facultad
CREATE INDEX idx_docente_departamento ON docente(departamento_o_facultad);