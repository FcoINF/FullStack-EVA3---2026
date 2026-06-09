package com.example.ms_profesionales.controller;

import com.example.ms_profesionales.model.MsProfesional;
import com.example.ms_profesionales.service.MsProfesionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//MAPEO DE ENDPOINTS:
//GET    http://localhost:8081/redsalud/v1/profesinales               -> listar todas
//GET    http://localhost:8081/redsalud/v1/profesionales/{id}         -> obtener una (404 si no existe)
//POST    http://localhost:8081/redsalud/v1/profesionales             -> crear (201 Created)
//PUT     http://localhost:8081/redsalud/v1/profesionales/{id}        -> actualizar
//DELETE  http://localhost:8081redsalud/v1/profesionales/{id}         -> eliminar (204 No Content)
//agregar patch

@RestController
@RequestMapping("/redsalud/v1/profesionales") // Se define la ruta base para todos los endpoints de este controlador

//Descripcion general del microservicio
@Tag(
        name = "Microservicio de Profesionales",
        description = "Se encarga de la gestion de los profesionales encargados de efectuar los distintos roles dentro de RedSaludPatagónica"
)

public class MsProfesionalController {

    private final MsProfesionalService service;  // Inyección del servicio que maneja la lógica de negocio

    public MsProfesionalController(MsProfesionalService service){
        this.service = service;
    }


    @Operation(
            summary = "Obtiene a todos los profesionales de RedSaludPatagónica",
            description = "Retorna la lista completa de los profesionales registrados"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Consulta exitosa"),
            @ApiResponse(responseCode = "500",
                    description = "Error interno"
            )
    })
    @GetMapping
    public ResponseEntity<List<MsProfesional>> listar(){return ResponseEntity.ok(service.listarTodos()); // Llama al servicio para obtener a los profesionales
    }


    @Operation(
            summary = "Permite buscar mediante el ID a los profesionales de RedSaludPatagónica",
            description = "Retorna al profesional registrado"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Profesional encontrado"),
            @ApiResponse(responseCode = "404",
                    description = "Profesional no encontrado"),
            @ApiResponse(responseCode = "400",
                    description = "Id inválido"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<MsProfesional> buscarPorId(@PathVariable Long id){
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)// Si existe, devuelve 200 OK con el objeto
                .orElse(ResponseEntity.notFound().build()); // Si no existe, devuelve 404 Not Found
    }


    @Operation(
            summary = "Registro de un profesional",
            description = "Permite agregar un nuevo profesional"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Profesional agregado"),
            @ApiResponse(responseCode = "400",
                    description = "Error interno al crear al profesional"
            )
    })
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody MsProfesional profesional) {
        try {
            // Validación manual de campos obligatorios
            if (profesional.getNombre() == null ||
                    profesional.getEspecialidad() == null ||
                    profesional.getCorreo() == null) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Faltan rellenar campos obligatorios");
            }

            MsProfesional nuevo = service.guardar(profesional);  // Guarda en BD
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear al profesional");
        }
    }


    @Operation(
            summary = "Actualizar datos de un profesional",
            description = "Permite modificar los datos de un profesional"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Datos del profesional actualizados"),
            @ApiResponse(responseCode = "404",
                    description = "Profesional no encontrado"),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody MsProfesional profesional) {
        MsProfesional actualizado = service.actualizar(id, profesional); // Actualiza campos de la entidad

        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Profesional no encontrado");
        }

        return ResponseEntity.ok(actualizado); // Devuelve objeto actualizado
    }


    @Operation(
            summary = "Eliminar a un profesional",
            description = "Permite eliminar a un profesional existente"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Profesional eliminado"),
            @ApiResponse(responseCode = "",
                    description = "Error al eliminar el profesional"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            if (service.buscarPorId(id).isPresent()) {
                service.eliminar(id);
                return ResponseEntity.ok("Profesional eliminado correctamente");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Profesional no encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar al profesional");
        }
    }
}
