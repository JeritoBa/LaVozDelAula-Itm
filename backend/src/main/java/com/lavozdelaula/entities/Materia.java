package com.lavozdelaula.entities;

public class Materia {

    private Integer idMateria;
    private String nombreMateria;
    private String facultad;

    public Materia() {
    }

    public Materia(Integer idMateria, String nombreMateria, String facultad) {
        this.idMateria = idMateria;
        this.nombreMateria = nombreMateria;
        this.facultad = facultad;
    }

    public Integer getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }

    public String getNombreMateria() {
        return nombreMateria;
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
}