package com.lavozdelaula.services;

import com.lavozdelaula.dto.EstudianteDto;
import com.lavozdelaula.dto.DocenteDto;
import com.lavozdelaula.dto.PerfilEstudianteDto;
import com.lavozdelaula.entities.jpa.EstudianteEntity;
import com.lavozdelaula.entities.jpa.PerfilEstudianteEntity;
import com.lavozdelaula.exceptions.ResourceNotFoundException;
import com.lavozdelaula.repositories.EstudianteRepository;
import com.lavozdelaula.repositories.ResenaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final ResenaRepository resenaRepository;

    public EstudianteService(EstudianteRepository estudianteRepository, ResenaRepository resenaRepository) {
        this.estudianteRepository = estudianteRepository;
        this.resenaRepository = resenaRepository;
    }

    public EstudianteDto registrar(EstudianteDto solicitud) {
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setCodigoInstitucional(solicitud.codigoInstitucional());
        estudiante.setNombre(solicitud.nombre());
        estudiante.setCorreoInstitucional(solicitud.correoInstitucional());
        estudiante.setProgramaAcademico(solicitud.programaAcademico());

        if (solicitud.perfil() != null) {
            PerfilEstudianteEntity perfil = new PerfilEstudianteEntity();
            perfil.setTelefono(solicitud.perfil().telefono());
            perfil.setFotoUrl(solicitud.perfil().fotoUrl());
            perfil.setEstudiante(estudiante);
            estudiante.setPerfil(perfil);
        }

        return toDto(estudianteRepository.save(estudiante));
    }

    @Transactional(readOnly = true)
    public List<EstudianteDto> obtenerTodos() {
        return estudianteRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public EstudianteDto obtenerPorId(Integer idEstudiante) {
        return estudianteRepository.findById(idEstudiante)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", idEstudiante));
    }

    @Transactional(readOnly = true)
    public List<DocenteDto> obtenerDocentesRelacionados(Integer idEstudiante) {
        if (!estudianteRepository.existsById(idEstudiante)) {
            throw new ResourceNotFoundException("Estudiante", idEstudiante);
        }
        return resenaRepository.findRelatedTeachers(idEstudiante).stream()
                .map(docente -> new DocenteDto(
                        docente.getIdDocente(),
                        docente.getNombre(),
                        docente.getDepartamentoOFacultad(),
                        docente.getCorreoInstitucional()))
                .toList();
    }

    private EstudianteDto toDto(EstudianteEntity estudiante) {
        PerfilEstudianteDto perfilDto = estudiante.getPerfil() == null
                ? null
                : new PerfilEstudianteDto(estudiante.getPerfil().getTelefono(), estudiante.getPerfil().getFotoUrl());
        return new EstudianteDto(
                estudiante.getIdEstudiante(),
                estudiante.getCodigoInstitucional(),
                estudiante.getNombre(),
                estudiante.getCorreoInstitucional(),
                estudiante.getProgramaAcademico(),
                perfilDto);
    }
}
