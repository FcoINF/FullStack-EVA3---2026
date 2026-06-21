package com.duoc.ms_farmacia.controller;

import com.duoc.ms_farmacia.exceptions.ResourceNotFoundException;
import com.duoc.ms_farmacia.model.farmacia;
import com.duoc.ms_farmacia.service.farmaciaService;
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

@WebMvcTest(farmaciaController.class)
class farmaciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private farmaciaService service;

    @Test
    void listarMedicamentos_retornaLista() throws Exception {
        when(service.listarMedicamentos()).thenReturn(List.of(new farmacia()));

        mockMvc.perform(get("/redsalud/v1/farmacia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void buscarPorId_cuandoExiste_retornaMedicamento() throws Exception {
        farmacia f = new farmacia();
        f.setId(1L);
        when(service.buscarPorId(1L)).thenReturn(f);

        mockMvc.perform(get("/redsalud/v1/farmacia/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void buscarPorId_cuandoNoExiste_retorna404() throws Exception {
        when(service.buscarPorId(999L)).thenThrow(new ResourceNotFoundException("No se encontro medicamento con id 999"));

        mockMvc.perform(get("/redsalud/v1/farmacia/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_retorna201() throws Exception {
        farmacia f = new farmacia();
        f.setId(1L);
        when(service.crear(any())).thenReturn(f);

        mockMvc.perform(post("/redsalud/v1/farmacia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"medicamentos\":\"Paracetamol\",\"stockMedicamentos\":100,\"encargadoNombre\":\"Ana\",\"telefonoFarmacia\":\"123\",\"proveedor\":\"Prov\",\"telefonoProveedor\":\"456\",\"horarioFarmacia\":\"L-V 9-18\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void actualizar_retorna200() throws Exception {
        farmacia f = new farmacia();
        f.setId(1L);
        f.setMedicamentos("Modificado");
        when(service.actualizar(eq(1L), any())).thenReturn(f);

        mockMvc.perform(put("/redsalud/v1/farmacia/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"medicamentos\":\"Modificado\",\"stockMedicamentos\":50,\"encargadoNombre\":\"Ana\",\"telefonoFarmacia\":\"123\",\"proveedor\":\"Prov\",\"telefonoProveedor\":\"456\",\"horarioFarmacia\":\"L-V 9-18\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.medicamentos").value("Modificado"));
    }

    @Test
    void patch_retorna200() throws Exception {
        farmacia f = new farmacia();
        f.setId(1L);
        f.setStockMedicamentos(200);
        when(service.patch(eq(1L), any())).thenReturn(f);

        mockMvc.perform(patch("/redsalud/v1/farmacia/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stockMedicamentos\":200}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockMedicamentos").value(200));
    }

    @Test
    void eliminar_retorna204() throws Exception {
        mockMvc.perform(delete("/redsalud/v1/farmacia/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_cuandoNoExiste_retorna404() throws Exception {
        doThrow(new ResourceNotFoundException("No se encontro medicamento con id 999"))
                .when(service).eliminar(999L);

        mockMvc.perform(delete("/redsalud/v1/farmacia/999"))
                .andExpect(status().isNotFound());
    }
}
