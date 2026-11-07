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
@Table(name = "materia")
public class MateriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_materia")
    private Integer idMateria;

    @Column(name = "codigo_materia", nullable = false, unique = true, length = 50)
    private String codigoMateria;

    @Column(name = "nombre_materia", nullable = false, length = 150)
    private String nombreMateria;

    @Column(name = "facultad", length = 150)
    private String facultad;

    @OneToMany(mappedBy = "materia")
    private List<ClaseEntity> clases = new ArrayList<>();

    @OneToMany(mappedBy = "materia")
    private List<ResenaEntity> resenas = new ArrayList<>();

    public Integer getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public String getCodigoMateria() {
        return codigoMateria;
    }

    public void setCodigoMateria(String codigoMateria) {
        this.codigoMateria = codigoMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public List<ClaseEntity> getClases() {
        return clases;
    }

    public void setClases(List<ClaseEntity> clases) {
        this.clases = clases;
    }

    public List<ResenaEntity> getResenas() {
        return resenas;
    }

    public void setResenas(List<ResenaEntity> resenas) {
        this.resenas = resenas;
    }
}
