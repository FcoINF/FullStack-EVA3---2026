package com.duoc.ms_pacientes.controller;

import com.duoc.ms_pacientes.model.pacientes;
import com.duoc.ms_pacientes.service.pacientesService;
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
//GET    http://localhost:8082/redsalud/v1/pacientes               -> listar todas
//GET    http://localhost:8082/redsalud/v1/pacientes/{id}          -> obtener una (404 si no existe)
//POST    http://localhost:8082/redsalud/v1/pacientes              -> crear (201 Created)
//PUT     http://localhost:8082/redsalud/v1/pacientes/{id}         -> actualizar
//DELETE  http://localhost:8082/redsalud/v1/pacientes/{id}         -> eliminar (204 No Content)
//INTREGAR EL PATCH

@RestController
@RequestMapping("/redsalud/v1/pacientes") // Se define la ruta base para todos los endpoints de este controlador

//Descripcion general del microservicio
@Tag(
        name = "Microservicio de Pacientes",
        description = "Se encarga de la gestion de los pacientes"
)
public class pacientesController {

    private final pacientesService service; // Inyección del servicio que maneja la lógica de negocio

    public pacientesController(pacientesService service){
        this.service = service;
    }

    @Operation(
            summary = "Obtiene a todos los pacientes de RedSaludPatagónica",
            description = "Retorna la lista completa de los pacientes registrados"
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
            List<pacientes> pacientes = service.listarTodos(); // Llama al servicio para obtener los pacientes
            return ResponseEntity.ok(pacientes); // Devuelve a los pacientes registrados
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar los pacientes"); // Devuelve mensaje de error
        }
    }

    @Operation(
            summary = "Permite buscar mediante el ID a los pacientes de RedSaludPatagónica",
            description = "Retorna al paciente registrado"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Paciente encontrado"),
            @ApiResponse(responseCode = "404",
                    description = "Paciente no encontrado"),
            @ApiResponse(responseCode = "400",
                    description = "Id inválido"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        try {
            pacientes pacientes = service.buscarPorId(id); // Busca pacientes por ID

            if (pacientes == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontro el paciente con el ID especificado"); // 404 si no existe
            }
            return ResponseEntity.ok().body(pacientes); // Devuelve el objeto encontrado
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error del servidor");
        }
    }


    @Operation(
            summary = "Registro de un paciente",
            description = "Permite agregar un nuevo paciente"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Paciente agregado"),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos"),
            @ApiResponse(responseCode = "500",
                    description = "Error interno al crear al paciente"
            )
    })
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody pacientes paciente){
        try {

            // Validación manual de campos que si son obligatorios
            if (paciente.getNombre() == null ||
                    paciente.getDireccion() == null ||
                    paciente.getTelefono() == null ||
                    paciente.getEmail() == null ||
                    paciente.getFechaNacimiento() == null ||
                    paciente.getResidencia() == null) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Faltan rellenar campos obligatorios");
            }
            pacientes nuevoPaciente = service.crear(paciente);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Paciente creado exitosamente");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear paciente");
        }
    }


    @Operation(
            summary = "Actualizar datos de un paciente",
            description = "Permite modificar los datos de un paciente"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Datos del paciente actualizados"),
            @ApiResponse(responseCode = "404",
                    description = "Paciente no encontrado"),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody pacientes paciente){
        pacientes actualizado = service.actualizar(id, paciente); // Actualiza los campos modificados de la entidad

        if (actualizado == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontro el ID del paciente");
        }

        return ResponseEntity.ok().body(actualizado); // Devuelve el objeto actualizado
    }


    @Operation(
            summary = "Eliminar a un paciente",
            description = "Permite eliminar a un paciente existente"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Paciente eliminado"),
            @ApiResponse(responseCode = "500",
                    description = "Error interno al eliminar paciente"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        boolean eliminado = service.eliminar(id);

        if (eliminado){
            return ResponseEntity.ok("Paciente eliminado correctamente");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Paciente no encontrado");
    }
}
