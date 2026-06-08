package com.duoc.ms_consultas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "consultas") // Mapea la clase a la tabla "consultas"
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "Consultas",
        description = "Representa una consulta médica registrada en RedSaludPatagónica"
)

public class consultas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //ID autoincremental
    @Schema(
            title = "Identificador único de la consulta",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;


    @Column(name = "nombrePaciente", nullable = false, length = 100)
    @Schema(
            description = "Nombre completo del paciente",
            example = "Juan Pérez"
    )
    private String nombrePaciente;

    @Column(name = "fichaPaciente", nullable = false)
    @Schema(
            description = "Número de ficha del paciente",
            example = "12345",
            minimum = "1"
    )
    private Integer fichaPaciente;

    @Column(name = "nombreProfesional", nullable = false, length = 100)
    @Schema(
            description = "Nombre completo del profesional de salud",
            example = "Dra. María González"
    )
    private String nombreProfesional;

    @Column(name = "fichaProfesional", nullable = false)
    @Schema(
            description = "Número de ficha del profesional",
            example = "67890",
            minimum = "1"
    )
    private Integer fichaProfesional;

    @Column(name = "razonConsulta", nullable = false, length = 100)
    @Schema(
            description = "Motivo principal de la consulta",
            example = "Dolor abdominal persistente"
    )
    private String razonConsulta;

    @Column(name = "modalidad", nullable = false, length = 50)
    @Schema(
            description = "Modalidad de la consulta",
            example = "Presencial",
            allowableValues = {"Presencial", "Remota"}
    )
    private String modalidad;

    @Column(name = "fechaConsulta", nullable = false, length = 100)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(
            description = "Fecha y hora de la consulta",
            example = "2026-06-08 14:30:00"
    )
    private LocalDateTime fechaConsulta;

}
