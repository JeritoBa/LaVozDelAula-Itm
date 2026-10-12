package com.lavozdelaula.services;

import com.lavozdelaula.entities.Materia;
import com.lavozdelaula.repositories.MateriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MateriaServiceImpl implements MateriaService {

    private final MateriaRepository materiaRepository;

    public MateriaServiceImpl(MateriaRepository materiaRepository) {
        this.materiaRepository = materiaRepository;
    }

    @Override
    public List<Materia> obtenerTodas() {
        return materiaRepository.findAll();
    }

    @Override
    public Optional<Materia> obtenerPorId(Integer idMateria) {
        return materiaRepository.findById(idMateria);
    }
}