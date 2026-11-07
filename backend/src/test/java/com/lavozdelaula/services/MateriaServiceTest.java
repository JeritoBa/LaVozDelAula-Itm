package com.lavozdelaula.services;

import com.lavozdelaula.dto.MateriaDto;
import com.lavozdelaula.entities.jpa.MateriaEntity;
import com.lavozdelaula.exceptions.ResourceNotFoundException;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MateriaServiceTest {

    @Mock
    private MateriaRepository repository;
    @InjectMocks
    private MateriaService service;

    @Test
    void registrarCreaMateria() {
        when(repository.save(any(MateriaEntity.class))).thenAnswer(invocation -> {
            MateriaEntity entity = invocation.getArgument(0);
            entity.setIdMateria(1);
            return entity;
        });
        MateriaDto result = service.registrar(new MateriaDto(null, "MAT-BD-01", "Bases de datos", "Ingeniería"));
        assertEquals(1, result.idMateria());
        assertEquals("Bases de datos", result.nombreMateria());
    }

    @Test
    void obtenerTodasDevuelveLosRegistros() {
        when(repository.findAll()).thenReturn(List.of(materia(1)));
        assertEquals(1, service.obtenerTodas().size());
    }

    @Test
    void obtenerPorIdDevuelveMateriaExistente() {
        when(repository.findById(1)).thenReturn(Optional.of(materia(1)));
        assertEquals("Bases de datos", service.obtenerPorId(1).nombreMateria());
    }

    @Test
    void obtenerPorIdLanzaNotFound() {
        when(repository.findById(10)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorId(10));
    }

    private MateriaEntity materia(Integer id) {
        MateriaEntity entity = new MateriaEntity();
        entity.setIdMateria(id);
        entity.setCodigoMateria("MAT-BD-01");
        entity.setNombreMateria("Bases de datos");
        entity.setFacultad("Ingeniería");
        return entity;
    }
}
