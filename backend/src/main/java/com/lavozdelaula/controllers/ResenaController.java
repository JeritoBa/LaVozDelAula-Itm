package com.lavozdelaula.controllers;

import com.lavozdelaula.entities.Resena;
import com.lavozdelaula.services.ResenaService;
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

    @PostMapping
    public ResponseEntity<Resena> crear(@RequestBody Resena resena) {
        Resena creada = resenaService.crear(resena);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(creada);
    }

    @GetMapping
    public ResponseEntity<List<Resena>> obtenerTodas(
            @RequestParam(required = false) Integer idEstudiante,
            @RequestParam(required = false) Integer idClase,
            @RequestParam(required = false) Integer idMateria) {

        if (idEstudiante != null) {
            return ResponseEntity.ok(
                    resenaService.obtenerPorEstudiante(idEstudiante)
            );
        }

        if (idClase != null) {
            return ResponseEntity.ok(
                    resenaService.obtenerPorClase(idClase)
            );
        }

        if (idMateria != null) {
            return ResponseEntity.ok(
                    resenaService.obtenerPorMateria(idMateria)
            );
        }

        return ResponseEntity.ok(
                resenaService.obtenerTodas()
        );
    }

    @GetMapping("/{idResena}")
    public ResponseEntity<Resena> obtenerPorId(
            @PathVariable Integer idResena) {

        return resenaService.obtenerPorId(idResena)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{idResena}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Integer idResena) {

        boolean eliminado = resenaService.eliminar(idResena);

        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{idResena}")
    public ResponseEntity actualizar(
            @PathVariable Integer idResena,
            @RequestBody Resena resena) {

        boolean actualizado = resenaService.actualizar(idResena, resena);

        if (!actualizado) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(resena);
    }
}
