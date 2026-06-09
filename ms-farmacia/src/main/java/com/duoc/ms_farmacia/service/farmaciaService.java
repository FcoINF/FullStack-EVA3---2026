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

    public List<farmacia> listarMedicamentos() {
        log.info("Listando todos los medicamentos");
        List<farmacia> lista = repository.findAll();
        log.debug("Se encontraron {} medicamentos", lista.size());
        return lista;
    }

    public farmacia buscarPorId(Long id) {
        log.info("Buscando medicamento con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontro medicamento con id: {}", id);
                    return new ResourceNotFoundException("No se encontro medicamento con id " + id);
                });
    }

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

    public void eliminar(Long id) {
        log.info("Eliminando medicamento con id: {}", id);
        farmacia existente = buscarPorId(id);
        repository.delete(existente);
        log.info("Medicamento {} eliminado exitosamente", id);
    }
}
