package com.duoc.ms_pacientes.service;

import com.duoc.ms_pacientes.exceptions.ResourceNotFoundException;
import com.duoc.ms_pacientes.model.pacientes;
import com.duoc.ms_pacientes.repository.pacientesRepository;
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
class pacientesServiceTest {

    @Mock
    private pacientesRepository repository;

    @InjectMocks
    private pacientesService service;

    @Test
    @DisplayName("listarTodos retorna lista de pacientes")
    void listarTodos_retornaLista() {
        when(repository.findAll()).thenReturn(List.of(new pacientes()));

        List<pacientes> resultado = service.listarTodos();

        assertThat(resultado).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("buscarPorId retorna paciente cuando existe")
    void buscarPorId_cuandoExiste_retornaPaciente() {
        pacientes p = new pacientes();
        p.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(p));

        pacientes resultado = service.buscarPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId lanza excepcion cuando no existe")
    void buscarPorId_cuandoNoExiste_lanzaExcepcion() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No se encontro paciente con id 999");

        verify(repository).findById(999L);
    }

    @Test
    @DisplayName("crear guarda paciente y retorna con id")
    void crear_guardaYRetorna() {
        pacientes p = new pacientes();
        p.setId(1L);
        p.setNombre("Juan");
        when(repository.save(any(pacientes.class))).thenReturn(p);

        pacientes resultado = service.crear(p);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(repository).save(any(pacientes.class));
    }

    @Test
    @DisplayName("actualizar modifica campos y guarda")
    void actualizar_cuandoExiste_actualizaYGuarda() {
        pacientes existente = new pacientes();
        existente.setId(1L);
        existente.setNombre("Original");

        pacientes actualizado = new pacientes();
        actualizado.setNombre("Modificado");
        actualizado.setDireccion("Dir");
        actualizado.setResidencia("Res");
        actualizado.setEmail("a@b.cl");
        actualizado.setTelefono("123");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        pacientes resultado = service.actualizar(1L, actualizado);

        assertThat(resultado.getNombre()).isEqualTo("Modificado");
        verify(repository).save(existente);
    }

    @Test
    @DisplayName("actualizar lanza excepcion cuando id no existe")
    void actualizar_cuandoNoExiste_lanzaExcepcion() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.actualizar(999L, new pacientes()))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar borra paciente cuando existe")
    void eliminar_cuandoExiste_borra() {
        pacientes p = new pacientes();
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
