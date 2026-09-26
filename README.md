# 🎓 La Voz del Aula

> **La experiencia de quienes ya estuvieron en el aula puede ayudarte a decidir qué esperar de ella.**

**La Voz del Aula** es una plataforma digital orientada a la comunidad estudiantil del **Instituto Tecnológico Metropolitano (ITM)** que permite recopilar, consultar y analizar opiniones y experiencias relacionadas con el desempeño y las prácticas pedagógicas de los docentes.

El proyecto busca convertir las experiencias individuales de los estudiantes en **información organizada y accesible**, que contribuya a comprender las dinámicas de enseñanza y facilite la toma de decisiones durante el proceso de inscripción de asignaturas.

---

## 💡 ¿Por qué La Voz del Aula?

Elegir una asignatura no siempre consiste únicamente en conocer su nombre, créditos o contenido.

La experiencia académica también puede estar influenciada por aspectos como:

* La metodología utilizada por el docente.
* La claridad con la que se explican los contenidos.
* Las estrategias utilizadas para fomentar la participación.
* Los mecanismos de evaluación.
* El acompañamiento durante el proceso de aprendizaje.
* La percepción de los estudiantes frente al ambiente de clase.
* La motivación y el desarrollo de competencias.

Sin embargo, gran parte de esta información permanece dispersa en conversaciones informales entre estudiantes.

**La Voz del Aula busca centralizar esas experiencias y convertirlas en información estructurada que pueda ser consultada por la comunidad académica.**

---

## 🎯 Objetivo

Desarrollar una plataforma digital que funcione como un **índice y repositorio de opiniones estudiantiles sobre la experiencia docente en el ITM**, permitiendo a la comunidad académica consultar y compartir valoraciones que contribuyan al análisis de las prácticas pedagógicas y orienten la toma de decisiones en el proceso de inscripción.

---

## 🔎 ¿Qué busca analizar?

El sistema parte de cinco grandes necesidades:

### 1. Prácticas pedagógicas

Identificar metodologías, estrategias y prácticas de enseñanza utilizadas por los docentes que pueden influir en el aprendizaje de los estudiantes.

### 2. Experiencia estudiantil

Comprender las principales dificultades y necesidades relacionadas con:

* Comprensión de contenidos.
* Participación en clase.
* Desarrollo de competencias.
* Rendimiento académico.
* Motivación.

### 3. Percepción de la comunidad

Recopilar las percepciones, necesidades y expectativas de los estudiantes frente a las prácticas de enseñanza y su influencia en la experiencia académica.

### 4. Requerimientos del sistema

Establecer los requerimientos funcionales y no funcionales necesarios para recopilar, gestionar y analizar información relacionada con la experiencia estudiantil.

### 5. Información para la toma de decisiones

Diseñar funcionalidades que permitan transformar la información recopilada en datos útiles para comprender la experiencia de enseñanza y aprendizaje.

---

## 🚀 Propuesta de valor

**La Voz del Aula conecta experiencias estudiantiles con información útil para tomar decisiones académicas con mayor contexto.**

No se busca reducir la experiencia docente a una única calificación.

La plataforma contempla diferentes dimensiones de la experiencia académica para que las opiniones puedan proporcionar **más contexto que una simple puntuación**.

---

## 🏗️ Arquitectura

El proyecto está construido bajo una arquitectura cliente-servidor basada en una **API REST**.

```text
┌───────────────────────────┐
│       Frontend Web        │
│      HTML + CSS + JS      │
└─────────────┬─────────────┘
              │
              │ HTTP / REST
              ▼
┌───────────────────────────┐
│       Backend API         │
│       Java + Spring       │
└─────────────┬─────────────┘
              │
              │ SQL / PostgreSQL
              ▼
┌───────────────────────────┐
│       Base de datos       │
│        PostgreSQL         │
│         Supabase          │
└───────────────────────────┘
```

### Tecnologías

| Capa                 | Tecnología                |
| -------------------- | ------------------------- |
| Backend              | Java + Spring             |
| API                  | REST                      |
| Frontend             | HTML5 + CSS3 + JavaScript |
| Base de datos        | PostgreSQL                |
| Plataforma de BD     | Supabase                  |
| Control de versiones | Git + GitHub              |

