package com.lavozdelaula.services;

import com.lavozdelaula.entities.Resena;
import com.lavozdelaula.repositories.ResenaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResenaService {

    private final ResenaRepository resenaRepository;

    public ResenaService(ResenaRepository resenaRepository) {
        this.resenaRepository = resenaRepository;
    }

    public Resena crear(Resena resena) {
        validarClaseOMateria(resena);
        return resenaRepository.save(resena);
    }

    public List<Resena> obtenerTodas() {
        return resenaRepository.findAll();
    }

    public Optional<Resena> obtenerPorId(Integer idResena) {
        return resenaRepository.findById(idResena);
    }

    public List<Resena> obtenerPorEstudiante(Integer idEstudiante) {
        return resenaRepository.findByEstudiante(idEstudiante);
    }

    public List<Resena> obtenerPorClase(Integer idClase) {
        return resenaRepository.findByClase(idClase);
    }

    public List<Resena> obtenerPorMateria(Integer idMateria) {
        return resenaRepository.findByMateria(idMateria);
    }

    public boolean eliminar(Integer idResena) {
        return resenaRepository.deleteById(idResena);
    }

    private void validarClaseOMateria(Resena resena) {
        boolean tieneClase = resena.getIdClase() != null;
        boolean tieneMateria = resena.getIdMateria() != null;

        if (tieneClase == tieneMateria) {
            throw new IllegalArgumentException(
                    "idClase e idMateria no pueden ser ambos nulos o ambos con valor"
            );
        }
    }
    public boolean actualizar(Integer idResena, Resena resena) {
        validarClaseOMateria(resena);
        resena.setIdResena(idResena);
        return resenaRepository.update(resena);
    }
}
