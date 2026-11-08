# Casos de uso — La Voz del Aula

## Propósito y alcance

Este documento concreta `requisitos.md` y `contrato-de-api.md` en flujos verificables. Describe el comportamiento de la API; no implica que todos los flujos estén disponibles en la interfaz web.

**Actores:** Estudiante (consulta y administra reseñas); Administrador institucional (registra datos académicos); Visitante de la API (consulta recursos). El contrato no define roles ni autenticación; los actores administrativos son una interpretación funcional, no autorización ya implementada.

## Casos

### CU-01 — Registrar estudiante
- **Actor:** Administrador institucional.
- **Precondiciones:** datos requeridos válidos y código/correo no conflictivos.
- **Flujo:** envía datos personales y académicos, con perfil opcional; el sistema valida y persiste.
- **Alternativas:** datos inválidos → `400`; duplicidad → `409`.
- **Postcondición:** estudiante registrado.
- **Trazabilidad:** RF01; `POST /api/estudiantes`.

### CU-02 — Consultar estudiante y docentes relacionados
- **Actor:** Visitante de la API.
- **Precondiciones:** estudiante existente para consulta por ID.
- **Flujo:** lista o consulta estudiantes y solicita docentes asociados a clases reseñadas.
- **Alternativas:** ID inexistente → `404`.
- **Postcondición:** datos consultados sin modificación.
- **Trazabilidad:** RF02; `GET /api/estudiantes`, `GET /api/estudiantes/{idEstudiante}`, `GET /api/estudiantes/{idEstudiante}/docentes`.

### CU-03 — Registrar y consultar docente
- **Actor:** Administrador institucional (registro); Visitante (consulta).
- **Precondiciones:** datos de docente válidos para registrar.
- **Flujo:** registra, lista o consulta docentes.
- **Alternativas:** entrada inválida → `400`; conflicto → `409`; ID inexistente → `404`.
- **Postcondición:** docente disponible para asociar a una clase.
- **Trazabilidad:** RF03–RF04; `POST /api/docentes`, `GET /api/docentes`, `GET /api/docentes/{idDocente}`.

### CU-04 — Registrar y consultar materia
- **Actor:** Administrador institucional (registro); Visitante (consulta).
- **Precondiciones:** código y nombre válidos.
- **Flujo:** registra, lista o consulta materias.
- **Alternativas:** entrada inválida → `400`; conflicto → `409`; ID inexistente → `404`.
- **Postcondición:** materia disponible para asignación.
- **Trazabilidad:** RF05; `POST /api/materias`, `GET /api/materias`, `GET /api/materias/{idMateria}`.

### CU-05 — Asignar docente a materia mediante una clase
- **Actor:** Administrador institucional.
- **Precondiciones:** docente y materia existentes.
- **Flujo:** registra la oferta de clase con jornada, código/horario y referencias.
- **Alternativas:** datos inválidos → `400`; referencia inexistente → `404`.
- **Postcondición:** clase consultable por ID, docente o materia.
- **Trazabilidad:** RF06; `POST /api/clases`, `GET /api/clases` y filtros, `GET /api/clases/{idClase}`.

### CU-06 — Crear reseña
- **Actor:** Estudiante.
- **Precondiciones:** estudiante existente; calificación general de 0 a 5; exactamente una referencia existente a clase o materia.
- **Flujo:** envía calificaciones, comentario opcional, anonimato e ID de estudiante; el sistema valida y registra la fecha.
- **Alternativas:** cuerpo, calificación o relación inválida → `400`; referencia inexistente → `404`; conflicto de integridad → `409`.
- **Postcondición:** reseña persistida y publicada por la API actual.
- **Trazabilidad:** RF07, RF12–RF17; `POST /api/resenas`.

### CU-07 — Consultar reseñas
- **Actor:** Visitante de la API.
- **Precondiciones:** docente existente para filtro por docente.
- **Flujo:** lista, filtra por un único estudiante/docente/clase/materia o consulta por ID.
- **Alternativas:** múltiples filtros → `400`; docente o reseña inexistente → `404`.
- **Postcondición:** resultados sin modificar; consultas ocultan el ID del autor de reseñas anónimas.
- **Trazabilidad:** RF08–RF10; `GET /api/resenas`, `GET /api/resenas/{idResena}`.

### CU-08 — Editar reseña propia
- **Actor:** Estudiante autor.
- **Precondiciones:** reseña existente; ID del estudiante recibido coincide con el autor; datos y relación válidos.
- **Flujo:** envía la reseña actualizada; el sistema verifica autoría y persiste cambios.
- **Alternativas:** datos inválidos u otro autor → `400`; referencia o reseña inexistente → `404`.
- **Postcondición:** reseña actualizada.
- **Trazabilidad:** RF11; `PUT /api/resenas/{idResena}`.
- **Riesgo:** comparar el ID del cuerpo no equivale a autorizar mediante identidad autenticada; falta autenticación confiable.

### CU-09 — Eliminar reseña
- **Actor:** No definido actualmente.
- **Precondiciones:** reseña existente.
- **Flujo:** solicita eliminar por ID.
- **Alternativas:** ID inexistente → `404`.
- **Postcondición:** eliminación y respuesta `204`.
- **Trazabilidad:** `DELETE /api/resenas/{idResena}`.
- **Pendiente:** definir actor, autorización y política de moderación antes de producción.

## Matriz de trazabilidad

| Requisito | Casos | Situación observada |
|---|---|---|
| RF01–RF06 | CU-01–CU-05 | Operaciones disponibles en la API. |
| RF07–RF17 | CU-06–CU-08 | API gestiona reseñas y anonimato; autenticación/moderación no quedan demostradas. |
| RNF02–RNF03 | CU-06–CU-09 | Definir identidad, autorización y privacidad antes de producción. |
| RNF10 | CU-09 | Política y mecanismo de moderación no definidos en el contrato. |

La disponibilidad de una ruta REST no garantiza que el usuario pueda completar el flujo desde el frontend; validar su cobertura aparte.
