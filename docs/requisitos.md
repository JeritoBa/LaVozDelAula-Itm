# Requisitos del Sistema

## 1. Requisitos Funcionales

### RF01. Registro de estudiantes
El sistema debe permitir registrar la información básica de los estudiantes:
- ID del estudiante.
- Código institucional.
- Nombre.
- Correo institucional.
- Programa académico.

**Entidad relacionada:** Estudiante

### RF02. Consulta de estudiantes
El sistema debe permitir consultar la información registrada de un estudiante y conocer los docentes relacionados con sus actividades académicas.

**Entidad relacionada:** Estudiante

### RF03. Registro de docentes
El sistema debe permitir registrar la información de los docentes:
- ID del docente.
- Nombre.
- Departamento o facultad.
- Correo institucional.

**Entidad relacionada:** Docente

### RF04. Consulta de docentes
El sistema debe permitir consultar los docentes registrados y visualizar la información correspondiente a cada uno.

**Entidad relacionada:** Docente

### RF05. Registro de materias
El sistema debe permitir registrar las materias o asignaturas ofrecidas por el ITM:
- ID de la materia.
- Código de la materia.
- Nombre de la materia.

**Entidad relacionada:** Materia

### RF06. Asignación de docentes a materias
El sistema debe permitir relacionar docentes con las materias que dictan.

Un docente puede dictar varias materias y una materia puede ser dictada por diferentes docentes.

**Entidad relacionada:** AsignacionDocenteMateria

### RF07. Creación de reseñas
El sistema debe permitir que un estudiante cree una reseña sobre un docente asociado a una materia. La reseña debe contener:
- ID de la reseña.
- Calificación general.
- Metodología de enseñanza.
- Nivel de exigencia.
- Comentario.
- Fecha de creación.
- Estudiante que realiza la reseña.
- Docente evaluado.
- Materia relacionada.

**Entidad relacionada:** Reseña

### RF08. Consulta de reseñas
El sistema debe permitir consultar las reseñas realizadas por los estudiantes sobre los docentes y las materias.

Se debe poder consultar las reseñas asociadas a:
- Un estudiante.
- Un docente.
- Una materia.

**Entidad relacionada:** Reseña

### RF09. Consulta de reseñas de un docente
El sistema debe permitir que un docente tenga asociadas múltiples reseñas realizadas por estudiantes.

**Relación:** Docente 1 ⟶ 0..* Reseña

**Entidad relacionada:** Reseña

### RF10. Consulta de reseñas de una materia
El sistema debe permitir consultar las reseñas relacionadas con una materia determinada.

**Relación:** Materia 1 ⟶ 0..* Reseña

**Entidad relacionada:** Reseña

### RF11. Edición de reseñas
El sistema debe permitir al estudiante editar una reseña que haya creado previamente.

La edición debe actualizar la información de la reseña sin crear una nueva.

**Entidad relacionada:** Reseña

### RF12. Publicación de reseñas
El sistema debe permitir publicar las reseñas creadas por los estudiantes para que puedan ser consultadas por la comunidad académica, de acuerdo con las reglas de acceso establecidas.

**Entidad relacionada:** Reseña

### RF13. Calificación del desempeño docente
El sistema debe permitir registrar una calificación general sobre el docente mediante la reseña. Esto permitirá posteriormente analizar la percepción de los estudiantes sobre el desempeño docente.

### RF14. Registro de metodología de enseñanza
El sistema debe permitir registrar la percepción del estudiante sobre la metodología de enseñanza utilizada por el docente.

### RF15. Registro de nivel de exigencia
El sistema debe permitir registrar el nivel de exigencia percibido por el estudiante en relación con su docente y la materia.

### RF16. Registro de comentarios
El sistema debe permitir que los estudiantes agreguen comentarios relacionados con su experiencia académica con el docente.

### RF17. Opción de anonimato en las reseñas
El sistema debe permitir que el estudiante elija si desea realizar la reseña de forma anónima o identificada.
- Si el estudiante elige publicar de forma anónima, su identidad no será visible para los demás usuarios.
- Si el estudiante elige publicar de forma identificada, su nombre (o código) podrá ser visible junto con la reseña.
- El sistema debe almacenar la información necesaria para respetar la opción seleccionada por el estudiante.

**Entidad relacionada:** Reseña

---

## 2. Requisitos No Funcionales

### RNF01. Usabilidad
La plataforma debe contar con una interfaz sencilla e intuitiva que permita a los estudiantes consultar y registrar información sin conocimientos técnicos.

### RNF02. Seguridad
El sistema debe proteger la información de los estudiantes, docentes y reseñas almacenadas.

### RNF03. Privacidad
La información personal de los estudiantes debe ser protegida y solamente debe mostrarse aquella información que esté autorizada para consulta.

### RNF04. Disponibilidad
La plataforma debe estar disponible para los usuarios durante los períodos establecidos por la institución.

### RNF05. Integridad de la información
El sistema debe garantizar que las reseñas estén asociadas correctamente con un estudiante, un docente y una materia existentes.

### RNF06. Rendimiento
El sistema debe responder en un tiempo adecuado al realizar consultas de estudiantes, docentes, materias y reseñas.

### RNF07. Escalabilidad
El sistema debe permitir aumentar progresivamente la cantidad de estudiantes, docentes, materias y reseñas sin afectar significativamente su funcionamiento.

### RNF08. Mantenibilidad
El software debe estar desarrollado de forma organizada, permitiendo realizar modificaciones y agregar nuevas funcionalidades en el futuro.

### RNF09. Compatibilidad
La plataforma debe poder utilizarse desde los principales navegadores web y dispositivos utilizados por la comunidad académica.

### RNF10. Moderación
El sistema debe contemplar mecanismos para evitar que las reseñas contengan contenido ofensivo, discriminatorio o que no esté relacionado con la experiencia académica.