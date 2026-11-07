package com.lavozdelaula.entities.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "perfil_estudiante")
public class PerfilEstudianteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil_estudiante")
    private Integer idPerfilEstudiante;

    @Column(name = "telefono", length = 30)
    private String telefono;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_estudiante", nullable = false, unique = true)
    private EstudianteEntity estudiante;

    public Integer getIdPerfilEstudiante() {
        return idPerfilEstudiante;
    }

    public void setIdPerfilEstudiante(Integer idPerfilEstudiante) {
        this.idPerfilEstudiante = idPerfilEstudiante;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public EstudianteEntity getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(EstudianteEntity estudiante) {
        this.estudiante = estudiante;
    }
}
