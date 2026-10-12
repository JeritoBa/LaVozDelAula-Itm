
package com.lavozdelaula.services;

import com.lavozdelaula.entities.Resena;

import java.util.List;
import java.util.Optional;

public interface ResenaService {

    Resena crear(Resena resena);

    List<Resena> obtenerTodas();

    Optional<Resena> obtenerPorId(Integer idResena);

    List<Resena> obtenerPorEstudiante(Integer idEstudiante);

    List<Resena> obtenerPorClase(Integer idClase);

    List<Resena> obtenerPorMateria(Integer idMateria);
    boolean actualizar(Integer idResena, Resena resena);


    boolean eliminar(Integer idResena);
}
