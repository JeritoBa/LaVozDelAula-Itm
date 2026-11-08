package com.lavozdelaula.controllers;

import com.lavozdelaula.entities.Resena;
import com.lavozdelaula.exceptions.GlobalExceptionHandler;
import com.lavozdelaula.services.ResenaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ResenaControllerTest {
    @Mock
    private ResenaService resenaService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ResenaController(resenaService))
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    void consultaPorMateriaDelegaAlServicio() throws Exception {
        when(resenaService.obtenerPorMateria(12)).thenReturn(List.of(new Resena()));
        mockMvc.perform(get("/api/resenas").param("idMateria", "12"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0]").exists());
        verify(resenaService).obtenerPorMateria(12);
    }

    @Test
    void consultaRechazaMasDeUnFiltro() throws Exception {
        mockMvc.perform(get("/api/resenas").param("idMateria", "12").param("idDocente", "4"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(resenaService);
    }

    @Test
    void crearRechazaCalificacionFueraDeRango() throws Exception {
        mockMvc.perform(post("/api/resenas").contentType("application/json")
                        .content("{\"calificacionGeneral\":6,\"idEstudiante\":10,\"idClase\":7,\"anonima\":false}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(resenaService);
    }

    @Test
    void crearRechazaJsonNoValidoComoSolicitudInvalida() throws Exception {
        mockMvc.perform(post("/api/resenas").contentType("application/json").content("{"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(resenaService);
    }
}
