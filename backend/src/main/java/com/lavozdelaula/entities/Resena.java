package com.lavozdelaula.entities;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Resena {

    @Schema(description = "Identificador generado por la base de datos", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idResena;

    @Schema(description = "Calificación general del docente, de 0 a 5", example = "4.5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La calificación general es obligatoria")
    @DecimalMin(value = "0.0", inclusive = true, message = "La calificación general debe ser mayor o igual a 0")
    @DecimalMax(value = "5.0", inclusive = true, message = "La calificación general debe ser menor o igual a 5")
    private BigDecimal calificacionGeneral;

    @Schema(description = "Percepción sobre la metodología de enseñanza, de 0 a 5", example = "4.0")
    @DecimalMin(value = "0.0", inclusive = true, message = "La metodología debe ser mayor o igual a 0")
    @DecimalMax(value = "5.0", inclusive = true, message = "La metodología debe ser menor o igual a 5")
    private BigDecimal metodologiaEnsenansa;

    @Schema(description = "Percepción sobre el nivel de exigencia, de 0 a 5", example = "3.5")
    @DecimalMin(value = "0.0", inclusive = true, message = "El nivel de exigencia debe ser mayor o igual a 0")
    @DecimalMax(value = "5.0", inclusive = true, message = "El nivel de exigencia debe ser menor o igual a 5")
    private BigDecimal nivelExigencia;

    @Schema(description = "Comentario opcional de la experiencia académica")
    @Size(max = 10000, message = "El comentario no puede superar 10000 caracteres")
    private String comentario;

    @Schema(description = "Fecha de creación asignada por el backend", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaCreacion;
    @Schema(description = "Define si la reseña se muestra de forma anónima", example = "false")
    private boolean anonima;

    @Schema(description = "Estudiante autor de la reseña", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El estudiante es obligatorio")
    private Integer idEstudiante;
    @Schema(description = "Clase asociada; debe indicarse solo esta o idMateria")
    private Integer idClase;
    @Schema(description = "Materia asociada; debe indicarse solo esta o idClase")
    private Integer idMateria;

    public Resena() {
    }

    public Resena(Integer idResena, BigDecimal calificacionGeneral, BigDecimal metodologiaEnsenansa,
                  BigDecimal nivelExigencia, String comentario, LocalDateTime fechaCreacion,
                  boolean anonima, Integer idEstudiante, Integer idClase, Integer idMateria) {
        this.idResena = idResena;
        this.calificacionGeneral = calificacionGeneral;
        this.metodologiaEnsenansa = metodologiaEnsenansa;
        this.nivelExigencia = nivelExigencia;
        this.comentario = comentario;
        this.fechaCreacion = fechaCreacion;
        this.anonima = anonima;
        this.idEstudiante = idEstudiante;
        this.idClase = idClase;
        this.idMateria = idMateria;
    }

    public Integer getIdResena() {
        return idResena;
    }

    public void setIdResena(Integer idResena) {
        this.idResena = idResena;
    }

    public BigDecimal getCalificacionGeneral() {
        return calificacionGeneral;
    }

    public void setCalificacionGeneral(BigDecimal calificacionGeneral) {
        this.calificacionGeneral = calificacionGeneral;
    }

    public BigDecimal getMetodologiaEnsenansa() {
        return metodologiaEnsenansa;
    }

    public void setMetodologiaEnsenansa(BigDecimal metodologiaEnsenansa) {
        this.metodologiaEnsenansa = metodologiaEnsenansa;
    }

    public BigDecimal getNivelExigencia() {
        return nivelExigencia;
    }

    public void setNivelExigencia(BigDecimal nivelExigencia) {
        this.nivelExigencia = nivelExigencia;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isAnonima() {
        return anonima;
    }

    public void setAnonima(boolean anonima) {
        this.anonima = anonima;
    }

    public Integer getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public Integer getIdClase() {
        return idClase;
    }

    public void setIdClase(Integer idClase) {
        this.idClase = idClase;
    }

    public Integer getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }
}