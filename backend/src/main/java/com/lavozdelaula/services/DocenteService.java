package com.lavozdelaula.services;

import com.lavozdelaula.dto.DocenteDto;
import com.lavozdelaula.entities.jpa.DocenteEntity;
import com.lavozdelaula.exceptions.ResourceNotFoundException;
import com.lavozdelaula.repositories.DocenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DocenteService {

    private final DocenteRepository docenteRepository;

    public DocenteService(DocenteRepository docenteRepository) {
        this.docenteRepository = docenteRepository;
    }

    public DocenteDto registrar(DocenteDto solicitud) {
        DocenteEntity docente = new DocenteEntity();
        docente.setNombre(solicitud.nombre());
        docente.setDepartamentoOFacultad(solicitud.departamentoOFacultad());
        docente.setCorreoInstitucional(solicitud.correoInstitucional());
        return toDto(docenteRepository.save(docente));
    }

    @Transactional(readOnly = true)
    public List<DocenteDto> obtenerTodos() {
        return docenteRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public DocenteDto obtenerPorId(Integer idDocente) {
        return docenteRepository.findById(idDocente)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Docente", idDocente));
    }

    private DocenteDto toDto(DocenteEntity docente) {
        return new DocenteDto(
                docente.getIdDocente(),
                docente.getNombre(),
                docente.getDepartamentoOFacultad(),
                docente.getCorreoInstitucional());
    }
}
