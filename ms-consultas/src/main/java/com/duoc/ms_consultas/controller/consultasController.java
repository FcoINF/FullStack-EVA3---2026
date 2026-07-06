package com.duoc.ms_consultas.controller;

import com.duoc.ms_consultas.dto.ConsultasDTO;
import com.duoc.ms_consultas.model.consultas;
import com.duoc.ms_consultas.service.consultasService;
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
@RequestMapping("/redsalud/v1/consultas")
@Tag(
        name = "Microservicio de Consultas",
        description = "Se encarga de la gestion de las consultas de los pacientes"
)
public class consultasController {

    private final consultasService service;

    public consultasController(consultasService service) {
        this.service = service;
    }

    @Operation(summary = "Obtiene todas las consultas de RedSaludPatagonica",
            description = "Retorna la lista completa de las consultas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    // GET /redsalud/v1/consultas - trae todas las consultas
    @GetMapping
    public ResponseEntity<List<consultas>> listarConsultas() {
        log.info("GET /redsalud/v1/consultas - listando consultas");
        return ResponseEntity.ok(service.listarConsultas());
    }

    @Operation(summary = "Permite buscar mediante el ID las consultas",
            description = "Retorna las consultas registradas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta encontrada"),
            @ApiResponse(responseCode = "404", description = "Consulta no encontrada"),
            @ApiResponse(responseCode = "400", description = "Id invalido")
    })
    // GET /redsalud/v1/consultas/{id} - busca una consulta por su ID
    @GetMapping("/{id}")
    public ResponseEntity<consultas> buscarPorId(@PathVariable Long id) {
        log.info("GET /redsalud/v1/consultas/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Registro de una consulta",
            description = "Permite agregar una consulta nueva")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Consulta creada"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "500", description = "Error interno al crear la consulta")
    })
    // POST /redsalud/v1/consultas - agrega una consulta nueva
    @PostMapping
    public ResponseEntity<consultas> crear(@Valid @RequestBody ConsultasDTO consultaDTO) {
        log.info("POST /redsalud/v1/consultas - creando consulta");
        consultas nueva = service.crear(consultaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @Operation(summary = "Actualizar una consulta",
            description = "Permite modificar los datos de la consulta")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Datos de la consulta actualizados"),
            @ApiResponse(responseCode = "404", description = "Consulta no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    // PUT /redsalud/v1/consultas/{id} - reemplaza todos los datos de una consulta
    @PutMapping("/{id}")
    public ResponseEntity<consultas> actualizar(@PathVariable Long id, @RequestBody consultas consulta) {
        log.info("PUT /redsalud/v1/consultas/{}", id);
        return ResponseEntity.ok(service.actualizar(id, consulta));
    }

    @Operation(summary = "Actualización parcial de una consulta",
            description = "Permite modificar uno o varios campos de una consulta sin reemplazar el recurso completo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Consulta no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    // PATCH /redsalud/v1/consultas/{id} - actualiza solo algunos campos de la consulta
    @PatchMapping("/{id}")
    public ResponseEntity<consultas> patchConsulta(
            @PathVariable Long id,
            @RequestBody ConsultasDTO consultaDTO) {
        log.info("PATCH /redsalud/v1/consultas/{}", id);
        return ResponseEntity.ok(service.patch(id, consultaDTO));
    }

    @Operation(summary = "Eliminar una consulta",
            description = "Permite eliminar una consulta existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Consulta eliminada"),
            @ApiResponse(responseCode = "500", description = "Error interno al eliminar la consulta")
    })
    // DELETE /redsalud/v1/consultas/{id} - elimina una consulta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /redsalud/v1/consultas/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
