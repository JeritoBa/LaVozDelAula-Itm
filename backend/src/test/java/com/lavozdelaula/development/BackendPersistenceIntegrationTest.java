package com.lavozdelaula.development;

import com.lavozdelaula.dto.ClaseDto;
import com.lavozdelaula.dto.DocenteDto;
import com.lavozdelaula.dto.EstudianteDto;
import com.lavozdelaula.dto.MateriaDto;
import com.lavozdelaula.dto.PerfilEstudianteDto;
import com.lavozdelaula.entities.Resena;
import com.lavozdelaula.services.ClaseService;
import com.lavozdelaula.services.DocenteService;
import com.lavozdelaula.services.EstudianteService;
import com.lavozdelaula.services.MateriaService;
import com.lavozdelaula.services.ResenaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BackendPersistenceIntegrationTest {

    @Autowired
    private EstudianteService estudianteService;
    @Autowired
    private DocenteService docenteService;
    @Autowired
    private MateriaService materiaService;
    @Autowired
    private ClaseService claseService;
    @Autowired
    private ResenaService resenaService;

    @Test
    void persisteCasosDeUsoYMapeosJpa() {
        String suffix = UUID.randomUUID().toString();
        EstudianteDto estudiante = estudianteService.registrar(new EstudianteDto(
                null, "EST-" + suffix, "Estudiante de prueba", suffix + "@estudiante.test",
                "Ingeniería", new PerfilEstudianteDto("3000000000", "https://example.test/foto.jpg")));
        DocenteDto docente = docenteService.registrar(new DocenteDto(
                null, "Docente de prueba", "Ingeniería", suffix + "@docente.test"));
        MateriaDto materia = materiaService.registrar(new MateriaDto(
                null, "MAT-" + suffix.substring(0, 8), "Materia de prueba " + suffix, "Ingeniería"));
        ClaseDto clase = claseService.asignarDocenteMateria(new ClaseDto(
                null, "Diurna", "CL-" + suffix.substring(0, 8), "Lunes 8-10",
                docente.idDocente(), materia.idMateria()));

        Resena solicitud = new Resena(
                null, new BigDecimal("4.5"), new BigDecimal("4.0"), null,
                "Buena experiencia de aprendizaje", null, true,
                estudiante.idEstudiante(), clase.idClase(), null);
        Resena creada = resenaService.crear(solicitud);
        Resena consultada = resenaService.obtenerPorId(creada.getIdResena());

        assertNull(consultada.getIdEstudiante());
        assertEquals(clase.idClase(), consultada.getIdClase());
        assertEquals(docente.idDocente(),
                estudianteService.obtenerDocentesRelacionados(estudiante.idEstudiante()).get(0).idDocente());
    }
}
