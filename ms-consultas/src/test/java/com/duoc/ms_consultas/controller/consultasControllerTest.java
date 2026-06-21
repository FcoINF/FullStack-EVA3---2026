package com.duoc.ms_consultas.controller;

import com.duoc.ms_consultas.exceptions.ResourceNotFoundException;
import com.duoc.ms_consultas.model.consultas;
import com.duoc.ms_consultas.service.consultasService;
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

@WebMvcTest(consultasController.class)
class consultasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private consultasService service;

    @Test
    void listarConsultas_retornaLista() throws Exception {
        when(service.listarConsultas()).thenReturn(List.of(new consultas()));

        mockMvc.perform(get("/redsalud/v1/consultas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void buscarPorId_cuandoExiste_retornaConsulta() throws Exception {
        consultas c = new consultas();
        c.setId(1L);
        when(service.buscarPorId(1L)).thenReturn(c);

        mockMvc.perform(get("/redsalud/v1/consultas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void buscarPorId_cuandoNoExiste_retorna404() throws Exception {
        when(service.buscarPorId(999L)).thenThrow(new ResourceNotFoundException("No se encontro consulta con id 999"));

        mockMvc.perform(get("/redsalud/v1/consultas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_retorna201() throws Exception {
        consultas c = new consultas();
        c.setId(1L);
        when(service.crear(any())).thenReturn(c);

        mockMvc.perform(post("/redsalud/v1/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombrePaciente\":\"Juan\",\"fichaPaciente\":1,\"nombreProfesional\":\"Dr\",\"fichaProfesional\":1,\"razonConsulta\":\"Control\",\"modalidad\":\"Presencial\",\"fechaConsulta\":\"2026-06-15T10:00:00\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void actualizar_retorna200() throws Exception {
        consultas c = new consultas();
        c.setId(1L);
        c.setRazonConsulta("Actualizado");
        when(service.actualizar(eq(1L), any())).thenReturn(c);

        mockMvc.perform(put("/redsalud/v1/consultas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombrePaciente\":\"Juan\",\"fichaPaciente\":1,\"nombreProfesional\":\"Dr\",\"fichaProfesional\":1,\"razonConsulta\":\"Actualizado\",\"modalidad\":\"Presencial\",\"fechaConsulta\":\"2026-06-15 10:00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.razonConsulta").value("Actualizado"));
    }

    @Test
    void patch_retorna200() throws Exception {
        consultas c = new consultas();
        c.setId(1L);
        c.setModalidad("Remota");
        when(service.patch(eq(1L), any())).thenReturn(c);

        mockMvc.perform(patch("/redsalud/v1/consultas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"modalidad\":\"Remota\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modalidad").value("Remota"));
    }

    @Test
    void eliminar_retorna204() throws Exception {
        mockMvc.perform(delete("/redsalud/v1/consultas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_cuandoNoExiste_retorna404() throws Exception {
        doThrow(new ResourceNotFoundException("No se encontro consulta con id 999"))
                .when(service).eliminar(999L);

        mockMvc.perform(delete("/redsalud/v1/consultas/999"))
                .andExpect(status().isNotFound());
    }
}
