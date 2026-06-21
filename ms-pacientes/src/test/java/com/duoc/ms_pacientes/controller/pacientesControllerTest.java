package com.duoc.ms_pacientes.controller;

import com.duoc.ms_pacientes.exceptions.ResourceNotFoundException;
import com.duoc.ms_pacientes.model.pacientes;
import com.duoc.ms_pacientes.service.pacientesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(pacientesController.class)
class pacientesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private pacientesService service;

    @Test
    void listarTodos_retornaLista() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(new pacientes()));

        mockMvc.perform(get("/redsalud/v1/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void buscarPorId_cuandoExiste_retornaPaciente() throws Exception {
        pacientes p = new pacientes();
        p.setId(1L);
        p.setNombre("Juan");
        when(service.buscarPorId(1L)).thenReturn(p);

        mockMvc.perform(get("/redsalud/v1/pacientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void buscarPorId_cuandoNoExiste_retorna404() throws Exception {
        when(service.buscarPorId(999L)).thenThrow(new ResourceNotFoundException("No se encontro paciente con id 999"));

        mockMvc.perform(get("/redsalud/v1/pacientes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_retorna201() throws Exception {
        pacientes p = new pacientes();
        p.setId(1L);
        p.setNombre("Juan");
        when(service.crear(any())).thenReturn(p);

        mockMvc.perform(post("/redsalud/v1/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Juan\",\"direccion\":\"Dir\",\"residencia\":\"Res\",\"fechaNacimiento\":\"15-08-1998\",\"email\":\"a@b.cl\",\"telefono\":\"123\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void actualizar_retorna200() throws Exception {
        pacientes p = new pacientes();
        p.setId(1L);
        p.setNombre("Modificado");
        when(service.actualizar(eq(1L), any())).thenReturn(p);

        mockMvc.perform(put("/redsalud/v1/pacientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Modificado\",\"direccion\":\"Dir\",\"residencia\":\"Res\",\"fechaNacimiento\":\"15-08-1998\",\"email\":\"a@b.cl\",\"telefono\":\"123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Modificado"));
    }

    @Test
    void patch_retorna200() throws Exception {
        pacientes p = new pacientes();
        p.setId(1L);
        p.setDireccion("Nueva Dir");
        when(service.patch(eq(1L), any())).thenReturn(p);

        mockMvc.perform(patch("/redsalud/v1/pacientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"direccion\":\"Nueva Dir\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.direccion").value("Nueva Dir"));
    }

    @Test
    void eliminar_retorna204() throws Exception {
        mockMvc.perform(delete("/redsalud/v1/pacientes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_cuandoNoExiste_retorna404() throws Exception {
        doThrow(new ResourceNotFoundException("No se encontro paciente con id 999"))
                .when(service).eliminar(999L);

        mockMvc.perform(delete("/redsalud/v1/pacientes/999"))
                .andExpect(status().isNotFound());
    }
}
