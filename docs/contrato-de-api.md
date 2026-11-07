# Contrato de API REST

Base local: `http://localhost:8080`

Formato: JSON

Documentación interactiva: `/swagger-ui.html`

Especificación OpenAPI: `/api-docs`

## Estudiantes

| Método | Ruta | Uso | Respuestas |
|---|---|---|---|
| `POST` | `/api/estudiantes` | Registrar estudiante y perfil opcional | `201`, `400`, `409` |
| `GET` | `/api/estudiantes` | Listar estudiantes | `200` |
| `GET` | `/api/estudiantes/{idEstudiante}` | Consultar estudiante | `200`, `404` |
| `GET` | `/api/estudiantes/{idEstudiante}/docentes` | Consultar docentes asociados a clases reseñadas | `200`, `404` |

Ejemplo de registro:

```json
{
  "codigoInstitucional": "EST-2023001",
  "nombre": "Juliana Restrepo",
  "correoInstitucional": "juliana@correo.itm.edu.co",
  "programaAcademico": "Ingeniería de Sistemas",
  "perfil": {
    "telefono": "3001234567",
    "fotoUrl": "https://example.edu/fotos/estudiante.jpg"
  }
}
```

## Docentes

| Método | Ruta | Uso | Respuestas |
|---|---|---|---|
| `POST` | `/api/docentes` | Registrar docente | `201`, `400`, `409` |
| `GET` | `/api/docentes` | Listar docentes | `200` |
| `GET` | `/api/docentes/{idDocente}` | Consultar docente | `200`, `404` |

```json
{
  "nombre": "Carlos Andrés Gómez",
  "departamentoOFacultad": "Facultad de Ingeniería",
  "correoInstitucional": "carlos.gomez@itm.edu.co"
}
```

## Materias

| Método | Ruta | Uso | Respuestas |
|---|---|---|---|
| `POST` | `/api/materias` | Registrar materia | `201`, `400`, `409` |
| `GET` | `/api/materias` | Listar materias | `200` |
| `GET` | `/api/materias/{idMateria}` | Consultar materia | `200`, `404` |

```json
{
  "codigoMateria": "MAT-BD-01",
  "nombreMateria": "Bases de Datos",
  "facultad": "Facultad de Ingeniería"
}
```

## Clases y asignación docente-materia

Cada clase representa una oferta de una materia dictada por un docente. Crear una clase asigna ambos:

| Método | Ruta | Uso | Respuestas |
|---|---|---|---|
| `POST` | `/api/clases` | Asignar docente a materia | `201`, `400`, `404` |
| `GET` | `/api/clases` | Listar clases | `200` |
| `GET` | `/api/clases?idDocente={id}` | Filtrar por docente | `200`, `404` |
| `GET` | `/api/clases?idMateria={id}` | Filtrar por materia | `200`, `404` |
| `GET` | `/api/clases/{idClase}` | Consultar clase | `200`, `404` |

```json
{
  "jornada": "Diurna",
  "codigo": "BD-01",
  "horario": "Lunes y miércoles 8:00-10:00",
  "idDocente": 1,
  "idMateria": 1
}
```

## Reseñas

| Método | Ruta | Uso | Respuestas |
|---|---|---|---|
| `POST` | `/api/resenas` | Crear y publicar reseña | `201`, `400`, `404`, `409` |
| `GET` | `/api/resenas` | Listar reseñas | `200` |
| `GET` | `/api/resenas?idEstudiante={id}` | Filtrar por estudiante | `200` |
| `GET` | `/api/resenas?idDocente={id}` | Filtrar por docente | `200`, `404` |
| `GET` | `/api/resenas?idClase={id}` | Filtrar por clase | `200` |
| `GET` | `/api/resenas?idMateria={id}` | Filtrar por materia | `200` |
| `GET` | `/api/resenas/{idResena}` | Consultar una reseña | `200`, `404` |
| `PUT` | `/api/resenas/{idResena}` | Editar reseña existente | `200`, `400`, `404` |
| `DELETE` | `/api/resenas/{idResena}` | Eliminar reseña | `204`, `404` |

Solo se permite un filtro por solicitud. La calificación general es obligatoria y debe estar entre `0` y `5`; metodología y exigencia son opcionales, también de `0` a `5`. `idEstudiante` es obligatorio. Debe indicarse exactamente uno de `idClase` o `idMateria`, y la referencia debe existir.

```json
{
  "calificacionGeneral": 4.5,
  "metodologiaEnsenansa": 4.0,
  "nivelExigencia": 3.5,
  "comentario": "El docente explica claramente los conceptos.",
  "anonima": true,
  "idEstudiante": 1,
  "idClase": 1,
  "idMateria": null
}
```

Las reseñas anónimas no incluyen `idEstudiante` en las respuestas de consulta. Al crear una reseña, el backend devuelve al autor el resultado de su solicitud. La edición comprueba que el identificador de estudiante del cuerpo corresponda al autor guardado. **Esta comprobación no reemplaza autenticación**: el proyecto todavía no implementa inicio de sesión ni tokens, por lo que no debe exponerse como API segura en producción.

## Formato de errores

Los errores tienen esta forma:

```json
{
  "timestamp": "2026-11-03T19:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Debe indicar exactamente una relación entre idClase e idMateria.",
  "path": "/api/resenas"
}
```

- `400`: validación o regla de negocio incumplida.
- `404`: entidad solicitada o referencia relacionada inexistente.
- `409`: valor único duplicado o conflicto con integridad de datos.
- `500`: error inesperado; el detalle se registra en el log del servidor, no se filtra al cliente.

## Persistencia y relaciones

El CRUD de reseñas usa JPA. Las consultas de listado y filtros de reseñas usan `JdbcTemplate` con columnas explícitas (sin `SELECT *`). `JdbcTemplate` administra y libera conexiones, sentencias y resultados mediante el `DataSource` de Spring; no se debe cerrar manualmente una conexión administrada por el framework.

El modelo persistente refleja `Estudiante 1:N Reseña`, `Docente 1:N Clase`, `Materia 1:N Clase`, `Clase 1:N Reseña`, y las relaciones `N:1` correspondientes. El perfil opcional del estudiante establece una relación `1:1`.
