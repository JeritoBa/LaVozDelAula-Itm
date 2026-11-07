package com.lavozdelaula.services;

import com.lavozdelaula.entities.Resena;
import com.lavozdelaula.entities.jpa.EstudianteEntity;
import com.lavozdelaula.entities.jpa.ResenaEntity;
import com.lavozdelaula.exceptions.BusinessException;
import com.lavozdelaula.exceptions.ResourceNotFoundException;
import com.lavozdelaula.repositories.ClaseRepository;
import com.lavozdelaula.repositories.DocenteRepository;
import com.lavozdelaula.repositories.EstudianteRepository;
import com.lavozdelaula.repositories.MateriaRepository;
import com.lavozdelaula.repositories.ResenaJdbcRepository;
import com.lavozdelaula.repositories.ResenaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ResenaService {

    private final ResenaRepository resenaRepository;
    private final ResenaJdbcRepository resenaJdbcRepository;
    private final EstudianteRepository estudianteRepository;
    private final ClaseRepository claseRepository;
    private final MateriaRepository materiaRepository;
    private final DocenteRepository docenteRepository;

    public ResenaService(
            ResenaRepository resenaRepository,
            ResenaJdbcRepository resenaJdbcRepository,
            EstudianteRepository estudianteRepository,
            ClaseRepository claseRepository,
            MateriaRepository materiaRepository,
            DocenteRepository docenteRepository) {
        this.resenaRepository = resenaRepository;
        this.resenaJdbcRepository = resenaJdbcRepository;
        this.estudianteRepository = estudianteRepository;
        this.claseRepository = claseRepository;
        this.materiaRepository = materiaRepository;
        this.docenteRepository = docenteRepository;
    }

    public Resena crear(Resena resena) {
        validar(resena);
        ResenaEntity entity = toEntity(resena);
        entity.setFechaCreacion(LocalDateTime.now());
        return toDomain(resenaRepository.save(entity), true);
    }

    @Transactional(readOnly = true)
    public List<Resena> obtenerTodas() {
        return resenaJdbcRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Resena obtenerPorId(Integer idResena) {
        return resenaRepository.findById(idResena)
                .map(entity -> toDomain(entity, false))
                .orElseThrow(() -> new ResourceNotFoundException("Reseña", idResena));
    }

    @Transactional(readOnly = true)
    public List<Resena> obtenerPorEstudiante(Integer idEstudiante) {
        return resenaJdbcRepository.findByEstudiante(idEstudiante);
    }

    @Transactional(readOnly = true)
    public List<Resena> obtenerPorClase(Integer idClase) {
        return resenaJdbcRepository.findByClase(idClase);
    }

    @Transactional(readOnly = true)
    public List<Resena> obtenerPorMateria(Integer idMateria) {
        return resenaJdbcRepository.findByMateria(idMateria);
    }

    @Transactional(readOnly = true)
    public List<Resena> obtenerPorDocente(Integer idDocente) {
        if (!docenteRepository.existsById(idDocente)) {
            throw new ResourceNotFoundException("Docente", idDocente);
        }
        return resenaJdbcRepository.findByDocente(idDocente);
    }

    public boolean eliminar(Integer idResena) {
        if (!resenaRepository.existsById(idResena)) {
            return false;
        }
        resenaRepository.deleteById(idResena);
        return true;
    }

    public boolean actualizar(Integer idResena, Resena resena) {
        validar(resena);
        ResenaEntity actual = resenaRepository.findById(idResena).orElse(null);
        if (actual == null) {
            return false;
        }
        if (!actual.getEstudiante().getIdEstudiante().equals(resena.getIdEstudiante())) {
            throw new BusinessException("Solo el estudiante que creó la reseña puede editarla.");
        }

        actualizarEntidad(actual, resena);
        resenaRepository.save(actual);
        return true;
    }

    private void validar(Resena resena) {
        if (resena == null) {
            throw new BusinessException("El cuerpo de la reseña es obligatorio.");
        }

        boolean tieneClase = resena.getIdClase() != null;
        boolean tieneMateria = resena.getIdMateria() != null;
        if (tieneClase == tieneMateria) {
            throw new BusinessException("Debe indicar exactamente una relación entre idClase e idMateria.");
        }
        if (resena.getIdEstudiante() == null) {
            throw new BusinessException("El campo idEstudiante es obligatorio.");
        }
        if (resena.getCalificacionGeneral() == null) {
            throw new BusinessException("La calificación general es obligatoria.");
        }
        if (!estudianteRepository.existsById(resena.getIdEstudiante())) {
            throw new ResourceNotFoundException("Estudiante", resena.getIdEstudiante());
        }
        if (tieneClase && !claseRepository.existsById(resena.getIdClase())) {
            throw new ResourceNotFoundException("Clase", resena.getIdClase());
        }
        if (tieneMateria && !materiaRepository.existsById(resena.getIdMateria())) {
            throw new ResourceNotFoundException("Materia", resena.getIdMateria());
        }
    }

    private ResenaEntity toEntity(Resena resena) {
        ResenaEntity entity = new ResenaEntity();
        actualizarEntidad(entity, resena);
        return entity;
    }

    private void actualizarEntidad(ResenaEntity entity, Resena resena) {
        entity.setCalificacionGeneral(resena.getCalificacionGeneral());
        entity.setMetodologiaEnsenansa(resena.getMetodologiaEnsenansa());
        entity.setNivelExigencia(resena.getNivelExigencia());
        entity.setComentario(resena.getComentario());
        entity.setAnonima(resena.isAnonima());

        entity.setEstudiante(estudianteRepository.getReferenceById(resena.getIdEstudiante()));

        if (resena.getIdClase() != null) {
            entity.setClase(claseRepository.getReferenceById(resena.getIdClase()));
            entity.setMateria(null);
        } else {
            entity.setMateria(materiaRepository.getReferenceById(resena.getIdMateria()));
            entity.setClase(null);
        }
    }

    private Resena toDomain(ResenaEntity entity, boolean showAuthor) {
        Resena resena = new Resena();
        resena.setIdResena(entity.getIdResena());
        resena.setCalificacionGeneral(entity.getCalificacionGeneral());
        resena.setMetodologiaEnsenansa(entity.getMetodologiaEnsenansa());
        resena.setNivelExigencia(entity.getNivelExigencia());
        resena.setComentario(entity.getComentario());
        resena.setFechaCreacion(entity.getFechaCreacion());
        resena.setAnonima(entity.isAnonima());
        resena.setIdEstudiante(showAuthor || !entity.isAnonima()
                ? entity.getEstudiante().getIdEstudiante()
                : null);
        resena.setIdClase(entity.getClase() == null ? null : entity.getClase().getIdClase());
        resena.setIdMateria(entity.getMateria() == null ? null : entity.getMateria().getIdMateria());
        return resena;
    }
}
