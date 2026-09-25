# Contrato de API — Entidad Reseña

**Base URL:** `/api/resenas`
**Formato:** JSON
**Versión:** 1.0

---

## Modelo de datos

```json
{
  "idResena": 1,
  "calificacionGeneral": 4.5,
  "metodologiaEnsenansa": 4.0,
  "nivelExigencia": 3.5,
  "comentario": "Excelente clase, el docente explica muy bien los conceptos.",
  "fechaCreacion": "2026-09-25T14:30:00",
  "anonima": false,
  "idEstudiante": 1,
  "idClase": 1,
  "idMateria": null
}
```

| Campo                 | Tipo             | Obligatorio | Descripción                                              |
|-----------------------|------------------|-------------|------------------------------------------------------------|
| idResena              | integer          | No (autogenerado) | Identificador único de la reseña                     |
| calificacionGeneral   | decimal (1 dec.) | Sí          | Calificación general (ej. 0.0 - 5.0)                        |
| metodologiaEnsenansa  | decimal (1 dec.) | No          | Calificación de metodología de enseñanza                   |
| nivelExigencia        | decimal (1 dec.) | No          | Calificación del nivel de exigencia                         |
| comentario            | string           | No          | Comentario textual del estudiante                          |
| fechaCreacion         | datetime ISO 8601| No (autogenerado) | Fecha y hora de creación de la reseña                |
| anonima               | boolean          | Sí          | Indica si la reseña se muestra de forma anónima            |
| idEstudiante          | integer          | Sí          | FK al estudiante que crea la reseña                         |
| idClase               | integer o null   | Condicional | FK a la clase reseñada                                      |
| idMateria             | integer o null   | Condicional | FK a la materia reseñada                                    |

> **Regla de negocio:** `idClase` e `idMateria` son mutuamente excluyentes. Exactamente uno de los dos debe tener valor; el otro debe ser `null`. Nunca ambos nulos ni ambos con valor.

---

## Endpoints

### 1. Crear una reseña

**`POST /api/resenas`**

**Request body:**

```json
{
  "calificacionGeneral": 4.5,
  "metodologiaEnsenansa": 4.0,
  "nivelExigencia": 3.5,
  "comentario": "Excelente clase.",
  "anonima": false,
  "idEstudiante": 1,
  "idClase": 1,
  "idMateria": null
}
```

**Respuesta exitosa — `201 Created`**

```json
{
  "idResena": 10,
  "calificacionGeneral": 4.5,
  "metodologiaEnsenansa": 4.0,
  "nivelExigencia": 3.5,
  "comentario": "Excelente clase.",
  "fechaCreacion": "2026-09-25T14:30:00",
  "anonima": false,
  "idEstudiante": 1,
  "idClase": 1,
  "idMateria": null
}
```

**Errores posibles:**

| Código | Causa                                                              |
|--------|---------------------------------------------------------------------|
| 400    | `idClase` e `idMateria` ambos nulos, o ambos con valor              |
| 400    | Campos obligatorios faltantes (`calificacionGeneral`, `idEstudiante`, `anonima`) |
| 404    | `idEstudiante`, `idClase` o `idMateria` no existen                  |

---

### 2. Obtener todas las reseñas

**`GET /api/resenas`**

**Query params opcionales (filtros):**

| Param         | Tipo    | Descripción                                  |
|---------------|---------|-----------------------------------------------|
| idEstudiante  | integer | Filtra reseñas de un estudiante específico    |
| idClase       | integer | Filtra reseñas de una clase específica        |
| idMateria     | integer | Filtra reseñas de una materia específica      |

**Ejemplo:** `GET /api/resenas?idClase=1`

**Respuesta exitosa — `200 OK`**

```json
[
  {
    "idResena": 10,
    "calificacionGeneral": 4.5,
    "metodologiaEnsenansa": 4.0,
    "nivelExigencia": 3.5,
    "comentario": "Excelente clase.",
    "fechaCreacion": "2026-09-25T14:30:00",
    "anonima": false,
    "idEstudiante": 1,
    "idClase": 1,
    "idMateria": null
  }
]
```

Si no hay resultados, retorna `200 OK` con arreglo vacío `[]`.

---

### 3. Obtener una reseña por ID

**`GET /api/resenas/{idResena}`**

**Respuesta exitosa — `200 OK`**

```json
{
  "idResena": 10,
  "calificacionGeneral": 4.5,
  "metodologiaEnsenansa": 4.0,
  "nivelExigencia": 3.5,
  "comentario": "Excelente clase.",
  "fechaCreacion": "2026-09-25T14:30:00",
  "anonima": false,
  "idEstudiante": 1,
  "idClase": 1,
  "idMateria": null
}
```

**Errores posibles:**

| Código | Causa                                |
|--------|----------------------------------------|
| 404    | No existe una reseña con ese `idResena` |

---

### 4. Actualizar una reseña

**`PUT /api/resenas/{idResena}`**

**Request body:**

```json
{
  "calificacionGeneral": 5.0,
  "metodologiaEnsenansa": 4.5,
  "nivelExigencia": 4.0,
  "comentario": "Actualizo mi opinión, mejoró mucho.",
  "anonima": true,
  "idEstudiante": 1,
  "idClase": 1,
  "idMateria": null
}
```

**Respuesta exitosa — `200 OK`**

```json
{
  "idResena": 10,
  "calificacionGeneral": 5.0,
  "metodologiaEnsenansa": 4.5,
  "nivelExigencia": 4.0,
  "comentario": "Actualizo mi opinión, mejoró mucho.",
  "fechaCreacion": "2026-09-25T14:30:00",
  "anonima": true,
  "idEstudiante": 1,
  "idClase": 1,
  "idMateria": null
}
```

**Errores posibles:**

| Código | Causa                                                              |
|--------|---------------------------------------------------------------------|
| 400    | `idClase` e `idMateria` ambos nulos, o ambos con valor              |
| 404    | No existe una reseña con ese `idResena`                             |
| 404    | `idEstudiante`, `idClase` o `idMateria` no existen                  |

---

### 5. Eliminar una reseña

**`DELETE /api/resenas/{idResena}`**

**Respuesta exitosa — `204 No Content`**

(Sin cuerpo de respuesta)

**Errores posibles:**

| Código | Causa                                |
|--------|----------------------------------------|
| 404    | No existe una reseña con ese `idResena` |

---

## Resumen de endpoints

| Método | Ruta                     | Descripción                          |
|--------|--------------------------|----------------------------------------|
| POST   | `/api/resenas`           | Crear una nueva reseña                 |
| GET    | `/api/resenas`           | Listar reseñas (con filtros opcionales)|
| GET    | `/api/resenas/{id}`      | Obtener una reseña por ID              |
| PUT    | `/api/resenas/{id}`      | Actualizar una reseña existente        |
| DELETE | `/api/resenas/{id}`      | Eliminar una reseña                    |

---

## Formato de error estándar

Todas las respuestas de error siguen esta estructura:

```json
{
  "timestamp": "2026-09-25T14:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "idClase e idMateria no pueden ser ambos nulos o ambos con valor",
  "path": "/api/resenas"
}
```