package com.lavozdelaula.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EstudianteDto(
        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        Integer idEstudiante,
        @NotBlank @Size(max = 50)
        String codigoInstitucional,
        @NotBlank @Size(max = 150)
        String nombre,
        @NotBlank @Email @Size(max = 150)
        String correoInstitucional,
        @Size(max = 150)
        String programaAcademico,
        @Valid
        PerfilEstudianteDto perfil) {
}
