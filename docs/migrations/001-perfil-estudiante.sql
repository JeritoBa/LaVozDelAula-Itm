CREATE TABLE IF NOT EXISTS perfil_estudiante (
    id_perfil_estudiante SERIAL PRIMARY KEY,
    id_estudiante        INTEGER NOT NULL UNIQUE REFERENCES estudiante(id_estudiante) ON DELETE CASCADE,
    telefono             VARCHAR(30),
    foto_url             VARCHAR(500)
);
