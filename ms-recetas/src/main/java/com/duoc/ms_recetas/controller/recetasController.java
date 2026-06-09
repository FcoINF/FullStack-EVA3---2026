package com.duoc.ms_recetas.controller;

import com.duoc.ms_recetas.model.recetas;
import com.duoc.ms_recetas.service.recetasService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//MAPEO DE ENDPOINTS:
//GET    http://localhost:8083/redsalud/v1/recetas               -> listar todas
//GET    http://localhost:8083/redsalud/v1/recetas/{id}          -> obtener una (404 si no existe)
//POST    http://localhost:8083/redsalud/v1/recetas              -> crear (201 Created)
//PUT     http://localhost:8083/redsalud/v1/recetas/{id}         -> actualizar
//DELETE  http://localhost:8083/redsalud/v1/recetas/{id}         -> eliminar (204 No Content)
//agregar patch

@RestController
@RequestMapping("/redsalud/v1/recetas")

//Descripcion general del microservicio
@Tag(
        name = "Microservicio de recetas",
        description = "Se encarga de la gestion de las recetas"
)
public class recetasController {

    private recetasService service; // Se define la ruta base para todos los endpoints de este controlador

    public recetasController(recetasService service) {
        this.service = service;
    }

    @Operation(
            summary = "Obtiene todas las recetas asignadas de RedSaludPatagónica",
            description = "Retorna la lista completa de las recetas registradas"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Consulta exitosa"),
            @ApiResponse(responseCode = "500",
                    description = "Error interno"
            )
    })
    @GetMapping
    public ResponseEntity<?> listarRecetas() {
        try {
            List<recetas> pacientes = service.listarRecetas(); // Llama al servicio para obtener las recetas
            return ResponseEntity.ok(pacientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar las recetas"); // Devuelve mensaje de error
        }
    }

    @Operation(
            summary = "Permite buscar mediante el ID las recetas de RedSaludPatagónica",
            description = "Retorna la receta registrada"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Receta encontrada"),
            @ApiResponse(responseCode = "404",
                    description = "Receta no encontrada"),
            @ApiResponse(responseCode = "400",
                    description = "Id inválido"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        try {
            recetas recetas = service.buscarPorId(id); // Busca una receta por ID

            if (recetas == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontro la receta con el ID especificado");
            }
            return ResponseEntity.ok().body(recetas); // Devuelve el objeto encontrado
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error del servidor");
        }
    }

    @Operation(
            summary = "Registro de una receta",
            description = "Permite agregar una nueva receta"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Receta creada"),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos"),
            @ApiResponse(responseCode = "500",
                    description = "Error interno al crear la receta"
            )
    })
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody recetas recetas){
        try {
            if (recetas.getFechaEmision() == null ||
                    recetas.getIdPaciente() == null ||
                    recetas.getNombrePaciente() == null ||
                    recetas.getIdProfesional() == null ||
                    recetas.getNombreProfesional() == null ||
                    recetas.getIndicacionesMedicas() == null ||
                    recetas.getNombreMedicamentos() == null) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Faltan rellenar campos obligatorios");
            }

            recetas nuevaReceta = service.crear(recetas);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Receta creada exitosamente");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la receta");
        }
    }

    @Operation(
            summary = "Actualizar datos de una receta",
            description = "Permite modificar los datos de una receta"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Datos de la receta actualizados"),
            @ApiResponse(responseCode = "404",
                    description = "Receta no encontrada"),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody recetas recetas){
        recetas actualizado = service.actualizar(id, recetas); // Actualiza campos de la entidad

        if (actualizado == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró la receta con el ID especificado");
        }

        return ResponseEntity.ok().body(actualizado);
    }

    @Operation(
            summary = "Eliminar una receta",
            description = "Permite eliminar una receta existente"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Receta eliminada"),
            @ApiResponse(responseCode = "500",
                    description = "Error interno al eliminar la receta"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        boolean eliminado = service.eliminar(id);

        if (eliminado){
            return ResponseEntity.ok("Receta eliminada correctamente");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Receta no encontrada");
    }




}
