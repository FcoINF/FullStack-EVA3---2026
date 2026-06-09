package com.example.ms_profesionales.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "profesionales") // Mapea la clase a la tabla "profesionales"
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Profesionales",
        description = "Representa a un profesional registrado en RedSaludPatagónica"
)
public class MsProfesional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autoincremental
    @Schema(
            title = "Identificador único del profesional",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;


    @Column(name = "nombre", nullable = false, length = 100)
    @Schema(
            description = "Nombre completo del profesional",
            example = "Dr. Raúl Ferrini"
    )
    private String nombre;

    @Column(name = "especialidad", nullable = false, length = 100)
    @Schema(
            description = "Especialidad médica del profesional",
            example = "Cardiología",
            allowableValues = {"Cardiología", "Pediatría", "Medicina General", "Odontología"}
    )
    private String especialidad;

    @Column(name = "correo", nullable = false, length = 150)
    @Schema(
            description = "Correo electrónico del profesional",
            example = "rau.ferrini@redsalud.cl"
    )
    private String correo;

    @Column(name = "telefono", length = 20)
    @Schema(
            description = "Teléfono de contacto del profesional",
            example = "+56912345678"
    )
    private String telefono;

}
