package com.duoc.ms_farmacia.service;

import com.duoc.ms_farmacia.dto.FarmaciaDTO;
import com.duoc.ms_farmacia.exceptions.ResourceNotFoundException;
import com.duoc.ms_farmacia.model.farmacia;
import com.duoc.ms_farmacia.repository.farmaciaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class farmaciaService {

    private final farmaciaRepository repository;

    public farmaciaService(farmaciaRepository repository) {
        this.repository = repository;
    }

    // Guarda un nuevo medicamento en la BD
    public farmacia crear(FarmaciaDTO dto) {
        log.info("Creando nuevo medicamento: {}", dto.getMedicamentos());
        farmacia nueva = new farmacia();
        nueva.setMedicamentos(dto.getMedicamentos());
        nueva.setStockMedicamentos(dto.getStockMedicamentos());
        nueva.setEncargadoNombre(dto.getEncargadoNombre());
        nueva.setTelefonoFarmacia(dto.getTelefonoFarmacia());
        nueva.setProveedor(dto.getProveedor());
        nueva.setTelefonoProveedor(dto.getTelefonoProveedor());
        nueva.setHorarioFarmacia(dto.getHorarioFarmacia());
        farmacia creada = repository.save(nueva);
        log.info("Medicamento creado exitosamente con id: {}", creada.getId());
        return creada;
    }

    // Trae todos los medicamentos de la BD
    public List<farmacia> listarMedicamentos() {
        log.info("Listando todos los medicamentos");
        List<farmacia> lista = repository.findAll();
        log.debug("Se encontraron {} medicamentos", lista.size());
        return lista;
    }

    // Busca un medicamento por su ID, si no existe lanza error
    public farmacia buscarPorId(Long id) {
        log.info("Buscando medicamento con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontro medicamento con id: {}", id);
                    return new ResourceNotFoundException("No se encontro medicamento con id " + id);
                });
    }

    // Reemplaza todos los datos de un medicamento existente
    public farmacia actualizar(Long id, FarmaciaDTO dto) {
        log.info("Actualizando medicamento con id: {}", id);
        farmacia f = buscarPorId(id);
        f.setMedicamentos(dto.getMedicamentos());
        f.setStockMedicamentos(dto.getStockMedicamentos());
        f.setEncargadoNombre(dto.getEncargadoNombre());
        f.setTelefonoFarmacia(dto.getTelefonoFarmacia());
        f.setProveedor(dto.getProveedor());
        f.setTelefonoProveedor(dto.getTelefonoProveedor());
        f.setHorarioFarmacia(dto.getHorarioFarmacia());
        farmacia guardada = repository.save(f);
        log.info("Medicamento {} actualizado exitosamente", id);
        return guardada;
    }

    // Actualiza solo los campos que vienen con datos (no nulos) de un medicamento
    public farmacia patch(Long id, FarmaciaDTO dto) {
        log.info("Actualizando parcialmente medicamento con id: {}", id);

        farmacia existente = buscarPorId(id);

        if (dto.getMedicamentos() != null) {
            existente.setMedicamentos(dto.getMedicamentos());
        }
        if (dto.getStockMedicamentos() != null) {
            existente.setStockMedicamentos(dto.getStockMedicamentos());
        }
        if (dto.getEncargadoNombre() != null) {
            existente.setEncargadoNombre(dto.getEncargadoNombre());
        }
        if (dto.getTelefonoFarmacia() != null) {
            existente.setTelefonoFarmacia(dto.getTelefonoFarmacia());
        }
        if (dto.getProveedor() != null) {
            existente.setProveedor(dto.getProveedor());
        }
        if (dto.getTelefonoProveedor() != null) {
            existente.setTelefonoProveedor(dto.getTelefonoProveedor());
        }
        if (dto.getHorarioFarmacia() != null) {
            existente.setHorarioFarmacia(dto.getHorarioFarmacia());
        }

        farmacia actualizada = repository.save(existente);
        log.info("Medicamento actualizado parcialmente con id: {}", actualizada.getId());
        return actualizada;
    }

    // Elimina un medicamento de la BD
    public void eliminar(Long id) {
        log.info("Eliminando medicamento con id: {}", id);
        farmacia existente = buscarPorId(id);
        repository.delete(existente);
        log.info("Medicamento {} eliminado exitosamente", id);
    }
}
