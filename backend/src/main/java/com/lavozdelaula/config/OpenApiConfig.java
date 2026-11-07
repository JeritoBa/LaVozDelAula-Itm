package com.lavozdelaula.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI laVozDelAulaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("La Voz del Aula API")
                        .description("API REST para gestión de reseñas, experiencias y valoración docente.")
                        .version("1.0.0"));
    }
}