---

## 📌 Funcionalidades principales

La plataforma está orientada a permitir:

* 🔎 Consultar información sobre docentes y experiencias académicas.
* 📝 Registrar opiniones y experiencias estudiantiles.
* 📊 Visualizar valoraciones organizadas por diferentes dimensiones.
* 📚 Relacionar experiencias con asignaturas y docentes.
* 👥 Facilitar el intercambio de información dentro de la comunidad estudiantil.
* 📈 Organizar la información recopilada para facilitar su análisis.
* 🔐 Gestionar el acceso y la información de los usuarios.

> Las funcionalidades pueden evolucionar a medida que avance el levantamiento de requerimientos y el desarrollo del proyecto.

---

## 🧩 Modelo conceptual

La información gira alrededor de una relación fundamental:

```text
        ESTUDIANTE
             │
             │ comparte
             ▼
         EXPERIENCIA
             │
       ┌─────┴─────┐
       ▼           ▼
   ASIGNATURA    DOCENTE
       │           │
       └─────┬─────┘
             │
             ▼
       VALORACIÓN
             │
             ▼
        INFORMACIÓN
```

Esto permite pasar de opiniones individuales a información estructurada que pueda ser consultada y analizada.

---

## 📂 Estructura del proyecto

```text
la-voz-del-aula/
│
├── backend/
│   └── ...
│
├── frontend/
│   ├── index.html
│   ├── css/
│   ├── js/
│   └── ...
│
├── database/
│   └── ...
│
├── docs/
│   └── ...
│
├── .gitignore
└── README.md
```

---

## ⚙️ Instalación y ejecución

### Requisitos

Antes de ejecutar el proyecto necesitas tener instalado:

* Java
* Maven
* Git
* Un navegador web moderno

Además, se requiere acceso a una instancia de **PostgreSQL** configurada en Supabase.

### 1. Clonar el repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd la-voz-del-aula
```

### 2. Configurar la base de datos

Crear/configurar una base de datos PostgreSQL en Supabase y establecer las credenciales necesarias para la aplicación.

Las variables de entorno requeridas se documentarán en la configuración del backend.

### 3. Ejecutar el backend

```bash
cd backend
./mvnw spring-boot:run
```

En Windows:

```bash
mvnw.cmd spring-boot:run
```

### 4. Ejecutar el frontend

Abrir `frontend/index.html` en un navegador o utilizar un servidor local para servir los archivos estáticos.

---

## 🔌 API

El backend expone una API REST para permitir la comunicación entre el frontend y la base de datos.

Ejemplo conceptual:

```text
GET    /api/docentes
GET    /api/docentes/{id}
GET    /api/asignaturas
GET    /api/opiniones
POST   /api/opiniones
```

> Los endpoints definitivos dependerán del diseño final de la API.

---

## 🎓 Contexto académico

**La Voz del Aula** es un proyecto desarrollado en el contexto académico del **Instituto Tecnológico Metropolitano (ITM)**.

El proyecto integra conceptos de:

* Ingeniería de software.
* Desarrollo de aplicaciones web.
* Diseño de APIs REST.
* Modelado y gestión de bases de datos.
* Levantamiento de requerimientos.
* Análisis de necesidades de usuarios.
* Diseño de soluciones de software.

---

## 🛡️ Consideraciones sobre las opiniones

Las opiniones representan **experiencias y percepciones individuales de estudiantes**.

Por esta razón, la información publicada en la plataforma debe interpretarse como una referencia basada en experiencias compartidas y no como una medida absoluta del desempeño de un docente.

El diseño del sistema busca promover información **contextualizada, responsable y útil para la comunidad académica**.

---

## 👥 Equipo

Proyecto desarrollado por estudiantes del **Instituto Tecnológico Metropolitano — ITM**.

> *La información mejora cuando las experiencias dejan de quedarse en conversaciones aisladas.*

---

### 📄 Licencia

Este proyecto fue desarrollado con fines académicos.

## 📚 Documentación de la API
La especificación de la API RESTful está publicada e interactiva en SwaggerHub:
👉 [Ver La Voz del Aula](https://app.swaggerhub.com/apis/itm-8bc/VozDelAula/1.0#/)