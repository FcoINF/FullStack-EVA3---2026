package com.duoc.ms_farmacia.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "farmacia") // Mapea la clase a la tabla "farmacia"
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "Farmacia",
        description = "Representa un registro de un medicamento"
)
public class farmacia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autoincremental
    @Schema(
            title = "Identificador único del medicamento",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Column(name = "medicamentos", nullable = false, length = 200)
    @Schema(
            description = "Nombre del medicamento",
            example = "Paracetamol 500mg"
    )
    private String medicamentos;

    @Column(name = "stockMedicamentos", nullable = false)
    @Schema(
            description = "Cantidad disponible en stock",
            example = "120",
            minimum = "1"
    )
    private int stockMedicamentos;

    @Column(name = "encargadoNombre", nullable = false, length = 100)
    @Schema(
            description = "Nombre del encargado de la farmacia",
            example = "Ana López"
    )
    private String encargadoNombre;

    @Column(name = "telefonoFarmacia", nullable = false, length = 100)
    @Schema(
            description = "Teléfono de contacto de la farmacia",
            example = "+56987654321"
    )
    private String telefonoFarmacia;

    @Column(name = "proveedor", nullable = false, length = 200)
    @Schema(
            description = "Proveedor del medicamento",
            example = "Laboratorios Chile"
    )
    private String proveedor;

    @Column(name = "telefonoProveedor", nullable = false, length = 100)
    @Schema(
            description = "Teléfono del proveedor",
            example = "+56223456789"
    )
    private String telefonoProveedor;

    @Column(name = "horarioFarmacia", nullable = false, length = 50)
    @Schema(
            description = "Horario de atención de la farmacia",
            example = "Lunes a Viernes 09:00 - 18:00"
    )
    private String horarioFarmacia;
}
