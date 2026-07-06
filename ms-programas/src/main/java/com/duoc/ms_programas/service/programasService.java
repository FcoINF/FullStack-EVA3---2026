package com.duoc.ms_programas.service;

import com.duoc.ms_programas.exceptions.ResourceNotFoundException;
import com.duoc.ms_programas.model.programas;
import com.duoc.ms_programas.repository.programasRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class programasService {

    private final programasRepository repository;

    public programasService(programasRepository repository) {
        this.repository = repository;
    }

    // Guarda un nuevo programa en la BD
    public programas crear(programas programa) {
        log.info("Creando nuevo programa: {}", programa.getNombrePrograma());
        programas creado = repository.save(programa);
        log.info("Programa creado exitosamente con id: {}", creado.getId());
        return creado;
    }

    // Trae todos los programas de la BD
    public List<programas> listarTodos() {
        log.info("Listando todos los programas");
        List<programas> lista = repository.findAll();
        log.debug("Se encontraron {} programas", lista.size());
        return lista;
    }

    // Busca un programa por su ID, si no existe lanza error
    public programas buscarPorId(Long id) {
        log.info("Buscando programa con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontro programa con id: {}", id);
                    return new ResourceNotFoundException("No se encontro programa con id " + id);
                });
    }

    // Reemplaza todos los datos de un programa existente
    public programas actualizar(Long id, programas programa) {
        log.info("Actualizando programa con id: {}", id);
        programas p = buscarPorId(id);
        p.setNombrePrograma(programa.getNombrePrograma());
        p.setNombreEncargado(programa.getNombreEncargado());
        p.setTipoPrograma(programa.getTipoPrograma());
        p.setLugarPrograma(programa.getLugarPrograma());
        p.setFechaPrograma(programa.getFechaPrograma());
        programas guardado = repository.save(p);
        log.info("Programa {} actualizado exitosamente", id);
        return guardado;
    }

    // Actualiza solo los campos que vienen con datos (no nulos) de un programa
    public programas patch(Long id, programas programa) {
        log.info("Actualizando parcialmente programa con id: {}", id);

        programas existente = buscarPorId(id);

        if (programa.getNombrePrograma() != null) {
            existente.setNombrePrograma(programa.getNombrePrograma());
        }
        if (programa.getNombreEncargado() != null) {
            existente.setNombreEncargado(programa.getNombreEncargado());
        }
        if (programa.getTipoPrograma() != null) {
            existente.setTipoPrograma(programa.getTipoPrograma());
        }
        if (programa.getLugarPrograma() != null) {
            existente.setLugarPrograma(programa.getLugarPrograma());
        }
        if (programa.getFechaPrograma() != null) {
            existente.setFechaPrograma(programa.getFechaPrograma());
        }

        programas actualizado = repository.save(existente);
        log.info("Programa actualizado parcialmente con id: {}", actualizado.getId());
        return actualizado;
    }

    // Elimina un programa de la BD
    public void eliminar(Long id) {
        log.info("Eliminando programa con id: {}", id);
        programas existente = buscarPorId(id);
        repository.delete(existente);
        log.info("Programa {} eliminado exitosamente", id);
    }
}
