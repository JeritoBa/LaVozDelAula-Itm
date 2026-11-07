package com.lavozdelaula.entities.jpa;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estudiante")
public class EstudianteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudiante")
    private Integer idEstudiante;

    @Column(name = "codigo_institucional", nullable = false, unique = true, length = 50)
    private String codigoInstitucional;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "correo_institucional", nullable = false, unique = true, length = 150)
    private String correoInstitucional;

    @Column(name = "programa_academico", length = 150)
    private String programaAcademico;

    @OneToMany(mappedBy = "estudiante")
    private List<ResenaEntity> resenas = new ArrayList<>();

    @OneToOne(mappedBy = "estudiante", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private PerfilEstudianteEntity perfil;

    public Integer getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getCodigoInstitucional() {
        return codigoInstitucional;
    }

    public void setCodigoInstitucional(String codigoInstitucional) {
        this.codigoInstitucional = codigoInstitucional;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }

    public String getProgramaAcademico() {
        return programaAcademico;
    }

    public void setProgramaAcademico(String programaAcademico) {
        this.programaAcademico = programaAcademico;
    }

    public List<ResenaEntity> getResenas() {
        return resenas;
    }

    public void setResenas(List<ResenaEntity> resenas) {
        this.resenas = resenas;
    }

    public PerfilEstudianteEntity getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilEstudianteEntity perfil) {
        this.perfil = perfil;
    }
}
