package com.lavozdelaula.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Resena {

    private Integer idResena;
    private BigDecimal calificacionGeneral;
    private BigDecimal metodologiaEnsenansa;
    private BigDecimal nivelExigencia;
    private String comentario;
    private LocalDateTime fechaCreacion;
    private boolean anonima;
    private Integer idEstudiante;
    private Integer idClase;   // nullable
    private Integer idMateria; // nullable

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