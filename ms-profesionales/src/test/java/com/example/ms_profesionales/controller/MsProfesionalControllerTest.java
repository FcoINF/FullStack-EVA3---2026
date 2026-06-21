package com.example.ms_profesionales.controller;

import com.example.ms_profesionales.exceptions.ResourceNotFoundException;
import com.example.ms_profesionales.model.MsProfesional;
import com.example.ms_profesionales.service.MsProfesionalService;
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

@WebMvcTest(MsProfesionalController.class)
class MsProfesionalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MsProfesionalService service;

    @Test
    void listar_retornaLista() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(new MsProfesional()));

        mockMvc.perform(get("/redsalud/v1/profesionales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void buscarPorId_cuandoExiste_retornaProfesional() throws Exception {
        MsProfesional p = new MsProfesional();
        p.setId(1L);
        when(service.buscarPorId(1L)).thenReturn(p);

        mockMvc.perform(get("/redsalud/v1/profesionales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void buscarPorId_cuandoNoExiste_retorna404() throws Exception {
        when(service.buscarPorId(999L)).thenThrow(new ResourceNotFoundException("No se encontro profesional con id 999"));

        mockMvc.perform(get("/redsalud/v1/profesionales/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_retorna201() throws Exception {
        MsProfesional p = new MsProfesional();
        p.setId(1L);
        p.setNombre("Dr. Test");
        when(service.guardar(any())).thenReturn(p);

        mockMvc.perform(post("/redsalud/v1/profesionales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Dr. Test\",\"especialidad\":\"Cardiologia\",\"correo\":\"test@cl\",\"telefono\":\"123\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Dr. Test"));
    }

    @Test
    void actualizar_retorna200() throws Exception {
        MsProfesional p = new MsProfesional();
        p.setId(1L);
        p.setNombre("Modificado");
        when(service.actualizar(eq(1L), any())).thenReturn(p);

        mockMvc.perform(put("/redsalud/v1/profesionales/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Modificado\",\"especialidad\":\"Neurologia\",\"correo\":\"mod@cl\",\"telefono\":\"456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Modificado"));
    }

    @Test
    void patch_retorna200() throws Exception {
        MsProfesional p = new MsProfesional();
        p.setId(1L);
        p.setTelefono("999");
        when(service.patch(eq(1L), any())).thenReturn(p);

        mockMvc.perform(patch("/redsalud/v1/profesionales/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"telefono\":\"999\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.telefono").value("999"));
    }

    @Test
    void eliminar_retorna204() throws Exception {
        mockMvc.perform(delete("/redsalud/v1/profesionales/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_cuandoNoExiste_retorna404() throws Exception {
        doThrow(new ResourceNotFoundException("No se encontro profesional con id 999"))
                .when(service).eliminar(999L);

        mockMvc.perform(delete("/redsalud/v1/profesionales/999"))
                .andExpect(status().isNotFound());
    }
}
