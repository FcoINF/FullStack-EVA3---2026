package com.example.ms_profesionales.service;

import com.example.ms_profesionales.exceptions.ResourceNotFoundException;
import com.example.ms_profesionales.model.MsProfesional;
import com.example.ms_profesionales.repository.MsProfesionalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MsProfesionalServiceTest {

    @Mock
    private MsProfesionalRepository repository;

    @InjectMocks
    private MsProfesionalService service;

    @Test
    @DisplayName("listarTodos retorna lista de profesionales")
    void listarTodos_retornaLista() {
        when(repository.findAll()).thenReturn(List.of(new MsProfesional()));

        List<MsProfesional> resultado = service.listarTodos();

        assertThat(resultado).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("buscarPorId retorna profesional cuando existe")
    void buscarPorId_cuandoExiste_retornaProfesional() {
        MsProfesional p = new MsProfesional();
        p.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(p));

        MsProfesional resultado = service.buscarPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId lanza excepcion cuando no existe")
    void buscarPorId_cuandoNoExiste_lanzaExcepcion() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No se encontro profesional con id 999");

        verify(repository).findById(999L);
    }

    @Test
    @DisplayName("guardar crea profesional y retorna con id")
    void guardar_creaYRetorna() {
        MsProfesional p = new MsProfesional();
        p.setId(1L);
        p.setNombre("Dr. Raul");
        when(repository.save(any(MsProfesional.class))).thenReturn(p);

        MsProfesional resultado = service.guardar(p);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(repository).save(any(MsProfesional.class));
    }

    @Test
    @DisplayName("actualizar modifica campos y guarda")
    void actualizar_cuandoExiste_actualizaYGuarda() {
        MsProfesional existente = new MsProfesional();
        existente.setId(1L);
        existente.setNombre("Original");

        MsProfesional actualizado = new MsProfesional();
        actualizado.setNombre("Modificado");
        actualizado.setEspecialidad("Cardiologia");
        actualizado.setCorreo("correo@cl");
        actualizado.setTelefono("123");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        MsProfesional resultado = service.actualizar(1L, actualizado);

        assertThat(resultado.getNombre()).isEqualTo("Modificado");
        verify(repository).save(existente);
    }

    @Test
    @DisplayName("actualizar lanza excepcion cuando id no existe")
    void actualizar_cuandoNoExiste_lanzaExcepcion() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.actualizar(999L, new MsProfesional()))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("patch modifica solo campos no nulos")
    void patch_conCamposParciales_actualizaSoloEsos() {
        MsProfesional existente = new MsProfesional();
        existente.setId(1L);
        existente.setNombre("Original");
        existente.setEspecialidad("Cardiologia");
        existente.setCorreo("orig@cl");
        existente.setTelefono("111");

        MsProfesional parcial = new MsProfesional();
        parcial.setEspecialidad("Neurologia");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        MsProfesional resultado = service.patch(1L, parcial);

        assertThat(resultado.getEspecialidad()).isEqualTo("Neurologia");
        assertThat(resultado.getNombre()).isEqualTo("Original");
        assertThat(resultado.getCorreo()).isEqualTo("orig@cl");
        verify(repository).save(existente);
    }

    @Test
    @DisplayName("patch con todos los campos nulos no modifica nada")
    void patch_conTodosNull_noModifica() {
        MsProfesional existente = new MsProfesional();
        existente.setId(1L);
        existente.setNombre("Original");
        existente.setEspecialidad("Cardiologia");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        MsProfesional resultado = service.patch(1L, new MsProfesional());

        assertThat(resultado.getNombre()).isEqualTo("Original");
        assertThat(resultado.getEspecialidad()).isEqualTo("Cardiologia");
        verify(repository).save(existente);
    }

    @Test
    @DisplayName("eliminar borra profesional cuando existe")
    void eliminar_cuandoExiste_borra() {
        MsProfesional p = new MsProfesional();
        p.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(p));

        service.eliminar(1L);

        verify(repository).delete(p);
    }

    @Test
    @DisplayName("eliminar lanza excepcion cuando id no existe")
    void eliminar_cuandoNoExiste_lanzaExcepcion() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.eliminar(999L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(repository, never()).delete(any());
    }
}
