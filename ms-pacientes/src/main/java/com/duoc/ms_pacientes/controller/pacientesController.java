package com.duoc.ms_pacientes.controller;

import com.duoc.ms_pacientes.model.pacientes;
import com.duoc.ms_pacientes.service.pacientesService;
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
@RequestMapping("/redsalud/v1/pacientes")
@Tag(
        name = "Microservicio de Pacientes",
        description = "Se encarga de la gestion de los pacientes"
)
public class pacientesController {

    private final pacientesService service;

    public pacientesController(pacientesService service) {
        this.service = service;
    }

    @Operation(summary = "Obtiene a todos los pacientes de RedSaludPatagonica",
            description = "Retorna la lista completa de los pacientes registrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @GetMapping
    public ResponseEntity<List<pacientes>> listarTodos() {
        log.info("GET /redsalud/v1/pacientes - listando pacientes");
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Permite buscar mediante el ID a los pacientes",
            description = "Retorna al paciente registrado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
            @ApiResponse(responseCode = "400", description = "Id invalido")
    })
    @GetMapping("/{id}")
    public ResponseEntity<pacientes> buscarPorId(@PathVariable Long id) {
        log.info("GET /redsalud/v1/pacientes/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Registro de un paciente",
            description = "Permite agregar un nuevo paciente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Paciente agregado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "500", description = "Error interno al crear al paciente")
    })
    @PostMapping
    public ResponseEntity<pacientes> crear(@Valid @RequestBody pacientes paciente) {
        log.info("POST /redsalud/v1/pacientes - creando paciente: {}", paciente.getNombre());
        pacientes nuevo = service.crear(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @Operation(summary = "Actualizar datos de un paciente",
            description = "Permite modificar los datos de un paciente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Datos del paciente actualizados"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<pacientes> actualizar(@PathVariable Long id, @RequestBody pacientes paciente) {
        log.info("PUT /redsalud/v1/pacientes/{}", id);
        return ResponseEntity.ok(service.actualizar(id, paciente));
    }

    @Operation(summary = "Eliminar a un paciente",
            description = "Permite eliminar a un paciente existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Paciente eliminado"),
            @ApiResponse(responseCode = "500", description = "Error interno al eliminar paciente")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /redsalud/v1/pacientes/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
