package com.lavozdelaula.controllers;

import com.lavozdelaula.dto.DocenteDto;
import com.lavozdelaula.services.DocenteService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/docentes")
public class DocenteController {

    private final DocenteService docenteService;

    public DocenteController(DocenteService docenteService) {
        this.docenteService = docenteService;
    }

    @Operation(summary = "Registrar docente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Docente registrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Correo ya registrado")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DocenteDto registrar(@Valid @RequestBody DocenteDto solicitud) {
        return docenteService.registrar(solicitud);
    }
    
    @Operation(
            summary = "Listar docentes",
            description = "Obtiene el listado de docentes registrados en el sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de docentes obtenido correctamente")
    })
    @GetMapping
    public List<DocenteDto> obtenerTodos() {
    return docenteService.obtenerTodos();
    }

    @Operation(summary = "Consultar docente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Docente encontrado"),
            @ApiResponse(responseCode = "404", description = "Docente no encontrado")
    })
    @GetMapping("/{idDocente}")
    public DocenteDto obtenerPorId(
    @Parameter(description = "Identificador del docente")
    @PathVariable Integer idDocente) {
    return docenteService.obtenerPorId(idDocente);
    }
}
