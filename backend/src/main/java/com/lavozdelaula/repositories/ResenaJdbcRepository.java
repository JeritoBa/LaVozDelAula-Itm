package com.lavozdelaula.repositories;

import com.lavozdelaula.entities.Resena;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResenaJdbcRepository {

    private static final String SELECT_COLUMNS = "r.id_resena, r.calificacion_general, r.metodologia_ensenansa, " +
            "r.nivel_exigencia, r.comentario, r.fecha_creacion, r.anonima, r.id_estudiante, r.id_clase, r.id_materia";

    private final JdbcTemplate jdbcTemplate;

    public ResenaJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Resena> findAll() {
        String sql = "SELECT " + SELECT_COLUMNS + " FROM resena r ORDER BY r.fecha_creacion DESC";
        return jdbcTemplate.query(sql, this::mapRow);
    }

    public List<Resena> findByEstudiante(Integer idEstudiante) {
        String sql = "SELECT " + SELECT_COLUMNS + " FROM resena r " +
                "WHERE r.id_estudiante = ? ORDER BY r.fecha_creacion DESC";
        return jdbcTemplate.query(sql, this::mapRow, idEstudiante);
    }

    public List<Resena> findByClase(Integer idClase) {
        String sql = "SELECT " + SELECT_COLUMNS + " FROM resena r " +
                "WHERE r.id_clase = ? ORDER BY r.fecha_creacion DESC";
        return jdbcTemplate.query(sql, this::mapRow, idClase);
    }

    public List<Resena> findByMateria(Integer idMateria) {
        String sql = "SELECT " + SELECT_COLUMNS + " FROM resena r " +
                "LEFT JOIN clase c ON c.id_clase = r.id_clase " +
                "WHERE r.id_materia = ? OR c.id_materia = ? ORDER BY r.fecha_creacion DESC";
        return jdbcTemplate.query(sql, this::mapRow, idMateria, idMateria);
    }

    public List<Resena> findByDocente(Integer idDocente) {
        String sql = "SELECT " + SELECT_COLUMNS + " FROM resena r " +
                "JOIN clase c ON c.id_clase = r.id_clase " +
                "WHERE c.id_docente = ? ORDER BY r.fecha_creacion DESC";
        return jdbcTemplate.query(sql, this::mapRow, idDocente);
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
        Integer idEstudiante = rs.getInt("id_estudiante");
        resena.setIdEstudiante(resena.isAnonima() ? null : idEstudiante);
        resena.setIdClase(rs.getObject("id_clase", Integer.class));
        resena.setIdMateria(rs.getObject("id_materia", Integer.class));
        return resena;
    }
}
