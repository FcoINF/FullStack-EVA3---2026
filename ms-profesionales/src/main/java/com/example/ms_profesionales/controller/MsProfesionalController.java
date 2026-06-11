package com.example.ms_profesionales.controller;

import com.example.ms_profesionales.model.MsProfesional;
import com.example.ms_profesionales.service.MsProfesionalService;
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
@RequestMapping("/redsalud/v1/profesionales")
@Tag(
        name = "Microservicio de Profesionales",
        description = "Se encarga de la gestion de los profesionales"
)
public class MsProfesionalController {

    private final MsProfesionalService service;

    public MsProfesionalController(MsProfesionalService service) {
        this.service = service;
    }

    @Operation(summary = "Obtiene a todos los profesionales de RedSaludPatagonica",
            description = "Retorna la lista completa de los profesionales registrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @GetMapping
    public ResponseEntity<List<MsProfesional>> listar() {
        log.info("GET /redsalud/v1/profesionales - listando profesionales");
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Permite buscar mediante el ID a los profesionales",
            description = "Retorna al profesional registrado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profesional encontrado"),
            @ApiResponse(responseCode = "404", description = "Profesional no encontrado"),
            @ApiResponse(responseCode = "400", description = "Id invalido")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MsProfesional> buscarPorId(@PathVariable Long id) {
        log.info("GET /redsalud/v1/profesionales/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Registro de un profesional",
            description = "Permite agregar un nuevo profesional")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Profesional agregado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping
    public ResponseEntity<MsProfesional> crear(@Valid @RequestBody MsProfesional profesional) {
        log.info("POST /redsalud/v1/profesionales - creando profesional: {}", profesional.getNombre());
        MsProfesional nuevo = service.guardar(profesional);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @Operation(summary = "Actualizar datos de un profesional",
            description = "Permite modificar los datos de un profesional")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Datos del profesional actualizados"),
            @ApiResponse(responseCode = "404", description = "Profesional no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MsProfesional> actualizar(@PathVariable Long id, @RequestBody MsProfesional profesional) {
        log.info("PUT /redsalud/v1/profesionales/{}", id);
        return ResponseEntity.ok(service.actualizar(id, profesional));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MsProfesional> patch(
            @PathVariable Long id,
            @RequestBody MsProfesional profesional) {

        log.info("PATCH /profesionales/{}", id);

        return ResponseEntity.ok(service.patch(id, profesional));
    }

    @Operation(summary = "Eliminar a un profesional",
            description = "Permite eliminar a un profesional existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Profesional eliminado"),
            @ApiResponse(responseCode = "500", description = "Error al eliminar el profesional")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /redsalud/v1/profesionales/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
