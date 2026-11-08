package com.lavozdelaula.controllers;

import com.lavozdelaula.dto.MateriaDto;
import com.lavozdelaula.services.MateriaService;
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
@RequestMapping("/api/materias")
public class MateriaController {

    private final MateriaService materiaService;

    public MateriaController(MateriaService materiaService) {
        this.materiaService = materiaService;
    }

    @Operation(summary = "Registrar materia")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Materia registrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Código de materia ya registrado")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MateriaDto registrar(@Valid @RequestBody MateriaDto solicitud) {
        return materiaService.registrar(solicitud);
    }

    @Operation(
            summary = "Listar materias",
            description = "Obtiene el listado de materias registradas en el sistema."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listado de materias obtenido correctamente")
    })
    @GetMapping
    public List<MateriaDto> obtenerTodas() {
        return materiaService.obtenerTodas();
    }

    @Operation(summary = "Consultar materia")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Materia encontrada"),
            @ApiResponse(responseCode = "404", description = "Materia no encontrada")
    })
    @GetMapping("/{idMateria}")
    public MateriaDto obtenerPorId(
        @Parameter(description = "Identificador de la materia")
        @PathVariable Integer idMateria) {
        return materiaService.obtenerPorId(idMateria);
    }
}
