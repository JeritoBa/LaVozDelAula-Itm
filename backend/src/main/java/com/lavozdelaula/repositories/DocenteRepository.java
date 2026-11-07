package com.lavozdelaula.repositories;

import com.lavozdelaula.entities.jpa.DocenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocenteRepository extends JpaRepository<DocenteEntity, Integer> {
}
