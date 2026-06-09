package com.duoc.ms_recetas.service;

import com.duoc.ms_recetas.exceptions.ResourceNotFoundException;
import com.duoc.ms_recetas.model.recetas;
import com.duoc.ms_recetas.repository.recetasRepository;
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
class recetasServiceTest {

    @Mock
    private recetasRepository repository;

    @InjectMocks
    private recetasService service;

    @Test
    @DisplayName("listarRecetas retorna lista de recetas")
    void listarRecetas_retornaLista() {
        when(repository.findAll()).thenReturn(List.of(new recetas()));

        List<recetas> resultado = service.listarRecetas();

        assertThat(resultado).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("buscarPorId retorna receta cuando existe")
    void buscarPorId_cuandoExiste_retornaReceta() {
        recetas r = new recetas();
        r.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(r));

        recetas resultado = service.buscarPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId lanza excepcion cuando no existe")
    void buscarPorId_cuandoNoExiste_lanzaExcepcion() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No se encontro receta con id 999");

        verify(repository).findById(999L);
    }

    @Test
    @DisplayName("crear guarda receta y retorna con id")
    void crear_guardaYRetorna() {
        recetas r = new recetas();
        r.setId(1L);
        r.setNombrePaciente("Juan");
        when(repository.save(any(recetas.class))).thenReturn(r);

        recetas resultado = service.crear(r);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(repository).save(any(recetas.class));
    }

    @Test
    @DisplayName("actualizar modifica campos y guarda")
    void actualizar_cuandoExiste_actualizaYGuarda() {
        recetas existente = new recetas();
        existente.setId(1L);
        existente.setNombrePaciente("Original");

        recetas actualizado = new recetas();
        actualizado.setNombrePaciente("Modificado");
        actualizado.setIdPaciente(123);
        actualizado.setNombreProfesional("Dr.");
        actualizado.setIdProfesional(456);
        actualizado.setNombreMedicamentos("Paracetamol");
        actualizado.setIndicacionesMedicas("1 cada 8h");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        recetas resultado = service.actualizar(1L, actualizado);

        assertThat(resultado.getNombrePaciente()).isEqualTo("Modificado");
        verify(repository).save(existente);
    }

    @Test
    @DisplayName("actualizar lanza excepcion cuando id no existe")
    void actualizar_cuandoNoExiste_lanzaExcepcion() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.actualizar(999L, new recetas()))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar borra receta cuando existe")
    void eliminar_cuandoExiste_borra() {
        recetas r = new recetas();
        r.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(r));

        service.eliminar(1L);

        verify(repository).delete(r);
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
