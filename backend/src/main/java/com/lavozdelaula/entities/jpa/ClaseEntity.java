package com.lavozdelaula.entities.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clase")
public class ClaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clase")
    private Integer idClase;

    @Column(name = "jornada", length = 50)
    private String jornada;

    @Column(name = "codigo", nullable = false, length = 50)
    private String codigo;

    @Column(name = "horario", length = 100)
    private String horario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_docente", nullable = false)
    private DocenteEntity docente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_materia", nullable = false)
    private MateriaEntity materia;

    @OneToMany(mappedBy = "clase")
    private List<ResenaEntity> resenas = new ArrayList<>();

    public Integer getIdClase() {
        return idClase;
    }

    public void setIdClase(Integer idClase) {
        this.idClase = idClase;
    }

    public String getJornada() {
        return jornada;
    }

    public void setJornada(String jornada) {
        this.jornada = jornada;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public DocenteEntity getDocente() {
        return docente;
    }

    public void setDocente(DocenteEntity docente) {
        this.docente = docente;
    }

    public MateriaEntity getMateria() {
        return materia;
    }

    public void setMateria(MateriaEntity materia) {
        this.materia = materia;
    }

    public List<ResenaEntity> getResenas() {
        return resenas;
    }

    public void setResenas(List<ResenaEntity> resenas) {
        this.resenas = resenas;
    }
}
