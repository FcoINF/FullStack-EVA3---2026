package com.duoc.ms_programas.controller;

import com.duoc.ms_programas.exceptions.ResourceNotFoundException;
import com.duoc.ms_programas.model.programas;
import com.duoc.ms_programas.service.programasService;
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

@WebMvcTest(programasController.class)
class programasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private programasService service;

    @Test
    void listarTodos_retornaLista() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(new programas()));

        mockMvc.perform(get("/redsalud/v1/programas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void buscarPorId_cuandoExiste_retornaPrograma() throws Exception {
        programas p = new programas();
        p.setId(1L);
        when(service.buscarPorId(1L)).thenReturn(p);

        mockMvc.perform(get("/redsalud/v1/programas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void buscarPorId_cuandoNoExiste_retorna404() throws Exception {
        when(service.buscarPorId(999L)).thenThrow(new ResourceNotFoundException("No se encontro programa con id 999"));

        mockMvc.perform(get("/redsalud/v1/programas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_retorna201() throws Exception {
        programas p = new programas();
        p.setId(1L);
        p.setNombrePrograma("Test");
        when(service.crear(any())).thenReturn(p);

        mockMvc.perform(post("/redsalud/v1/programas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombrePrograma\":\"Test\",\"nombreEncargado\":\"Enc\",\"tipoPrograma\":\"Preventivo\",\"lugarPrograma\":\"Lugar\",\"fechaPrograma\":\"2026-07-15\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombrePrograma").value("Test"));
    }

    @Test
    void actualizar_retorna200() throws Exception {
        programas p = new programas();
        p.setId(1L);
        p.setNombrePrograma("Modificado");
        when(service.actualizar(eq(1L), any())).thenReturn(p);

        mockMvc.perform(put("/redsalud/v1/programas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombrePrograma\":\"Modificado\",\"nombreEncargado\":\"Enc\",\"tipoPrograma\":\"Educativo\",\"lugarPrograma\":\"Lugar\",\"fechaPrograma\":\"2026-07-20\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombrePrograma").value("Modificado"));
    }

    @Test
    void patch_retorna200() throws Exception {
        programas p = new programas();
        p.setId(1L);
        p.setTipoPrograma("Asistencial");
        when(service.patch(eq(1L), any())).thenReturn(p);

        mockMvc.perform(patch("/redsalud/v1/programas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tipoPrograma\":\"Asistencial\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoPrograma").value("Asistencial"));
    }

    @Test
    void eliminar_retorna204() throws Exception {
        mockMvc.perform(delete("/redsalud/v1/programas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_cuandoNoExiste_retorna404() throws Exception {
        doThrow(new ResourceNotFoundException("No se encontro programa con id 999"))
                .when(service).eliminar(999L);

        mockMvc.perform(delete("/redsalud/v1/programas/999"))
                .andExpect(status().isNotFound());
    }
}
