package com.duoc.ms_recetas.controller;

import com.duoc.ms_recetas.exceptions.ResourceNotFoundException;
import com.duoc.ms_recetas.model.recetas;
import com.duoc.ms_recetas.service.recetasService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(recetasController.class)
class recetasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private recetasService service;

    @Test
    void listarRecetas_retornaLista() throws Exception {
        when(service.listarRecetas()).thenReturn(List.of(new recetas()));

        mockMvc.perform(get("/redsalud/v1/recetas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void buscarPorId_cuandoExiste_retornaReceta() throws Exception {
        recetas r = new recetas();
        r.setId(1L);
        when(service.buscarPorId(1L)).thenReturn(r);

        mockMvc.perform(get("/redsalud/v1/recetas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void buscarPorId_cuandoNoExiste_retorna404() throws Exception {
        when(service.buscarPorId(999L)).thenThrow(new ResourceNotFoundException("No se encontro receta con id 999"));

        mockMvc.perform(get("/redsalud/v1/recetas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_retorna201() throws Exception {
        recetas r = new recetas();
        r.setId(1L);
        r.setNombrePaciente("Juan");
        when(service.crear(any())).thenReturn(r);

        mockMvc.perform(post("/redsalud/v1/recetas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fechaEmision\":\"2026-06-15\",\"idPaciente\":1,\"nombrePaciente\":\"Juan\",\"idProfesional\":1,\"nombreProfesional\":\"Dr\",\"nombreMedicamentos\":\"Paracetamol\",\"indicacionesMedicas\":\"1 cada 8h\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombrePaciente").value("Juan"));
    }

    @Test
    void actualizar_retorna200() throws Exception {
        recetas r = new recetas();
        r.setId(1L);
        r.setNombreMedicamentos("Ibuprofeno");
        when(service.actualizar(eq(1L), any())).thenReturn(r);

        mockMvc.perform(put("/redsalud/v1/recetas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fechaEmision\":\"2026-06-15\",\"idPaciente\":1,\"nombrePaciente\":\"Juan\",\"idProfesional\":1,\"nombreProfesional\":\"Dr\",\"nombreMedicamentos\":\"Ibuprofeno\",\"indicacionesMedicas\":\"1 cada 8h\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreMedicamentos").value("Ibuprofeno"));
    }

    @Test
    void patch_retorna200() throws Exception {
        recetas r = new recetas();
        r.setId(1L);
        r.setIndicacionesMedicas("1 cada 6h");
        when(service.patch(eq(1L), any())).thenReturn(r);

        mockMvc.perform(patch("/redsalud/v1/recetas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"indicacionesMedicas\":\"1 cada 6h\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.indicacionesMedicas").value("1 cada 6h"));
    }

    @Test
    void eliminar_retorna204() throws Exception {
        mockMvc.perform(delete("/redsalud/v1/recetas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_cuandoNoExiste_retorna404() throws Exception {
        doThrow(new ResourceNotFoundException("No se encontro receta con id 999"))
                .when(service).eliminar(999L);

        mockMvc.perform(delete("/redsalud/v1/recetas/999"))
                .andExpect(status().isNotFound());
    }
}
