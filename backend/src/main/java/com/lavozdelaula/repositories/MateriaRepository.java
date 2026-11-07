package com.lavozdelaula.repositories;

import com.lavozdelaula.entities.jpa.MateriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateriaRepository extends JpaRepository<MateriaEntity, Integer> {
}
