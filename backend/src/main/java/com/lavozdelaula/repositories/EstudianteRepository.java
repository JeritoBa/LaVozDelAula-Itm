package com.lavozdelaula.repositories;

import com.lavozdelaula.entities.jpa.EstudianteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteRepository extends JpaRepository<EstudianteEntity, Integer> {
}
