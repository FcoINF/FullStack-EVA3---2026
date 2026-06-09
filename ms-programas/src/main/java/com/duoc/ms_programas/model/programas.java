package com.duoc.ms_programas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "programas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Programa",
        description = "Representa un programa de salud registrado en RedSaludPatagónica"
)
public class programas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autoincremental
    @Schema(
            title = "Identificador único del programa",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Column(name = "nombrePrograma", nullable = false, length = 150)
    @Schema(
            description = "Nombre del programa de salud",
            example = "Campaña de Vacunación Influenza"
    )
    private String nombrePrograma;

    @Column(name = "nombreEncargado", nullable = false, length = 100)
    @Schema(
            description = "Nombre del encargado del programa",
            example = "Dr. Francisco Molina"
    )
    private String nombreEncargado;

    @Column(name = "tipoPrograma", nullable = false, length = 100)
    @Schema(
            description = "Tipo de programa de salud",
            example = "Preventivo",
            allowableValues = {"Preventivo", "Educativo", "Asistencial"}
    )
    private String tipoPrograma;

    @Column(name = "lugarPrograma", nullable = false, length = 150)
    @Schema(
            description = "Lugar donde se ejecuta el programa",
            example = "Centro de Salud Familiar Chiloé"
    )
    private String lugarPrograma;

    @Column(name = "fechaPrograma", nullable = false)
    @Schema(
            description = "Fecha en que se realiza el programa",
            example = "2026-07-15"
    )
    private LocalDate fechaPrograma;
}
