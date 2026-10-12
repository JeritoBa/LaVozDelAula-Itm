package com.lavozdelaula.repositories;

import com.lavozdelaula.entities.Materia;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MateriaRepository {

    private final JdbcTemplate jdbcTemplate;

    public MateriaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Materia> findAll() {
        String sql = "SELECT id_materia, nombre_materia, facultad " +
                "FROM materia ORDER BY nombre_materia";

        return jdbcTemplate.query(sql, this::mapRow);
    }

    public Optional<Materia> findById(Integer idMateria) {
        String sql = "SELECT id_materia, nombre_materia, facultad " +
                "FROM materia WHERE id_materia = ?";

        List<Materia> resultado = jdbcTemplate.query(
                sql,
                this::mapRow,
                idMateria
        );

        return resultado.stream().findFirst();
    }

    private Materia mapRow(java.sql.ResultSet rs, int rowNum)
            throws java.sql.SQLException {
        Materia materia = new Materia();
        materia.setIdMateria(rs.getInt("id_materia"));
        materia.setNombreMateria(rs.getString("nombre_materia"));
        materia.setFacultad(rs.getString("facultad"));
        return materia;
    }
}