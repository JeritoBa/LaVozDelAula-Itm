package com.lavozdelaula.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MateriaDto(
        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        Integer idMateria,
        @NotBlank @Size(max = 50)
        String codigoMateria,
        @NotBlank @Size(max = 150)
        String nombreMateria,
        @Size(max = 150)
        String facultad) {
}
