package com.duoc.ms_recetas.service;

import com.duoc.ms_recetas.exceptions.ResourceNotFoundException;
import com.duoc.ms_recetas.model.recetas;
import com.duoc.ms_recetas.repository.recetasRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class recetasService {

    private final recetasRepository repository;

    public recetasService(recetasRepository repository) {
        this.repository = repository;
    }

    public recetas crear(recetas receta) {
        log.info("Creando nueva receta para paciente: {}", receta.getNombrePaciente());
        recetas creada = repository.save(receta);
        log.info("Receta creada exitosamente con id: {}", creada.getId());
        return creada;
    }

    public List<recetas> listarRecetas() {
        log.info("Listando todas las recetas");
        List<recetas> lista = repository.findAll();
        log.debug("Se encontraron {} recetas", lista.size());
        return lista;
    }

    public recetas buscarPorId(Long id) {
        log.info("Buscando receta con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontro receta con id: {}", id);
                    return new ResourceNotFoundException("No se encontro receta con id " + id);
                });
    }

    public recetas actualizar(Long id, recetas receta) {
        log.info("Actualizando receta con id: {}", id);
        recetas r = buscarPorId(id);
        r.setIdPaciente(receta.getIdPaciente());
        r.setNombrePaciente(receta.getNombrePaciente());
        r.setIdProfesional(receta.getIdProfesional());
        r.setNombreProfesional(receta.getNombreProfesional());
        r.setFechaEmision(receta.getFechaEmision());
        r.setNombreMedicamentos(receta.getNombreMedicamentos());
        r.setIndicacionesMedicas(receta.getIndicacionesMedicas());
        recetas guardada = repository.save(r);
        log.info("Receta {} actualizada exitosamente", id);
        return guardada;
    }

    public void eliminar(Long id) {
        log.info("Eliminando receta con id: {}", id);
        recetas existente = buscarPorId(id);
        repository.delete(existente);
        log.info("Receta {} eliminada exitosamente", id);
    }
}
