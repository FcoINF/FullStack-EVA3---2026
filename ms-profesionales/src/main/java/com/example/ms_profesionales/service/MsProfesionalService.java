package com.example.ms_profesionales.service;

import com.example.ms_profesionales.exceptions.ResourceNotFoundException;
import com.example.ms_profesionales.model.MsProfesional;
import com.example.ms_profesionales.repository.MsProfesionalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MsProfesionalService {

    private final MsProfesionalRepository repository;

    public MsProfesionalService(MsProfesionalRepository repository) {
        this.repository = repository;
    }

    public List<MsProfesional> listarTodos() {
        log.info("Listando todos los profesionales");
        List<MsProfesional> lista = repository.findAll();
        log.debug("Se encontraron {} profesionales", lista.size());
        return lista;
    }

    public MsProfesional buscarPorId(Long id) {
        log.info("Buscando profesional con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontro profesional con id: {}", id);
                    return new ResourceNotFoundException("No se encontro profesional con id " + id);
                });
    }

    public MsProfesional guardar(MsProfesional profesional) {
        log.info("Creando nuevo profesional: {}", profesional.getNombre());
        MsProfesional creado = repository.save(profesional);
        log.info("Profesional creado exitosamente con id: {}", creado.getId());
        return creado;
    }

    public MsProfesional actualizar(Long id, MsProfesional profesional) {
        log.info("Actualizando profesional con id: {}", id);
        MsProfesional p = buscarPorId(id);
        p.setNombre(profesional.getNombre());
        p.setEspecialidad(profesional.getEspecialidad());
        p.setCorreo(profesional.getCorreo());
        p.setTelefono(profesional.getTelefono());
        MsProfesional guardado = repository.save(p);
        log.info("Profesional {} actualizado exitosamente", id);
        return guardado;
    }

    public MsProfesional patch(Long id, MsProfesional profesional) {
        log.info("Actualizando parcialmente profesional con id: {}", id);

        MsProfesional existente = buscarPorId(id);

        if (profesional.getNombre() != null) {
            existente.setNombre(profesional.getNombre());
        }
        if (profesional.getEspecialidad() != null) {
            existente.setEspecialidad(profesional.getEspecialidad());
        }
        if (profesional.getCorreo() != null) {
            existente.setCorreo(profesional.getCorreo());
        }
        if (profesional.getTelefono() != null) {
            existente.setTelefono(profesional.getTelefono());
        }

        MsProfesional actualizado = repository.save(existente);
        log.info("Profesional actualizado parcialmente con id: {}", actualizado.getId());
        return actualizado;
    }

    public void eliminar(Long id) {
        log.info("Eliminando profesional con id: {}", id);
        MsProfesional existente = buscarPorId(id);
        repository.delete(existente);
        log.info("Profesional {} eliminado exitosamente", id);
    }
}
