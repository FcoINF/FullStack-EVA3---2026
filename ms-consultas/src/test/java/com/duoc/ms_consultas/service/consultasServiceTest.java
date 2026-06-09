package com.duoc.ms_consultas.service;

import com.duoc.ms_consultas.dto.ConsultasDTO;
import com.duoc.ms_consultas.exceptions.ResourceNotFoundException;
import com.duoc.ms_consultas.model.consultas;
import com.duoc.ms_consultas.repository.consultasRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class consultasServiceTest {

    @Mock
    private consultasRepository repository;

    @InjectMocks
    private consultasService service;

    @Test
    @DisplayName("listarConsultas retorna lista de consultas")
    void listarConsultas_retornaLista() {
        when(repository.findAll()).thenReturn(List.of(new consultas()));

        List<consultas> resultado = service.listarConsultas();

        assertThat(resultado).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("buscarPorId retorna consulta cuando existe")
    void buscarPorId_cuandoExiste_retornaConsulta() {
        consultas consulta = new consultas();
        consulta.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(consulta));

        consultas resultado = service.buscarPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId lanza excepcion cuando no existe")
    void buscarPorId_cuandoNoExiste_lanzaExcepcion() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No se encontro consulta con id 999");

        verify(repository).findById(999L);
    }

    @Test
    @DisplayName("crear mapea DTO a entidad y guarda")
    void crear_mapeaDTOyGuarda() {
        ConsultasDTO dto = new ConsultasDTO();
        dto.setNombrePaciente("Juan");
        dto.setFichaPaciente(123);
        dto.setNombreProfesional("Dra. Maria");
        dto.setFichaProfesional(456);
        dto.setRazonConsulta("Dolor");
        dto.setModalidad("Presencial");
        dto.setFechaConsulta(LocalDateTime.now());

        consultas entity = new consultas();
        entity.setId(1L);
        when(repository.save(any(consultas.class))).thenReturn(entity);

        consultas resultado = service.crear(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(repository).save(any(consultas.class));
    }

    @Test
    @DisplayName("actualizar modifica campos y guarda")
    void actualizar_cuandoExiste_actualizaYGuarda() {
        consultas existente = new consultas();
        existente.setId(1L);
        existente.setNombrePaciente("Original");

        consultas actualizada = new consultas();
        actualizada.setNombrePaciente("Modificado");
        actualizada.setFichaPaciente(123);
        actualizada.setNombreProfesional("Dr.");
        actualizada.setFichaProfesional(456);
        actualizada.setRazonConsulta("Control");
        actualizada.setModalidad("Remota");
        actualizada.setFechaConsulta(LocalDateTime.now());

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        consultas resultado = service.actualizar(1L, actualizada);

        assertThat(resultado.getNombrePaciente()).isEqualTo("Modificado");
        verify(repository).save(existente);
    }

    @Test
    @DisplayName("actualizar lanza excepcion cuando id no existe")
    void actualizar_cuandoNoExiste_lanzaExcepcion() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.actualizar(999L, new consultas()))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar borra consulta cuando existe")
    void eliminar_cuandoExiste_borra() {
        consultas consulta = new consultas();
        consulta.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(consulta));

        service.eliminar(1L);

        verify(repository).delete(consulta);
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
