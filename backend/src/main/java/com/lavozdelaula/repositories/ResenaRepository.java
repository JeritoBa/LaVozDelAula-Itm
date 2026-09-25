package com.lavozdelaula.repositories;

import com.lavozdelaula.entities.Resena;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
public class ResenaRepository {

    private final JdbcTemplate jdbcTemplate;

    public ResenaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Resena save(Resena resena) {
        String sql = "INSERT INTO resena " +
                "(calificacion_general, metodologia_ensenansa, nivel_exigencia, comentario, " +
                "anonima, id_estudiante, id_clase, id_materia) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setBigDecimal(1, resena.getCalificacionGeneral());
            ps.setBigDecimal(2, resena.getMetodologiaEnsenansa());
            ps.setBigDecimal(3, resena.getNivelExigencia());
            ps.setString(4, resena.getComentario());
            ps.setBoolean(5, resena.isAnonima());
            ps.setInt(6, resena.getIdEstudiante());

            if (resena.getIdClase() != null) {
                ps.setInt(7, resena.getIdClase());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            if (resena.getIdMateria() != null) {
                ps.setInt(8, resena.getIdMateria());
            } else {
                ps.setNull(8, Types.INTEGER);
            }

            return ps;
        }, keyHolder);

        resena.setIdResena(keyHolder.getKey().intValue());
        return resena;
    }

    public Optional<Resena> findById(Integer idResena) {
        String sql = "SELECT * FROM resena WHERE id_resena = ?";
        List<Resena> resultado = jdbcTemplate.query(sql, this::mapRow, idResena);
        return resultado.stream().findFirst();
    }

    public List<Resena> findAll() {
        String sql = "SELECT * FROM resena ORDER BY fecha_creacion DESC";
        return jdbcTemplate.query(sql, this::mapRow);
    }

    public List<Resena> findByEstudiante(Integer idEstudiante) {
        String sql = "SELECT * FROM resena WHERE id_estudiante = ? ORDER BY fecha_creacion DESC";
        return jdbcTemplate.query(sql, this::mapRow, idEstudiante);
    }

    public List<Resena> findByClase(Integer idClase) {
        String sql = "SELECT * FROM resena WHERE id_clase = ? ORDER BY fecha_creacion DESC";
        return jdbcTemplate.query(sql, this::mapRow, idClase);
    }

    public List<Resena> findByMateria(Integer idMateria) {
        String sql = "SELECT * FROM resena WHERE id_materia = ? ORDER BY fecha_creacion DESC";
        return jdbcTemplate.query(sql, this::mapRow, idMateria);
    }

    public boolean deleteById(Integer idResena) {
        String sql = "DELETE FROM resena WHERE id_resena = ?";
        return jdbcTemplate.update(sql, idResena) > 0;
    }

    private Resena mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Resena resena = new Resena();
        resena.setIdResena(rs.getInt("id_resena"));
        resena.setCalificacionGeneral(rs.getBigDecimal("calificacion_general"));
        resena.setMetodologiaEnsenansa(rs.getBigDecimal("metodologia_ensenansa"));
        resena.setNivelExigencia(rs.getBigDecimal("nivel_exigencia"));
        resena.setComentario(rs.getString("comentario"));
        resena.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        resena.setAnonima(rs.getBoolean("anonima"));
        resena.setIdEstudiante(rs.getInt("id_estudiante"));

        int idClase = rs.getInt("id_clase");
        resena.setIdClase(rs.wasNull() ? null : idClase);

        int idMateria = rs.getInt("id_materia");
        resena.setIdMateria(rs.wasNull() ? null : idMateria);

        return resena;
    }
}