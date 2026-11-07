package com.lavozdelaula.services;

import com.lavozdelaula.dto.DocenteDto;
import com.lavozdelaula.entities.jpa.DocenteEntity;
import com.lavozdelaula.exceptions.ResourceNotFoundException;
import com.lavozdelaula.repositories.DocenteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocenteServiceTest {

    @Mock
    private DocenteRepository repository;
    @InjectMocks
    private DocenteService service;

    @Test
    void registrarCreaDocente() {
        when(repository.save(any(DocenteEntity.class))).thenAnswer(invocation -> {
            DocenteEntity entity = invocation.getArgument(0);
            entity.setIdDocente(1);
            return entity;
        });
        DocenteDto result = service.registrar(new DocenteDto(null, "Carlos Gómez", "Ingeniería", "carlos@itm.edu.co"));
        assertEquals(1, result.idDocente());
        assertEquals("Carlos Gómez", result.nombre());
    }

    @Test
    void obtenerTodosDevuelveLosRegistros() {
        when(repository.findAll()).thenReturn(List.of(docente(1)));
        assertEquals(1, service.obtenerTodos().size());
    }

    @Test
    void obtenerPorIdDevuelveDocenteExistente() {
        when(repository.findById(1)).thenReturn(Optional.of(docente(1)));
        assertEquals("carlos@itm.edu.co", service.obtenerPorId(1).correoInstitucional());
    }

    @Test
    void obtenerPorIdLanzaNotFound() {
        when(repository.findById(10)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorId(10));
    }

    private DocenteEntity docente(Integer id) {
        DocenteEntity entity = new DocenteEntity();
        entity.setIdDocente(id);
        entity.setNombre("Carlos Gómez");
        entity.setDepartamentoOFacultad("Ingeniería");
        entity.setCorreoInstitucional("carlos@itm.edu.co");
        return entity;
    }
}
