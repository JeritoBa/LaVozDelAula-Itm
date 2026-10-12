
package com.lavozdelaula.controllers;

import com.lavozdelaula.entities.Materia;
import com.lavozdelaula.services.MateriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {

    private final MateriaService materiaService;

    public MateriaController(MateriaService materiaService) {
        this.materiaService = materiaService;
    }

    @GetMapping
    public ResponseEntity<List<Materia>> obtenerTodas() {
        return ResponseEntity.ok(materiaService.obtenerTodas());
    }

    @GetMapping("/{idMateria}")
    public ResponseEntity<Materia> obtenerPorId(
            @PathVariable Integer idMateria) {
        return materiaService.obtenerPorId(idMateria)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
