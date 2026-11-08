package com.lavozdelaula.controllers;

import com.lavozdelaula.dto.EstudianteDto;
import com.lavozdelaula.dto.DocenteDto;
import com.lavozdelaula.services.EstudianteService;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @Operation(summary = "Registrar estudiante")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Estudiante registrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Código o correo ya registrados")
    })
    @PostMapping
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.CREATED)
    public EstudianteDto registrar(@Valid @RequestBody EstudianteDto solicitud) {
        return estudianteService.registrar(solicitud);
    }

    @Operation(
            summary = "Listar estudiantes",
            description = "Obtiene el listado de estudiantes registrados en el sistema."
    )
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de estudiantes obtenido correctamente")
    })
    @GetMapping
    public List<EstudianteDto> obtenerTodos() {
        return estudianteService.obtenerTodos();
    }

    @Operation(summary = "Consultar estudiante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estudiante encontrado"),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    @GetMapping("/{idEstudiante}")
    public EstudianteDto obtenerPorId(
        @Parameter(description = "Identificador del estudiante")
        @PathVariable Integer idEstudiante) {
        return estudianteService.obtenerPorId(idEstudiante);
    }

    @Operation(summary = "Consultar docentes relacionados con un estudiante",
            description = "Devuelve los docentes de las clases asociadas a las reseñas del estudiante.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Docentes encontrados; puede ser una lista vacía"),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    @GetMapping("/{idEstudiante}/docentes")
    public List<DocenteDto> obtenerDocentesRelacionados(
        @Parameter(description = "Identificador del estudiante")
        @PathVariable Integer idEstudiante) {
        return estudianteService.obtenerDocentesRelacionados(idEstudiante);
    }
}
