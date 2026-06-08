package com.duoc.ms_consultas.controller;

import com.duoc.ms_consultas.dto.ConsultasDTO;
import com.duoc.ms_consultas.model.consultas;
import com.duoc.ms_consultas.service.consultasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;


import java.util.List;

//MAPEO DE ENDPOINTS:
//GET    http://localhost:8084/redsalud/v1/consultas              -> listar todas
//GET    http://localhost:8084/redsalud/v1/consultas/{id}         -> obtener una (404 si no existe)
//POST    http://localhost:8084/redsalud/v1/consultas              -> crear (201 Created)
//PUT     http://localhost:8084/redsalud/v1/consultas/{id}         -> actualizar
//DELETE  http://localhost:8084/redsalud/v1/consultas/{id}         -> eliminar (204 No Content)
//INTREGAR EL PATCH


@RestController
@RequestMapping("/redsalud/v1/consultas")// Se define la ruta base para todos los endpoints de este controlador

//Descripcion general del microservicio
@Tag(
        name = "Microservicio de Consultas",
        description = "Se encarga de la gestion de las consultas de los pacientes"
)
public class consultasController {

    private final consultasService service;  // Inyección del servicio que maneja la lógica de negocio
    public consultasController(consultasService service){
        this.service = service;
    }

    @Operation(
            summary = "Obtiene todas las consultas de RedSaludPatagónica",
            description = "Retorna la lista completa de las consultas"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Consulta exitosa"),
            @ApiResponse(responseCode = "500",
                    description = "Error interno"
            )
    })

    @GetMapping
    public ResponseEntity<?> listarConsultas(){
        try {
            List<consultas> consultas = service.listarConsultas(); // Llama al servicio para obtener las consultas
            return ResponseEntity.ok()
                    .body(consultas); // Devuelve la lista completa
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar las consultas");
        }
    }

    @Operation(
            summary = "Permite buscar mediante el ID las consultas de RedSaludPatagónica",
            description = "Retorna las consultas registradas"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Consulta encontrada"),
            @ApiResponse(responseCode = "404",
                    description = "Consulta no encontrada"),
            @ApiResponse(responseCode = "400",
                    description = "Id inválido"
            )
    })

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        try {
            consultas consultas= service.buscarPorId(id); // Busca una consulta por ID

            if (consultas == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró la consulta con el ID especificado");
            }
            return ResponseEntity.ok().body(consultas); // Devuelve la entidad encontrada
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error del servidor");
        }
    }

    @Operation(
            summary = "Registro de una consulta",
            description = "Permite agregar una consulta nueva"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Consulta creada"),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos"),
            @ApiResponse(responseCode = "500",
                    description = "Error interno al crear la consulta"
            )
    })

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody ConsultasDTO consultaDTO){
        try {
            consultas nuevaConsulta = service.crear(consultaDTO);  // Convierte DTO en entidad y guarda
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaConsulta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al crear la consulta");
        }
    }


    @Operation(
            summary = "Actualizar una consulta",
            description = "Permite modificar los datos de la consulta"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Datos de la consulta actualizados"),
            @ApiResponse(responseCode = "404",
                    description = "Consulta no encontrada"),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos")
    })

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody consultas consulta){
        consultas actualizado = service.actualizar(id, consulta); // Actualiza campos de la entidad

        if (actualizado == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontro el ID de la consulta");
        }

        return ResponseEntity.ok().body(actualizado);
    }

    @Operation(
            summary = "Eliminar una consulta",
            description = "Permite eliminar una consulta existente"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "204",
                    description = "Consulta eliminada"),
            @ApiResponse(responseCode = "500",
                    description = "Error interno al eliminar la consulta"
            )
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        boolean eliminado = service.eliminar(id); // Elimina por ID

        if (eliminado){
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Consulta no encontrada");
    }
}

