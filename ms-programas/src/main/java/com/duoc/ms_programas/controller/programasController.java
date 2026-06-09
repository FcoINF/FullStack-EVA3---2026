package com.duoc.ms_programas.controller;

import com.duoc.ms_programas.model.programas;
import com.duoc.ms_programas.service.programasService;

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
//GET    http://localhost:8085/redsalud/v1/programas              -> listar todas
//GET    http://localhost:8085/redsalud/v1/programas/{id}         -> obtener una (404 si no existe)
//POST    http://localhost:8085/redsalud/v1/programas             -> crear (201 Created)
//PUT     http://localhost:8085/redsalud/v1/programas/{id}        -> actualizar
//DELETE  http://localhost:8085redsalud/v1/programas/{id}         -> eliminar (204 No Content)
//agregar patch

@RestController
@RequestMapping("/redsalud/v1/programas") // Se define la ruta base para todos los endpoints de este controlador
//Descripcion general del microservicio
@Tag(
        name = "Microservicio de programas",
        description = "Se encarga de la gestion de los programas disponibles en RedSaludPatagónica"
)
public class programasController {

    private final programasService service; // Inyección del servicio que maneja la lógica de negocio

    public programasController(programasService service){
        this.service = service;
    }


    @Operation(
            summary = "Obtiene a todos los programas de RedSaludPatagónica",
            description = "Retorna la lista completa de los programas registrados"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Consulta exitosa"),
            @ApiResponse(responseCode = "500",
                    description = "Error interno"
            )
    })
    @GetMapping
    public ResponseEntity<?> listarTodos(){
        try {
            List<programas> programas = service.listarTodos(); // Llama al servicio para obtener los programas
            return ResponseEntity.ok(programas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar los programas"); // Devuelve mensaje de error
        }
    }

    @Operation(
            summary = "Permite buscar mediante el ID a los programas de RedSaludPatagónica",
            description = "Retorna al programa registrado"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Programa encontrado"),
            @ApiResponse(responseCode = "404",
                    description = "Programa no encontrado"),
            @ApiResponse(responseCode = "400",
                    description = "Id inválido"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        try {
            programas programas = service.buscarPorId(id); // Busca programa por ID

            if (programas == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontro el programa con el ID especificado"); // 404 si no existe
            }
            return ResponseEntity.ok().body(programas); // Devuelve el objeto encontrado
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error del servidor");
        }
    }

    @Operation(
            summary = "Registro de un programa",
            description = "Permite agregar un nuevo programa"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Programa creado"),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos"),
            @ApiResponse(responseCode = "500",
                    description = "Error interno al crear el programa"
            )
    })
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody programas programas){
        try {
            // Validación manual de campos obligatorios
            if (programas.getNombrePrograma() == null ||
                    programas.getNombreEncargado() == null ||
                    programas.getTipoPrograma() == null ||
                    programas.getLugarPrograma() == null ||
                    programas.getFechaPrograma() == null) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Faltan rellenar campos obligatorios");
            }

            programas nuevoPrograma = service.crear(programas);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Programa creado exitosamente");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el programa");
        }
    }

    @Operation(
            summary = "Actualizar datos de un programa",
            description = "Permite modificar los datos de un programa"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Datos del programa actualizados"),
            @ApiResponse(responseCode = "404",
                    description = "Programa no encontrado"),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody programas programas){
        programas actualizado = service.actualizar(id, programas);

        if (actualizado == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontro el programa con el ID especificado");
        }

        return ResponseEntity.ok().body(actualizado); // Devuelve el objeto actualizado
    }

    @Operation(
            summary = "Eliminar a un programa",
            description = "Permite eliminar a un programa existente"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Programa eliminado"),
            @ApiResponse(responseCode = "500",
                    description = "Error interno al eliminar programa"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        boolean eliminado = service.eliminar(id); // Elimina el objeto mediante el ID

        if (eliminado){
            return ResponseEntity.ok("Programa eliminado correctamente");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Programa no encontrado");
    }

}
