package com.duoc.ms_pacientes.service;

import com.duoc.ms_pacientes.exceptions.ResourceNotFoundException;
import com.duoc.ms_pacientes.model.pacientes;
import com.duoc.ms_pacientes.repository.pacientesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class pacientesService {

    private final pacientesRepository repository;

    public pacientesService(pacientesRepository repository) {
        this.repository = repository;
    }

    public pacientes crear(pacientes paciente) {
        log.info("Creando nuevo paciente: {}", paciente.getNombre());
        pacientes creado = repository.save(paciente);
        log.info("Paciente creado exitosamente con id: {}", creado.getId());
        return creado;
    }

    public List<pacientes> listarTodos() {
        log.info("Listando todos los pacientes");
        List<pacientes> lista = repository.findAll();
        log.debug("Se encontraron {} pacientes", lista.size());
        return lista;
    }

    public pacientes buscarPorId(Long id) {
        log.info("Buscando paciente con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontro paciente con id: {}", id);
                    return new ResourceNotFoundException("No se encontro paciente con id " + id);
                });
    }

    public pacientes actualizar(Long id, pacientes paciente) {
        log.info("Actualizando paciente con id: {}", id);
        pacientes p = buscarPorId(id);
        p.setNombre(paciente.getNombre());
        p.setDireccion(paciente.getDireccion());
        p.setResidencia(paciente.getResidencia());
        p.setFechaNacimiento(paciente.getFechaNacimiento());
        p.setEmail(paciente.getEmail());
        p.setTelefono(paciente.getTelefono());
        p.setRut(paciente.getRut());
        pacientes guardado = repository.save(p);
        log.info("Paciente {} actualizado exitosamente", id);
        return guardado;
    }

    public pacientes patch(Long id, pacientes paciente) {
        log.info("Actualizando parcialmente paciente con id: {}", id);

        pacientes existente = buscarPorId(id);

        if (paciente.getNombre() != null) {
            existente.setNombre(paciente.getNombre());
        }
        if (paciente.getDireccion() != null) {
            existente.setDireccion(paciente.getDireccion());
        }
        if (paciente.getResidencia() != null) {
            existente.setResidencia(paciente.getResidencia());
        }
        if (paciente.getFechaNacimiento() != null) {
            existente.setFechaNacimiento(paciente.getFechaNacimiento());
        }
        if (paciente.getEmail() != null) {
            existente.setEmail(paciente.getEmail());
        }
        if (paciente.getTelefono() != null) {
            existente.setTelefono(paciente.getTelefono());
        }
        if (paciente.getRut() != null) {
            existente.setRut(paciente.getRut());
        }

        pacientes actualizado = repository.save(existente);
        log.info("Paciente actualizado parcialmente con id: {}", actualizado.getId());
        return actualizado;
    }

    public void eliminar(Long id) {
        log.info("Eliminando paciente con id: {}", id);
        pacientes existente = buscarPorId(id);
        repository.delete(existente);
        log.info("Paciente {} eliminado exitosamente", id);
    }
}
