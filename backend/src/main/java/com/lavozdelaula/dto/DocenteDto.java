package com.lavozdelaula.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DocenteDto(
        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        Integer idDocente,
        @NotBlank @Size(max = 150)
        String nombre,
        @Size(max = 150)
        String departamentoOFacultad,
        @NotBlank @Email @Size(max = 150)
        String correoInstitucional) {
}
