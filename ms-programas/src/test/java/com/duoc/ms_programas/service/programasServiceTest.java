package com.duoc.ms_programas.service;

import com.duoc.ms_programas.exceptions.ResourceNotFoundException;
import com.duoc.ms_programas.model.programas;
import com.duoc.ms_programas.repository.programasRepository;
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
class programasServiceTest {

    @Mock
    private programasRepository repository;

    @InjectMocks
    private programasService service;

    @Test
    @DisplayName("listarTodos retorna lista de programas")
    void listarTodos_retornaLista() {
        when(repository.findAll()).thenReturn(List.of(new programas()));

        List<programas> resultado = service.listarTodos();

        assertThat(resultado).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("buscarPorId retorna programa cuando existe")
    void buscarPorId_cuandoExiste_retornaPrograma() {
        programas p = new programas();
        p.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(p));

        programas resultado = service.buscarPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId lanza excepcion cuando no existe")
    void buscarPorId_cuandoNoExiste_lanzaExcepcion() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No se encontro programa con id 999");

        verify(repository).findById(999L);
    }

    @Test
    @DisplayName("crear guarda programa y retorna con id")
    void crear_guardaYRetorna() {
        programas p = new programas();
        p.setId(1L);
        p.setNombrePrograma("Vacunacion");
        when(repository.save(any(programas.class))).thenReturn(p);

        programas resultado = service.crear(p);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(repository).save(any(programas.class));
    }

    @Test
    @DisplayName("actualizar modifica campos y guarda")
    void actualizar_cuandoExiste_actualizaYGuarda() {
        programas existente = new programas();
        existente.setId(1L);
        existente.setNombrePrograma("Original");

        programas actualizado = new programas();
        actualizado.setNombrePrograma("Modificado");
        actualizado.setNombreEncargado("Enc");
        actualizado.setTipoPrograma("Preventivo");
        actualizado.setLugarPrograma("Santiago");
        actualizado.setFechaPrograma(java.time.LocalDate.now());

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        programas resultado = service.actualizar(1L, actualizado);

        assertThat(resultado.getNombrePrograma()).isEqualTo("Modificado");
        verify(repository).save(existente);
    }

    @Test
    @DisplayName("actualizar lanza excepcion cuando id no existe")
    void actualizar_cuandoNoExiste_lanzaExcepcion() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.actualizar(999L, new programas()))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar borra programa cuando existe")
    void eliminar_cuandoExiste_borra() {
        programas p = new programas();
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
