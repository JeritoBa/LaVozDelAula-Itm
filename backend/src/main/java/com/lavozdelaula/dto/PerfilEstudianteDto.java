package com.lavozdelaula.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record PerfilEstudianteDto(
        @Size(max = 30) @Schema(description = "Teléfono de contacto opcional")
        String telefono,
        @Size(max = 500) @Schema(description = "URL de la foto de perfil, opcional")
        String fotoUrl) {
}
