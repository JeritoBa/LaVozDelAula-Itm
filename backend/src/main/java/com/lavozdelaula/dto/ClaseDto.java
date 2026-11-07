package com.lavozdelaula.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClaseDto(
        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        Integer idClase,
        @Size(max = 50)
        String jornada,
        @NotBlank @Size(max = 50)
        String codigo,
        @Size(max = 100)
        String horario,
        @NotNull
        Integer idDocente,
        @NotNull
        Integer idMateria) {
}
