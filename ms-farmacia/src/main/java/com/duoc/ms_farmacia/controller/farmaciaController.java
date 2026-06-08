package com.duoc.ms_farmacia.controller;

import com.duoc.ms_farmacia.dto.FarmaciaDTO;
import com.duoc.ms_farmacia.model.farmacia;
import com.duoc.ms_farmacia.service.farmaciaService;

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
//GET    http://localhost:8086/redsalud/v1/farmacia               -> listar todas
//GET    http://localhost:8086/redsalud/v1/farmacia/{id}          -> obtener una (404 si no existe)
//POST    http://localhost:8086/redsalud/v1/farmacia              -> crear (201 Created)
//PUT     http://localhost:8086/redsalud/v1/farmacia/{id}         -> actualizar
//DELETE  http://localhost:8086/redsalud/v1/farmacia/{id}         -> eliminar (204 No Content)
//Integrar el patch

@RestController
@RequestMapping("/redsalud/v1/farmacia") // Se define la ruta base para todos los endpoints de este controlador

//Descripcion general del microservicio
@Tag(
        name = "Microservicio de Farmacia",
        description = "Se encarga de la gestion de los medicamentos"
)
public class farmaciaController {

    private final farmaciaService service; // Inyección del servicio que maneja la lógica de negocio


    public farmaciaController(farmaciaService service){
        this.service = service;
    }


    @Operation(
            summary = "Obtiene todos los medicamentos de la farmacia",
            description = "Retorna la lista completa de los medicamentos"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Consulta exitosa"),
            @ApiResponse(responseCode = "500",
                    description = "Error interno"
            )
    })
    @GetMapping
    public ResponseEntity<?> listarMedicamentos(){
        try {
            List<farmacia> pacientes = service.listarMedicamentos(); // Llama al servicio para obtener los medicamentos
            return ResponseEntity.ok(pacientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar los medicamentos"); // Devuelve mensaje de error
        }
    }


    @Operation(
            summary = "Permite buscar mediante el ID los medicamentos",
            description = "Retorna los medicamentos registrados"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Medicamento encontrado"),
            @ApiResponse(responseCode = "404",
                    description = "Medicamento no encontrado"),
            @ApiResponse(responseCode = "400",
                    description = "Id inválido"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        try {
            farmacia farmacia = service.buscarPorId(id); // Busca medicamento por ID

            if (farmacia == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró la consulta con el ID especificado"); // 404 si no existe
            }
            return ResponseEntity.ok().body(farmacia); // Devuelve el objeto encontrado
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error del servidor");
        }
    }


    @Operation(
            summary = "Registro de un medicamento",
            description = "Permite agregar un medicamento nuevo"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Medicamento creado"),
            @ApiResponse(responseCode = "500",
                    description = "Error interno al crear el medicamento"
            )
    })
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody FarmaciaDTO farmaciaDTO) {
        try {
            farmacia nuevoMedicamento = service.crear(farmaciaDTO); // Convierte el DTO en entidad y la guarda
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMedicamento);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al ingresar el medicamento");
        }
    }


    @Operation(
            summary = "Actualizar un medicamento",
            description = "Permite modificar los datos de un medicamento"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Datos del medicamento actualizados"),
            @ApiResponse(responseCode = "404",
                    description = "Medicamento no encontrado"),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody FarmaciaDTO farmaciaDTO){
        try {
            farmacia actualizado = service.actualizar(id, farmaciaDTO); // Actualiza los campos modificados de la entidad

            if (actualizado == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontro el ID de la farmacia para actualizar los datos");
            }

            return ResponseEntity.ok().body(actualizado); // Devuelve el objeto actualizado

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el medicamento");
        }
    }

    @Operation(
            summary = "Eliminar un medicamento",
            description = "Permite eliminar un medicamento existente"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "204",
                    description = "Medicamento eliminado"),
            @ApiResponse(responseCode = "500",
                    description = "Error interno al eliminar el medicamento"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        boolean eliminado = service.eliminar(id); // Elimina el objeto mediante el ID

        if (eliminado){
            return ResponseEntity.ok("Medicamento eliminado correctamente");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Medicamento no encontrado");
    }

}
