package com.duoc.ms_farmacia.service;

import com.duoc.ms_farmacia.dto.FarmaciaDTO;
import com.duoc.ms_farmacia.exceptions.ResourceNotFoundException;
import com.duoc.ms_farmacia.model.farmacia;
import com.duoc.ms_farmacia.repository.farmaciaRepository;
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
class farmaciaServiceTest {

    @Mock
    private farmaciaRepository repository;

    @InjectMocks
    private farmaciaService service;

    @Test
    @DisplayName("listarMedicamentos retorna lista")
    void listarMedicamentos_retornaLista() {
        when(repository.findAll()).thenReturn(List.of(new farmacia()));

        List<farmacia> resultado = service.listarMedicamentos();

        assertThat(resultado).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("buscarPorId retorna medicamento cuando existe")
    void buscarPorId_cuandoExiste_retornaMedicamento() {
        farmacia f = new farmacia();
        f.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(f));

        farmacia resultado = service.buscarPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId lanza excepcion cuando no existe")
    void buscarPorId_cuandoNoExiste_lanzaExcepcion() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No se encontro medicamento con id 999");

        verify(repository).findById(999L);
    }

    @Test
    @DisplayName("crear mapea DTO a entidad y guarda")
    void crear_mapeaDTOyGuarda() {
        FarmaciaDTO dto = new FarmaciaDTO();
        dto.setMedicamentos("Paracetamol");
        dto.setStockMedicamentos(100);
        dto.setEncargadoNombre("Ana");
        dto.setTelefonoFarmacia("123456789");
        dto.setProveedor("Laboratorio X");
        dto.setTelefonoProveedor("987654321");
        dto.setHorarioFarmacia("Lunes a Viernes 9-18");

        farmacia entity = new farmacia();
        entity.setId(1L);
        when(repository.save(any(farmacia.class))).thenReturn(entity);

        farmacia resultado = service.crear(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(repository).save(any(farmacia.class));
    }

    @Test
    @DisplayName("actualizar modifica campos y guarda")
    void actualizar_cuandoExiste_actualizaYGuarda() {
        farmacia existente = new farmacia();
        existente.setId(1L);
        existente.setMedicamentos("Original");

        FarmaciaDTO dto = new FarmaciaDTO();
        dto.setMedicamentos("Modificado");
        dto.setStockMedicamentos(50);
        dto.setEncargadoNombre("Ana");
        dto.setTelefonoFarmacia("123");
        dto.setProveedor("Prov");
        dto.setTelefonoProveedor("456");
        dto.setHorarioFarmacia("L-V 9-18");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        farmacia resultado = service.actualizar(1L, dto);

        assertThat(resultado.getMedicamentos()).isEqualTo("Modificado");
        verify(repository).save(existente);
    }

    @Test
    @DisplayName("actualizar lanza excepcion cuando id no existe")
    void actualizar_cuandoNoExiste_lanzaExcepcion() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.actualizar(999L, new FarmaciaDTO()))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar borra medicamento cuando existe")
    void eliminar_cuandoExiste_borra() {
        farmacia f = new farmacia();
        f.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(f));

        service.eliminar(1L);

        verify(repository).delete(f);
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
