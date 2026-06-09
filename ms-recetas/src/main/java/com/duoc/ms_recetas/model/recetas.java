package com.duoc.ms_recetas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "recetas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Receta",
        description = "Representa una receta médica registrada en RedSaludPatagónica"
)
public class recetas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autoincremental
    @Schema(
            title = "Identificador único de la receta",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Column(name = "fechaEmision", nullable = false)
    @Schema(
            description = "Fecha de emisión de la receta",
            example = "2026-06-09"
    )
    private LocalDate fechaEmision;

    @Column(name = "idPaciente", nullable = false, length = 100)
    @Schema(
            description = "Identificador único del paciente",
            example = "12345"
    )
    private Integer idPaciente;

    @Column(name = "nombrePaciente", nullable = false)
    @Schema(
            description = "Nombre completo del paciente",
            example = "Benjamín Aguero"
    )
    private String nombrePaciente;

    @Column(name = "idProfesional", nullable = false)
    @Schema(
            description = "Identificador único del profesional",
            example = "67890"
    )
    private Integer idProfesional;

    @Column(name = "nombreProfesional", nullable = false, length = 100)
    @Schema(
            description = "Nombre completo del profesional que emite la receta",
            example = "Dr. Raúl Ferrini"
    )
    private String nombreProfesional;

    @Column(name = "nombreMedicamentos", nullable = false, length = 100)
    @Schema(
            description = "Nombre de los medicamentos prescritos",
            example = "Amoxicilina 500mg"
    )
    private String nombreMedicamentos;

    @Column(name = "indicacionesMedicas", nullable = false, length = 100)
    @Schema(
            description = "Indicaciones médicas para el paciente",
            example = "Tomar 1 cápsula cada 8 horas por 7 días"
    )
    private String indicacionesMedicas;
}
