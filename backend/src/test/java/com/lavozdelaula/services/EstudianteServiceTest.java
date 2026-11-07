package com.lavozdelaula.services;

import com.lavozdelaula.dto.EstudianteDto;
import com.lavozdelaula.dto.PerfilEstudianteDto;
import com.lavozdelaula.entities.jpa.EstudianteEntity;
import com.lavozdelaula.entities.jpa.PerfilEstudianteEntity;
import com.lavozdelaula.exceptions.ResourceNotFoundException;
import com.lavozdelaula.repositories.EstudianteRepository;
import com.lavozdelaula.repositories.ResenaRepository;
import com.lavozdelaula.entities.jpa.DocenteEntity;
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
class EstudianteServiceTest {

    @Mock
    private EstudianteRepository repository;
    @Mock
    private ResenaRepository resenaRepository;
    @InjectMocks
    private EstudianteService service;

    @Test
    void registrarCreaEstudianteYPerfilOpcional() {
        EstudianteDto input = new EstudianteDto(null, "EST-1", "Ana Pérez", "ana@itm.edu.co",
                "Sistemas", new PerfilEstudianteDto("3001234567", "https://example.test/foto.jpg"));
        when(repository.save(any(EstudianteEntity.class))).thenAnswer(invocation -> {
            EstudianteEntity entity = invocation.getArgument(0);
            entity.setIdEstudiante(1);
            return entity;
        });

        EstudianteDto result = service.registrar(input);

        assertEquals(1, result.idEstudiante());
        assertEquals("Ana Pérez", result.nombre());
        assertEquals("3001234567", result.perfil().telefono());
    }

    @Test
    void obtenerTodosDevuelveLosRegistros() {
        EstudianteEntity entity = estudiante(1);
        when(repository.findAll()).thenReturn(List.of(entity));
        assertEquals(1, service.obtenerTodos().size());
    }

    @Test
    void obtenerPorIdDevuelveEstudianteExistente() {
        when(repository.findById(1)).thenReturn(Optional.of(estudiante(1)));
        assertEquals(1, service.obtenerPorId(1).idEstudiante());
    }

    @Test
    void obtenerPorIdLanzaNotFound() {
        when(repository.findById(20)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorId(20));
    }

    @Test
    void obtenerDocentesRelacionadosValidaEstudianteYDevuelveDocentes() {
        DocenteEntity docente = new DocenteEntity();
        docente.setIdDocente(7);
        docente.setNombre("Carlos");
        docente.setCorreoInstitucional("carlos@itm.edu.co");
        when(repository.existsById(1)).thenReturn(true);
        when(resenaRepository.findRelatedTeachers(1)).thenReturn(List.of(docente));

        assertEquals(7, service.obtenerDocentesRelacionados(1).get(0).idDocente());
    }

    @Test
    void obtenerDocentesRelacionadosLanzaNotFoundSiEstudianteNoExiste() {
        when(repository.existsById(20)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.obtenerDocentesRelacionados(20));
        verifyNoInteractions(resenaRepository);
    }

    private EstudianteEntity estudiante(Integer id) {
        EstudianteEntity entity = new EstudianteEntity();
        entity.setIdEstudiante(id);
        entity.setCodigoInstitucional("EST-" + id);
        entity.setNombre("Ana Pérez");
        entity.setCorreoInstitucional("ana@itm.edu.co");
        entity.setProgramaAcademico("Sistemas");
        return entity;
    }
}
