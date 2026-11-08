# Calidad de código y estrategia de pruebas

## Estado evaluado

La organización actual separa controladores, servicios, repositorios, DTO y entidades. Los servicios reciben repositorios por constructor; la lógica de negocio puede probarse con Mockito sin iniciar Spring. Esto constituye una base mantenible, no una certificación de cumplimiento SOLID.

| Principio | Evidencia actual | Evaluación y siguiente paso |
|---|---|---|
| **S — Responsabilidad única** | Controladores traducen HTTP y servicios concentran reglas/orquestación. | Base clara. `ResenaService` valida, coordina persistencia y convierte entidades/modelos; vigilar su crecimiento y extraer políticas solo cuando tengan razón de cambio independiente. |
| **O — Abierto/cerrado** | Interfaces de repositorio y separación por capas permiten sustituir implementaciones. | No hay un mecanismo de extensión formal para políticas o proveedores nuevos. Evitar jerarquías especulativas; abstraer ante una segunda implementación real. |
| **L — Sustitución de Liskov** | No se observaron jerarquías propias relevantes en el dominio revisado. | No es posible valorar sustitución con evidencia suficiente; no crear subtipos de entidad para forzar el principio. |
| **I — Segregación de interfaces** | Repositorios separados por agregado/entidad. | Adecuado a la escala actual; mantener interfaces pequeñas y específicas al agregar operaciones. |
| **D — Inversión de dependencias** | Servicios dependen de interfaces de repositorio inyectadas por constructor. | Evidencia positiva en la capa de aplicación. La capa de dominio aún conoce anotaciones/tipos de Spring/JPA en algunas partes; valorar separación solo si crece la lógica independiente del framework. |

## Clean Code y riesgos

- Los nombres y capas son generalmente descriptivos; las validaciones HTTP y los errores tienen puntos centralizados.
- Se retiraron dos copias de `ResenaService`/`ResenaServiceTest` que estaban en la raíz en vez de formar parte del módulo Maven. Las pruebas útiles de anonimato se trasladaron a la suite real.
- La respuesta de creación ahora respeta el anonimato; el controlador responde `400` ante JSON no legible en lugar de informar un error interno `500`.
- No se añadió una política de autenticación, autorización ni moderación: el ID de estudiante recibido en el cuerpo no es una identidad confiable. Es un pendiente de seguridad, no una refactorización de estilo.
- No hay evidencia de análisis estático, formato automático o cobertura de frontend. Evitar declarar cubiertos RNF de seguridad, privacidad o mantenibilidad solo por tener pruebas unitarias.

## Pruebas

La suite combina pruebas unitarias de servicios/repositorio, pruebas MVC de reseñas y pruebas de integración/persistencia. Las pruebas nuevas verifican filtrado HTTP, rechazo de filtros incompatibles, validación del rango de calificación, JSON malformado y anonimato en la creación.

Ejecutar desde `backend`:

```text
./mvnw test
```

En Windows: `mvnw.cmd test`. El wrapper del repositorio puede requerir que `maven-wrapper.properties` y la caché local de Maven estén correctamente configurados. La cobertura no está cuantificada hasta incorporar/ejecutar un reporte de cobertura.
