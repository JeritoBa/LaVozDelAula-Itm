package com.lavozdelaula.services;

import com.lavozdelaula.entities.Resena;
import com.lavozdelaula.entities.jpa.ClaseEntity;
import com.lavozdelaula.entities.jpa.DocenteEntity;
import com.lavozdelaula.entities.jpa.EstudianteEntity;
import com.lavozdelaula.entities.jpa.MateriaEntity;
import com.lavozdelaula.entities.jpa.ResenaEntity;
import com.lavozdelaula.exceptions.BusinessException;
import com.lavozdelaula.exceptions.ResourceNotFoundException;
import com.lavozdelaula.repositories.ClaseRepository;
import com.lavozdelaula.repositories.DocenteRepository;
import com.lavozdelaula.repositories.EstudianteRepository;
import com.lavozdelaula.repositories.MateriaRepository;
import com.lavozdelaula.repositories.ResenaJdbcRepository;
import com.lavozdelaula.repositories.ResenaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResenaServiceTest {

    @Mock
    private ResenaRepository resenaRepository;
    @Mock
    private ResenaJdbcRepository resenaJdbcRepository;
    @Mock
    private EstudianteRepository estudianteRepository;
    @Mock
    private ClaseRepository claseRepository;
    @Mock
    private MateriaRepository materiaRepository;
    @Mock
    private DocenteRepository docenteRepository;
    @InjectMocks
    private ResenaService service;

    private Resena solicitud;

    @BeforeEach
    void setUp() {
        solicitud = new Resena(1, new BigDecimal("4.5"), new BigDecimal("4.0"),
                new BigDecimal("3.5"), "Buena experiencia", LocalDateTime.now(),
                false, 10, 7, null);
    }

    @Test
    void crearGuardaYDevuelveLaResena() {
        when(estudianteRepository.existsById(10)).thenReturn(true);
        when(claseRepository.existsById(7)).thenReturn(true);
        when(estudianteRepository.getReferenceById(10)).thenReturn(estudiante(10));
        when(claseRepository.getReferenceById(7)).thenReturn(clase(7));
        when(resenaRepository.save(any(ResenaEntity.class))).thenAnswer(invocation -> {
            ResenaEntity entity = invocation.getArgument(0);
            entity.setIdResena(1);
            return entity;
        });

        Resena creada = service.crear(solicitud);

        assertEquals(1, creada.getIdResena());
        assertEquals(10, creada.getIdEstudiante());
        assertEquals(7, creada.getIdClase());
        verify(resenaRepository).save(any(ResenaEntity.class));
    }

    @Test
    void crearRechazaRelacionesInvalidas() {
        solicitud.setIdMateria(12);
        assertThrows(BusinessException.class, () -> service.crear(solicitud));
        verifyNoInteractions(resenaRepository);
    }

    @Test
    void crearRechazaEstudianteInexistente() {
        when(estudianteRepository.existsById(10)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.crear(solicitud));
    }

    @Test
    void obtenerTodasUsaElDaoSql() {
        when(resenaJdbcRepository.findAll()).thenReturn(List.of(solicitud));
        assertEquals(1, service.obtenerTodas().size());
        verify(resenaJdbcRepository).findAll();
    }

    @Test
    void obtenerPorIdDevuelveResenaExistente() {
        when(resenaRepository.findById(1)).thenReturn(Optional.of(entity(1, 10, 7, null)));
        assertEquals(1, service.obtenerPorId(1).getIdResena());
    }

    @Test
    void obtenerPorIdLanzaNotFound() {
        when(resenaRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorId(99));
    }

    @Test
    void obtenerPorEstudianteFiltraConSql() {
        when(resenaJdbcRepository.findByEstudiante(10)).thenReturn(List.of(solicitud));
        assertEquals(1, service.obtenerPorEstudiante(10).size());
        verify(resenaJdbcRepository).findByEstudiante(10);
    }

    @Test
    void obtenerPorClaseFiltraConSql() {
        when(resenaJdbcRepository.findByClase(7)).thenReturn(List.of(solicitud));
        assertEquals(1, service.obtenerPorClase(7).size());
        verify(resenaJdbcRepository).findByClase(7);
    }

    @Test
    void obtenerPorMateriaFiltraConSql() {
        when(resenaJdbcRepository.findByMateria(12)).thenReturn(List.of());
        assertTrue(service.obtenerPorMateria(12).isEmpty());
        verify(resenaJdbcRepository).findByMateria(12);
    }

    @Test
    void obtenerPorDocenteValidaYFiltra() {
        when(docenteRepository.existsById(4)).thenReturn(true);
        when(resenaJdbcRepository.findByDocente(4)).thenReturn(List.of(solicitud));
        assertEquals(1, service.obtenerPorDocente(4).size());
    }

    @Test
    void obtenerPorDocenteRechazaIdInexistente() {
        when(docenteRepository.existsById(4)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorDocente(4));
        verifyNoInteractions(resenaJdbcRepository);
    }

    @Test
    void eliminarDevuelveTrueCuandoExiste() {
        when(resenaRepository.existsById(1)).thenReturn(true);
        assertTrue(service.eliminar(1));
        verify(resenaRepository).deleteById(1);
    }

    @Test
    void eliminarDevuelveFalseCuandoNoExiste() {
        when(resenaRepository.existsById(1)).thenReturn(false);
        assertFalse(service.eliminar(1));
        verify(resenaRepository, never()).deleteById(1);
    }

    @Test
    void actualizarGuardaLaEntidadExistente() {
        ResenaEntity actual = entity(1, 10, 7, null);
        when(estudianteRepository.existsById(10)).thenReturn(true);
        when(claseRepository.existsById(7)).thenReturn(true);
        when(resenaRepository.findById(1)).thenReturn(Optional.of(actual));
        when(estudianteRepository.getReferenceById(10)).thenReturn(estudiante(10));
        when(claseRepository.getReferenceById(7)).thenReturn(clase(7));
        when(resenaRepository.save(actual)).thenReturn(actual);

        assertTrue(service.actualizar(1, solicitud));
        verify(resenaRepository).save(actual);
    }

    @Test
    void actualizarDevuelveFalseSiLaResenaNoExiste() {
        when(estudianteRepository.existsById(10)).thenReturn(true);
        when(claseRepository.existsById(7)).thenReturn(true);
        when(resenaRepository.findById(99)).thenReturn(Optional.empty());
        assertFalse(service.actualizar(99, solicitud));
    }

    @Test
    void actualizarRechazaUnEstudianteDiferenteAlAutor() {
        ResenaEntity actual = entity(1, 5, 7, null);
        when(estudianteRepository.existsById(10)).thenReturn(true);
        when(claseRepository.existsById(7)).thenReturn(true);
        when(resenaRepository.findById(1)).thenReturn(Optional.of(actual));
        assertThrows(BusinessException.class, () -> service.actualizar(1, solicitud));
        verify(resenaRepository, never()).save(any());
    }

    private ResenaEntity entity(Integer id, Integer studentId, Integer classId, Integer subjectId) {
        ResenaEntity entity = new ResenaEntity();
        entity.setIdResena(id);
        entity.setCalificacionGeneral(new BigDecimal("4.5"));
        entity.setMetodologiaEnsenansa(new BigDecimal("4.0"));
        entity.setNivelExigencia(new BigDecimal("3.5"));
        entity.setComentario("Buena experiencia");
        entity.setFechaCreacion(LocalDateTime.now());
        entity.setEstudiante(estudiante(studentId));
        entity.setClase(classId == null ? null : clase(classId));
        if (subjectId != null) {
            MateriaEntity materia = new MateriaEntity();
            materia.setIdMateria(subjectId);
            entity.setMateria(materia);
        }
        return entity;
    }

    private EstudianteEntity estudiante(Integer id) {
        EstudianteEntity entity = new EstudianteEntity();
        entity.setIdEstudiante(id);
        return entity;
    }

    private ClaseEntity clase(Integer id) {
        ClaseEntity entity = new ClaseEntity();
        entity.setIdClase(id);
        return entity;
    }
}
