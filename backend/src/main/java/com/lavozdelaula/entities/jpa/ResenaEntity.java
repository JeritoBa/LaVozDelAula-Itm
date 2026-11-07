package com.lavozdelaula.entities.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "resena")
public class ResenaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resena")
    private Integer idResena;

    @Column(name = "calificacion_general", nullable = false, precision = 3, scale = 1)
    private BigDecimal calificacionGeneral;

    @Column(name = "metodologia_ensenansa", precision = 3, scale = 1)
    private BigDecimal metodologiaEnsenansa;

    @Column(name = "nivel_exigencia", precision = 3, scale = 1)
    private BigDecimal nivelExigencia;

    @Column(name = "comentario", columnDefinition = "text")
    private String comentario;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "anonima", nullable = false)
    private boolean anonima;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_estudiante", nullable = false)
    private EstudianteEntity estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_clase")
    private ClaseEntity clase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_materia")
    private MateriaEntity materia;

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

    public EstudianteEntity getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(EstudianteEntity estudiante) {
        this.estudiante = estudiante;
    }

    public ClaseEntity getClase() {
        return clase;
    }

    public void setClase(ClaseEntity clase) {
        this.clase = clase;
    }

    public MateriaEntity getMateria() {
        return materia;
    }

    public void setMateria(MateriaEntity materia) {
        this.materia = materia;
    }
}
