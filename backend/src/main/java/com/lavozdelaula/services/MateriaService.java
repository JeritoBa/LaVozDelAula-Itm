package com.lavozdelaula.services;

import com.lavozdelaula.entities.Materia;

import java.util.List;
import java.util.Optional;

public interface MateriaService {

    List<Materia> obtenerTodas();

    Optional<Materia> obtenerPorId(Integer idMateria);
}