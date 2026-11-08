package com.lavozdelaula.controllers;

import com.lavozdelaula.dto.ClaseDto;
import com.lavozdelaula.exceptions.BusinessException;
import com.lavozdelaula.services.ClaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clases")
public class ClaseController {

    private final ClaseService claseService;

    public ClaseController(ClaseService claseService) {
        this.claseService = claseService;
    }

    @Operation(
            summary = "Asignar docente a materia",
            description = "Crea una asignación entre un docente y una materia."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Asignación creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o solicitud incorrecta"),
            @ApiResponse(responseCode = "404", description = "Docente o materia no encontrado")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClaseDto asignarDocenteMateria(@Valid @RequestBody ClaseDto solicitud) {
        return claseService.asignarDocenteMateria(solicitud);
    }

    @Operation(
            summary = "Listar clases y asignaciones",
            description = "Obtiene todas las asignaciones. Permite filtrar por docente o materia, utilizando un solo filtro por solicitud."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de asignaciones obtenido correctamente"),
            @ApiResponse(responseCode = "400", description = "No se permite utilizar ambos filtros simultáneamente")
    })
    @GetMapping
    public List<ClaseDto> obtenerTodas(
            @Parameter(description = "Identificador del docente para filtrar las asignaciones")
            @RequestParam(required = false) Integer idDocente,

            @Parameter(description = "Identificador de la materia para filtrar las asignaciones")
            @RequestParam(required = false) Integer idMateria) {

        if (idDocente != null && idMateria != null) {
            throw new BusinessException("Utilice un solo filtro por solicitud.");
        }

        if (idDocente != null) {
            return claseService.obtenerPorDocente(idDocente);
        }

        if (idMateria != null) {
            return claseService.obtenerPorMateria(idMateria);
        }

        return claseService.obtenerTodas();
    }

    @Operation(
            summary = "Consultar clase por ID",
            description = "Obtiene los datos de una asignación específica mediante su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clase encontrada correctamente"),
            @ApiResponse(responseCode = "400", description = "Identificador inválido"),
            @ApiResponse(responseCode = "404", description = "Clase no encontrada")
    })
    @GetMapping("/{idClase}")
    public ClaseDto obtenerPorId(
            @Parameter(description = "Identificador de la clase")
            @PathVariable Integer idClase) {
        return claseService.obtenerPorId(idClase);
    }
}
