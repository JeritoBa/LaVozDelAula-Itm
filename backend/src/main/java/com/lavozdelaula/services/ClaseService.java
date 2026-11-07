package com.lavozdelaula.services;

import com.lavozdelaula.dto.ClaseDto;
import com.lavozdelaula.entities.jpa.ClaseEntity;
import com.lavozdelaula.entities.jpa.DocenteEntity;
import com.lavozdelaula.entities.jpa.MateriaEntity;
import com.lavozdelaula.exceptions.ResourceNotFoundException;
import com.lavozdelaula.repositories.ClaseRepository;
import com.lavozdelaula.repositories.DocenteRepository;
import com.lavozdelaula.repositories.MateriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClaseService {

    private final ClaseRepository claseRepository;
    private final DocenteRepository docenteRepository;
    private final MateriaRepository materiaRepository;

    public ClaseService(
            ClaseRepository claseRepository,
            DocenteRepository docenteRepository,
            MateriaRepository materiaRepository) {
        this.claseRepository = claseRepository;
        this.docenteRepository = docenteRepository;
        this.materiaRepository = materiaRepository;
    }

    public ClaseDto asignarDocenteMateria(ClaseDto solicitud) {
        DocenteEntity docente = docenteRepository.findById(solicitud.idDocente())
                .orElseThrow(() -> new ResourceNotFoundException("Docente", solicitud.idDocente()));
        MateriaEntity materia = materiaRepository.findById(solicitud.idMateria())
                .orElseThrow(() -> new ResourceNotFoundException("Materia", solicitud.idMateria()));

        ClaseEntity clase = new ClaseEntity();
        clase.setJornada(solicitud.jornada());
        clase.setCodigo(solicitud.codigo());
        clase.setHorario(solicitud.horario());
        clase.setDocente(docente);
        clase.setMateria(materia);
        return toDto(claseRepository.save(clase));
    }

    @Transactional(readOnly = true)
    public List<ClaseDto> obtenerTodas() {
        return claseRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ClaseDto obtenerPorId(Integer idClase) {
        return claseRepository.findById(idClase)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Clase", idClase));
    }

    @Transactional(readOnly = true)
    public List<ClaseDto> obtenerPorDocente(Integer idDocente) {
        if (!docenteRepository.existsById(idDocente)) {
            throw new ResourceNotFoundException("Docente", idDocente);
        }
        return claseRepository.findByDocenteIdDocente(idDocente).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<ClaseDto> obtenerPorMateria(Integer idMateria) {
        if (!materiaRepository.existsById(idMateria)) {
            throw new ResourceNotFoundException("Materia", idMateria);
        }
        return claseRepository.findByMateriaIdMateria(idMateria).stream().map(this::toDto).toList();
    }

    private ClaseDto toDto(ClaseEntity clase) {
        return new ClaseDto(
                clase.getIdClase(),
                clase.getJornada(),
                clase.getCodigo(),
                clase.getHorario(),
                clase.getDocente().getIdDocente(),
                clase.getMateria().getIdMateria());
    }
}
