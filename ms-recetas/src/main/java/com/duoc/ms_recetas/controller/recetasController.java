package com.duoc.ms_recetas.controller;

import com.duoc.ms_recetas.model.recetas;
import com.duoc.ms_recetas.service.recetasService;
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
@RequestMapping("/redsalud/v1/recetas")
@Tag(
        name = "Microservicio de recetas",
        description = "Se encarga de la gestion de las recetas"
)
public class recetasController {

    private final recetasService service;

    public recetasController(recetasService service) {
        this.service = service;
    }

    @Operation(summary = "Obtiene todas las recetas asignadas de RedSaludPatagonica",
            description = "Retorna la lista completa de las recetas registradas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @GetMapping
    public ResponseEntity<List<recetas>> listarRecetas() {
        log.info("GET /redsalud/v1/recetas - listando recetas");
        return ResponseEntity.ok(service.listarRecetas());
    }

    @Operation(summary = "Permite buscar mediante el ID las recetas",
            description = "Retorna la receta registrada")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receta encontrada"),
            @ApiResponse(responseCode = "404", description = "Receta no encontrada"),
            @ApiResponse(responseCode = "400", description = "Id invalido")
    })
    @GetMapping("/{id}")
    public ResponseEntity<recetas> buscarPorId(@PathVariable Long id) {
        log.info("GET /redsalud/v1/recetas/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Registro de una receta",
            description = "Permite agregar una nueva receta")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Receta creada"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "500", description = "Error interno al crear la receta")
    })
    @PostMapping
    public ResponseEntity<recetas> crear(@Valid @RequestBody recetas receta) {
        log.info("POST /redsalud/v1/recetas - creando receta");
        recetas nueva = service.crear(receta);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @Operation(summary = "Actualizar datos de una receta",
            description = "Permite modificar los datos de una receta")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Datos de la receta actualizados"),
            @ApiResponse(responseCode = "404", description = "Receta no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<recetas> actualizar(@PathVariable Long id, @RequestBody recetas receta) {
        log.info("PUT /redsalud/v1/recetas/{}", id);
        return ResponseEntity.ok(service.actualizar(id, receta));
    }

    @Operation(summary = "Eliminar una receta",
            description = "Permite eliminar una receta existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Receta eliminada"),
            @ApiResponse(responseCode = "500", description = "Error interno al eliminar la receta")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /redsalud/v1/recetas/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
