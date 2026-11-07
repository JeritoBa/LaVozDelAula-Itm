package com.lavozdelaula.services;

import com.lavozdelaula.dto.ClaseDto;
import com.lavozdelaula.entities.jpa.ClaseEntity;
import com.lavozdelaula.entities.jpa.DocenteEntity;
import com.lavozdelaula.entities.jpa.MateriaEntity;
import com.lavozdelaula.exceptions.ResourceNotFoundException;
import com.lavozdelaula.repositories.ClaseRepository;
import com.lavozdelaula.repositories.DocenteRepository;
import com.lavozdelaula.repositories.MateriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaseServiceTest {

    @Mock
    private ClaseRepository claseRepository;
    @Mock
    private DocenteRepository docenteRepository;
    @Mock
    private MateriaRepository materiaRepository;
    @InjectMocks
    private ClaseService service;

    @Test
    void asignarDocenteMateriaRegistraLaRelacion() {
        when(docenteRepository.findById(2)).thenReturn(Optional.of(docente(2)));
        when(materiaRepository.findById(3)).thenReturn(Optional.of(materia(3)));
        when(claseRepository.save(any(ClaseEntity.class))).thenAnswer(invocation -> {
            ClaseEntity entity = invocation.getArgument(0);
            entity.setIdClase(1);
            return entity;
        });
        ClaseDto result = service.asignarDocenteMateria(input());
        assertEquals(1, result.idClase());
        assertEquals(2, result.idDocente());
        assertEquals(3, result.idMateria());
    }

    @Test
    void asignarDocenteMateriaRechazaDocenteInexistente() {
        when(docenteRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.asignarDocenteMateria(input()));
        verifyNoInteractions(claseRepository);
    }

    @Test
    void asignarDocenteMateriaRechazaMateriaInexistente() {
        when(docenteRepository.findById(2)).thenReturn(Optional.of(docente(2)));
        when(materiaRepository.findById(3)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.asignarDocenteMateria(input()));
    }

    @Test
    void obtenerTodasDevuelveClases() {
        when(claseRepository.findAll()).thenReturn(List.of(clase(1)));
        assertEquals(1, service.obtenerTodas().size());
    }

    @Test
    void obtenerPorIdDevuelveClaseExistente() {
        when(claseRepository.findById(1)).thenReturn(Optional.of(clase(1)));
        assertEquals(1, service.obtenerPorId(1).idClase());
    }

    @Test
    void obtenerPorIdLanzaNotFound() {
        when(claseRepository.findById(9)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorId(9));
    }

    @Test
    void obtenerPorDocenteValidaYDevuelveClases() {
        when(docenteRepository.existsById(2)).thenReturn(true);
        when(claseRepository.findByDocenteIdDocente(2)).thenReturn(List.of(clase(1)));
        assertEquals(1, service.obtenerPorDocente(2).size());
    }

    @Test
    void obtenerPorDocenteLanzaNotFoundSiNoExiste() {
        when(docenteRepository.existsById(2)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorDocente(2));
    }

    @Test
    void obtenerPorMateriaValidaYDevuelveClases() {
        when(materiaRepository.existsById(3)).thenReturn(true);
        when(claseRepository.findByMateriaIdMateria(3)).thenReturn(List.of(clase(1)));
        assertEquals(1, service.obtenerPorMateria(3).size());
    }

    @Test
    void obtenerPorMateriaLanzaNotFoundSiNoExiste() {
        when(materiaRepository.existsById(3)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorMateria(3));
    }

    private ClaseDto input() {
        return new ClaseDto(null, "Diurna", "BD-01", "Lunes 8-10", 2, 3);
    }

    private ClaseEntity clase(Integer id) {
        ClaseEntity entity = new ClaseEntity();
        entity.setIdClase(id);
        entity.setCodigo("BD-01");
        entity.setDocente(docente(2));
        entity.setMateria(materia(3));
        return entity;
    }

    private DocenteEntity docente(Integer id) {
        DocenteEntity entity = new DocenteEntity();
        entity.setIdDocente(id);
        return entity;
    }

    private MateriaEntity materia(Integer id) {
        MateriaEntity entity = new MateriaEntity();
        entity.setIdMateria(id);
        return entity;
    }
}
