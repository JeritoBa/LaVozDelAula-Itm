package com.lavozdelaula.repositories;

import com.lavozdelaula.entities.jpa.ClaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClaseRepository extends JpaRepository<ClaseEntity, Integer> {

    List<ClaseEntity> findByDocenteIdDocente(Integer idDocente);

    List<ClaseEntity> findByMateriaIdMateria(Integer idMateria);
}
