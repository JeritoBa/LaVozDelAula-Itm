ALTER TABLE materia ADD COLUMN IF NOT EXISTS codigo_materia VARCHAR(50);

UPDATE materia
SET codigo_materia = 'MAT-' || id_materia
WHERE codigo_materia IS NULL;

ALTER TABLE materia ALTER COLUMN codigo_materia SET NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS uq_materia_codigo_materia ON materia(codigo_materia);
