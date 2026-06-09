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

    public programas crear(programas programa) {
        log.info("Creando nuevo programa: {}", programa.getNombrePrograma());
        programas creado = repository.save(programa);
        log.info("Programa creado exitosamente con id: {}", creado.getId());
        return creado;
    }

    public List<programas> listarTodos() {
        log.info("Listando todos los programas");
        List<programas> lista = repository.findAll();
        log.debug("Se encontraron {} programas", lista.size());
        return lista;
    }

    public programas buscarPorId(Long id) {
        log.info("Buscando programa con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontro programa con id: {}", id);
                    return new ResourceNotFoundException("No se encontro programa con id " + id);
                });
    }

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

    public void eliminar(Long id) {
        log.info("Eliminando programa con id: {}", id);
        programas existente = buscarPorId(id);
        repository.delete(existente);
        log.info("Programa {} eliminado exitosamente", id);
    }
}
