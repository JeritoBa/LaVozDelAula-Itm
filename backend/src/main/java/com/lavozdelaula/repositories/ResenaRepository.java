package com.lavozdelaula.repositories;

import com.lavozdelaula.entities.jpa.ResenaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<ResenaEntity, Integer> {

    @Query("select distinct r.clase.docente from ResenaEntity r " +
            "where r.estudiante.idEstudiante = :idEstudiante and r.clase is not null")
    List<com.lavozdelaula.entities.jpa.DocenteEntity> findRelatedTeachers(
            @Param("idEstudiante") Integer idEstudiante);
}
