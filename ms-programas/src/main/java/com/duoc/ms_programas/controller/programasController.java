package com.duoc.ms_programas.controller;

import com.duoc.ms_programas.model.programas;
import com.duoc.ms_programas.service.programasService;
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
@RequestMapping("/redsalud/v1/programas")
@Tag(
        name = "Microservicio de programas",
        description = "Se encarga de la gestion de los programas disponibles en RedSaludPatagonica"
)
public class programasController {

    private final programasService service;

    public programasController(programasService service) {
        this.service = service;
    }

    @Operation(summary = "Obtiene a todos los programas de RedSaludPatagonica",
            description = "Retorna la lista completa de los programas registrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @GetMapping
    public ResponseEntity<List<programas>> listarTodos() {
        log.info("GET /redsalud/v1/programas - listando programas");
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Permite buscar mediante el ID a los programas",
            description = "Retorna al programa registrado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Programa encontrado"),
            @ApiResponse(responseCode = "404", description = "Programa no encontrado"),
            @ApiResponse(responseCode = "400", description = "Id invalido")
    })
    @GetMapping("/{id}")
    public ResponseEntity<programas> buscarPorId(@PathVariable Long id) {
        log.info("GET /redsalud/v1/programas/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Registro de un programa",
            description = "Permite agregar un nuevo programa")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Programa creado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "500", description = "Error interno al crear el programa")
    })
    @PostMapping
    public ResponseEntity<programas> crear(@Valid @RequestBody programas programa) {
        log.info("POST /redsalud/v1/programas - creando programa: {}", programa.getNombrePrograma());
        programas nuevo = service.crear(programa);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @Operation(summary = "Actualizar datos de un programa",
            description = "Permite modificar los datos de un programa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Datos del programa actualizados"),
            @ApiResponse(responseCode = "404", description = "Programa no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<programas> actualizar(@PathVariable Long id, @Valid @RequestBody programas programa) {
        log.info("PUT /redsalud/v1/programas/{}", id);
        return ResponseEntity.ok(service.actualizar(id, programa));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<programas> patch(
            @PathVariable Long id,
            @RequestBody programas programa) {

        log.info("PATCH /redsalud/v1/programas/{}", id);

        return ResponseEntity.ok(service.patch(id, programa));
    }

    @Operation(summary = "Eliminar a un programa",
            description = "Permite eliminar a un programa existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Programa eliminado"),
            @ApiResponse(responseCode = "500", description = "Error interno al eliminar programa")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /redsalud/v1/programas/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
