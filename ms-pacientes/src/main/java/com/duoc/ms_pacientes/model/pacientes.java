package com.duoc.ms_pacientes.model;

//Nuevo import para el proyecto
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "pacientes") // Mapea la clase a la tabla "pacientes"
@Data
@AllArgsConstructor
@NoArgsConstructor

@Schema(
        name = "Pacientes",
        description = "Representa a los pacientes registrados en RedSaludPatagónica"
)
public class pacientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Id autoincremental
    @Schema(
            title = "Identificador único del paciente",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;



    @Column(name = "nombre", nullable = false, length = 100) //Usamos colum para definir la columna y datos que queremos integrales
    @Schema(
            title = "Nombre del paciente",
            description = "Nombre del paciente registrado en RedSaludPatagónica",
            example = "Juan Pérez"
    )
    private String nombre;


    @Column(name = "direccion", nullable = false, length = 200)
    @Schema(
            title = "Dirección del paciente",
            description = "Dirección del paciente registrado en RedSaludPatagónica",
            example = "Av. Los Carrera 1234"
    )
    private String direccion;

    @Column(name = "residencia", nullable = false, length = 200)
    @Schema(
            title = "Lugar de residencia del paciente",
            description = "Lugar de residensia del paciente registrado en RedSaludPatagónica",
            example = "Santiago"
    )
    private String residencia;

    @JsonFormat(pattern = "dd-MM-yyyy") // Controla formato de fecha en JSON
    @Schema(
            title = "Fecha de nacimiento del paciente",
            description = "Fecha de nacimiento del paciente registrado en RedSaludPatagónica",
            example = "15-08-1998"
    )
    private LocalDate fechaNacimiento;


    @Column(name = "email", nullable = false, length = 100)
    @Schema(
            title = "Email del paciente",
            description = "Dirección del correo electrónico del paciente registrado en RedSaludPatagónica",
            example = "sebastian.rojas@gmail.com"
    )
    private String email;

    @Column(name = "telefono", nullable = false, length = 11)
    @Schema(
            title = "Teléfono del paciente",
            description = "Número telefónico del paciente registrado en RedSaludPatagónica",
            example = "987654321"
    )
    private String telefono;


    // Dentro del proyecto, el rut no puede ser obligatorio en el formulario, lo ponemos como un valor que puede estar vacio
    @Column(name = "rut", nullable = true, length = 10)
    @Schema(
            title = "Rut del paciente",
            description = "En caso de que paciente registrado en RedSaludPatagónica tenga rut, este podrá registrarlo, de lo contrario, como es opcional no hay problema si no tiene",
            example = "987654321"
    )
    private String rut;


}
