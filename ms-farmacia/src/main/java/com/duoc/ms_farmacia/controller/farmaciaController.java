package com.duoc.ms_farmacia.controller;

import com.duoc.ms_farmacia.dto.FarmaciaDTO;
import com.duoc.ms_farmacia.model.farmacia;
import com.duoc.ms_farmacia.service.farmaciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/redsalud/v1/farmacia")
@Tag(
        name = "Microservicio de Farmacia",
        description = "Se encarga de la gestion de los medicamentos"
)
public class farmaciaController {

    private final farmaciaService service;

    public farmaciaController(farmaciaService service) {
        this.service = service;
    }

    @Operation(summary = "Obtiene todos los medicamentos de la farmacia",
            description = "Retorna la lista completa de los medicamentos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @GetMapping
    public ResponseEntity<List<farmacia>> listarMedicamentos() {
        log.info("GET /redsalud/v1/farmacia - listando medicamentos");
        return ResponseEntity.ok(service.listarMedicamentos());
    }

    @Operation(summary = "Permite buscar mediante el ID los medicamentos",
            description = "Retorna los medicamentos registrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medicamento encontrado"),
            @ApiResponse(responseCode = "404", description = "Medicamento no encontrado"),
            @ApiResponse(responseCode = "400", description = "Id invalido")
    })
    @GetMapping("/{id}")
    public ResponseEntity<farmacia> buscarPorId(@PathVariable Long id) {
        log.info("GET /redsalud/v1/farmacia/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Registro de un medicamento",
            description = "Permite agregar un medicamento nuevo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Medicamento creado"),
            @ApiResponse(responseCode = "500", description = "Error interno al crear el medicamento")
    })
    @PostMapping
    public ResponseEntity<farmacia> crear(@Valid @RequestBody FarmaciaDTO farmaciaDTO) {
        log.info("POST /redsalud/v1/farmacia - creando medicamento");
        farmacia nuevo = service.crear(farmaciaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @Operation(summary = "Actualizar un medicamento",
            description = "Permite modificar los datos de un medicamento")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Datos del medicamento actualizados"),
            @ApiResponse(responseCode = "404", description = "Medicamento no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<farmacia> actualizar(@PathVariable Long id, @Valid @RequestBody FarmaciaDTO farmaciaDTO) {
        log.info("PUT /redsalud/v1/farmacia/{}", id);
        return ResponseEntity.ok(service.actualizar(id, farmaciaDTO));
    }

    @Operation(summary = "Eliminar un medicamento",
            description = "Permite eliminar un medicamento existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Medicamento eliminado"),
            @ApiResponse(responseCode = "500", description = "Error interno al eliminar el medicamento")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /redsalud/v1/farmacia/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
