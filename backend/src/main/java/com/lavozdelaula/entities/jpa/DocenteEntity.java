package com.lavozdelaula.entities.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "docente")
public class DocenteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_docente")
    private Integer idDocente;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "departamento_o_facultad", length = 150)
    private String departamentoOFacultad;

    @Column(name = "correo_institucional", nullable = false, unique = true, length = 150)
    private String correoInstitucional;

    @OneToMany(mappedBy = "docente")
    private List<ClaseEntity> clases = new ArrayList<>();

    public Integer getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(Integer idDocente) {
        this.idDocente = idDocente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDepartamentoOFacultad() {
        return departamentoOFacultad;
    }

    public void setDepartamentoOFacultad(String departamentoOFacultad) {
        this.departamentoOFacultad = departamentoOFacultad;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }

    public List<ClaseEntity> getClases() {
        return clases;
    }

    public void setClases(List<ClaseEntity> clases) {
        this.clases = clases;
    }
}
