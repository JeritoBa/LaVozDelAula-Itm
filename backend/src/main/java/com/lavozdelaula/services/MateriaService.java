package com.lavozdelaula.services;

import com.lavozdelaula.dto.MateriaDto;
import com.lavozdelaula.entities.jpa.MateriaEntity;
import com.lavozdelaula.exceptions.ResourceNotFoundException;
import com.lavozdelaula.repositories.MateriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MateriaService {

    private final MateriaRepository materiaRepository;

    public MateriaService(MateriaRepository materiaRepository) {
        this.materiaRepository = materiaRepository;
    }

    public MateriaDto registrar(MateriaDto solicitud) {
        MateriaEntity materia = new MateriaEntity();
        materia.setCodigoMateria(solicitud.codigoMateria());
        materia.setNombreMateria(solicitud.nombreMateria());
        materia.setFacultad(solicitud.facultad());
        return toDto(materiaRepository.save(materia));
    }

    @Transactional(readOnly = true)
    public List<MateriaDto> obtenerTodas() {
        return materiaRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public MateriaDto obtenerPorId(Integer idMateria) {
        return materiaRepository.findById(idMateria)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Materia", idMateria));
    }

    private MateriaDto toDto(MateriaEntity materia) {
        return new MateriaDto(
                materia.getIdMateria(),
                materia.getCodigoMateria(),
                materia.getNombreMateria(),
                materia.getFacultad());
    }
}
