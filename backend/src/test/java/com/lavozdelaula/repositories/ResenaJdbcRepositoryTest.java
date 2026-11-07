package com.lavozdelaula.repositories;

import com.lavozdelaula.entities.Resena;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ResenaJdbcRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ResenaJdbcRepository repository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("INSERT INTO estudiante " +
                "(id_estudiante, codigo_institucional, nombre, correo_institucional) VALUES (10, 'EST-10', 'Ana', 'ana@test.edu')");
        jdbcTemplate.update("INSERT INTO docente " +
                "(id_docente, nombre, correo_institucional) VALUES (20, 'Docente', 'docente@test.edu')");
        jdbcTemplate.update("INSERT INTO materia (id_materia, codigo_materia, nombre_materia) " +
                "VALUES (30, 'MAT-BD-01', 'Bases de datos')");
        jdbcTemplate.update("INSERT INTO clase " +
                "(id_clase, codigo, id_docente, id_materia) VALUES (40, 'BD-01', 20, 30)");
        jdbcTemplate.update("INSERT INTO resena " +
                "(id_resena, calificacion_general, metodologia_ensenansa, nivel_exigencia, comentario, fecha_creacion, anonima, id_estudiante, id_clase) " +
                "VALUES (50, 4.5, 4.0, 3.5, 'Resena de clase', CURRENT_TIMESTAMP, FALSE, 10, 40)");
        jdbcTemplate.update("INSERT INTO resena " +
                "(id_resena, calificacion_general, comentario, fecha_creacion, anonima, id_estudiante, id_materia) " +
                "VALUES (51, 3.5, 'Resena de materia', CURRENT_TIMESTAMP, TRUE, 10, 30)");
    }

    @Test
    void findAllSeleccionaColumnasYMapeaResenas() {
        List<Resena> results = repository.findAll();
        assertEquals(2, results.size());
    }

    @Test
    void findByEstudianteFiltraPorEstudiante() {
        assertEquals(2, repository.findByEstudiante(10).size());
    }

    @Test
    void findByClaseFiltraPorClase() {
        assertEquals(1, repository.findByClase(40).size());
    }

    @Test
    void findByMateriaIncluyeResenasDirectasYDeClase() {
        List<Resena> results = repository.findByMateria(30);
        assertEquals(2, results.size());
        assertNull(results.stream()
                .filter(Resena::isAnonima)
                .findFirst()
                .orElseThrow()
                .getIdEstudiante());
    }

    @Test
    void findByDocenteIncluyeResenasDeLasClasesDictadas() {
        assertEquals(1, repository.findByDocente(20).size());
    }
}
