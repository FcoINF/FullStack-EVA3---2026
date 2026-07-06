package com.duoc.ms_consultas.service;

import com.duoc.ms_consultas.dto.ConsultasDTO;
import com.duoc.ms_consultas.exceptions.ResourceNotFoundException;
import com.duoc.ms_consultas.model.consultas;
import com.duoc.ms_consultas.repository.consultasRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class consultasService {

    private final consultasRepository repository;

    public consultasService(consultasRepository repository) {
        this.repository = repository;
    }

    // Guarda una nueva consulta en la BD
    public consultas crear(ConsultasDTO dto) {
        log.info("Creando nueva consulta para paciente: {}", dto.getFichaPaciente());
        consultas entity = new consultas();
        entity.setNombrePaciente(dto.getNombrePaciente());
        entity.setFichaPaciente(dto.getFichaPaciente());
        entity.setNombreProfesional(dto.getNombreProfesional());
        entity.setFichaProfesional(dto.getFichaProfesional());
        entity.setRazonConsulta(dto.getRazonConsulta());
        entity.setFechaConsulta(dto.getFechaConsulta());
        entity.setModalidad(dto.getModalidad());
        consultas creada = repository.save(entity);
        log.info("Consulta creada exitosamente con id: {}", creada.getId());
        return creada;
    }

    // Trae todas las consultas de la BD
    public List<consultas> listarConsultas() {
        log.info("Listando todas las consultas");
        List<consultas> consultas = repository.findAll();
        log.debug("Se encontraron {} consultas", consultas.size());
        return consultas;
    }

    // Busca una consulta por su ID, si no existe lanza error
    public consultas buscarPorId(Long id) {
        log.info("Buscando consulta con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontro consulta con id: {}", id);
                    return new ResourceNotFoundException("No se encontro consulta con id " + id);
                });
    }

    // Reemplaza todos los datos de una consulta existente
    public consultas actualizar(Long id, consultas consultaActualizada) {
        log.info("Actualizando consulta con id: {}", id);
        consultas existente = buscarPorId(id);
        existente.setNombrePaciente(consultaActualizada.getNombrePaciente());
        existente.setFichaPaciente(consultaActualizada.getFichaPaciente());
        existente.setNombreProfesional(consultaActualizada.getNombreProfesional());
        existente.setFichaProfesional(consultaActualizada.getFichaProfesional());
        existente.setRazonConsulta(consultaActualizada.getRazonConsulta());
        existente.setFechaConsulta(consultaActualizada.getFechaConsulta());
        existente.setModalidad(consultaActualizada.getModalidad());
        consultas guardada = repository.save(existente);
        log.info("Consulta actualizada exitosamente con id: {}", guardada.getId());
        return guardada;
    }

    // Actualiza solo los campos que vienen con datos (no nulos) de una consulta
    public consultas patch(Long id, ConsultasDTO dto) {
        log.info("Actualizando parcialmente consulta con id: {}", id);

        consultas existente = buscarPorId(id);
        if (dto.getNombrePaciente() != null) {
            existente.setNombrePaciente(dto.getNombrePaciente());
        }
        if (dto.getFichaPaciente() != null) {
            existente.setFichaPaciente(dto.getFichaPaciente());
        }
        if (dto.getNombreProfesional() != null) {
            existente.setNombreProfesional(dto.getNombreProfesional());
        }
        if (dto.getFichaProfesional() != null) {
            existente.setFichaProfesional(dto.getFichaProfesional());
        }
        if (dto.getRazonConsulta() != null) {
            existente.setRazonConsulta(dto.getRazonConsulta());
        }
        if (dto.getFechaConsulta() != null) {
            existente.setFechaConsulta(dto.getFechaConsulta());
        }
        if (dto.getModalidad() != null) {
            existente.setModalidad(dto.getModalidad());
        }

        consultas actualizada = repository.save(existente);
        log.info("Consulta actualizada parcialmente con id: {}", actualizada.getId());
        return actualizada;
    }

    // Elimina una consulta de la BD
    public void eliminar(Long id) {
        log.info("Eliminando consulta con id: {}", id);
        consultas existente = buscarPorId(id);
        repository.delete(existente);
        log.info("Consulta {} eliminada exitosamente", id);
    }
}
