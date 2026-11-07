package com.lavozdelaula.controllers;

import com.lavozdelaula.entities.Resena;
import com.lavozdelaula.exceptions.ApiError;
import com.lavozdelaula.exceptions.BusinessException;
import com.lavozdelaula.services.ResenaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    private final ResenaService resenaService;

    public ResenaController(ResenaService resenaService) {
        this.resenaService = resenaService;
    }

    @Operation(summary = "Crear una reseña", description = "Registra una nueva reseña con validación de negocio.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reseña creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Estudiante, clase o materia no encontrada",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto de integridad",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<Resena> crear(@Valid @RequestBody Resena resena) {
        Resena creada = resenaService.crear(resena);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @Operation(summary = "Listar reseñas", description = "Obtiene todas las reseñas o filtra por estudiante, docente, clase o materia. Solo se permite un filtro por solicitud.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de reseñas"),
            @ApiResponse(responseCode = "400", description = "Se solicitaron filtros incompatibles",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Docente no encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping
    public ResponseEntity<List<Resena>> obtenerTodas(
            @Parameter(description = "Identificador del estudiante")
            @RequestParam(required = false) Integer idEstudiante,
            @Parameter(description = "Identificador del docente")
            @RequestParam(required = false) Integer idDocente,
            @Parameter(description = "Identificador de la clase")
            @RequestParam(required = false) Integer idClase,
            @Parameter(description = "Identificador de la materia")
            @RequestParam(required = false) Integer idMateria) {

        int filtros = (idEstudiante == null ? 0 : 1)
                + (idDocente == null ? 0 : 1)
                + (idClase == null ? 0 : 1)
                + (idMateria == null ? 0 : 1);
        if (filtros > 1) {
            throw new BusinessException("Utilice un solo filtro por solicitud.");
        }
        if (idEstudiante != null) {
            return ResponseEntity.ok(resenaService.obtenerPorEstudiante(idEstudiante));
        }
        if (idDocente != null) {
            return ResponseEntity.ok(resenaService.obtenerPorDocente(idDocente));
        }
        if (idClase != null) {
            return ResponseEntity.ok(resenaService.obtenerPorClase(idClase));
        }
        if (idMateria != null) {
            return ResponseEntity.ok(resenaService.obtenerPorMateria(idMateria));
        }

        return ResponseEntity.ok(resenaService.obtenerTodas());
    }

    @Operation(summary = "Obtener reseña por id", description = "Consulta una reseña específica por su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña encontrada"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{idResena}")
    public ResponseEntity<Resena> obtenerPorId(@PathVariable Integer idResena) {
        return ResponseEntity.ok(resenaService.obtenerPorId(idResena));
    }

    @Operation(summary = "Actualizar reseña", description = "Actualiza una reseña existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña actualizada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o el estudiante no es propietario",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Reseña o asociación no encontrada",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/{idResena}")
    public ResponseEntity<Resena> actualizar(@PathVariable Integer idResena, @Valid @RequestBody Resena resena) {
        boolean actualizado = resenaService.actualizar(idResena, resena);
        if (!actualizado) {
            return ResponseEntity.notFound().build();
        }
        resena.setIdResena(idResena);
        return ResponseEntity.ok(resena);
    }

    @Operation(summary = "Eliminar reseña", description = "Elimina una reseña por su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reseña eliminada"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{idResena}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idResena) {
        boolean eliminado = resenaService.eliminar(idResena);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
