package com.lavozdelaula.controllers;

import com.lavozdelaula.dto.ClaseDto;
import com.lavozdelaula.exceptions.BusinessException;
import com.lavozdelaula.services.ClaseService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Asignar docente a materia")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Asignación creada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Docente o materia no encontrado")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClaseDto asignarDocenteMateria(@Valid @RequestBody ClaseDto solicitud) {
        return claseService.asignarDocenteMateria(solicitud);
    }

    @Operation(summary = "Listar clases y asignaciones", description = "Puede filtrarse por docente o materia.")
    @GetMapping
    public List<ClaseDto> obtenerTodas(
            @RequestParam(required = false) Integer idDocente,
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

    @Operation(summary = "Consultar clase por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clase encontrada"),
            @ApiResponse(responseCode = "404", description = "Clase no encontrada")
    })
    @GetMapping("/{idClase}")
    public ClaseDto obtenerPorId(@PathVariable Integer idClase) {
        return claseService.obtenerPorId(idClase);
    }
}
