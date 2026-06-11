package com.duoc.ms_farmacia.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FarmaciaDTO {


    private Long id; // Identificador unico del medicamen

    @NotBlank(message = "El nombre del medicamento es obligatorio")
    private String medicamentos; // Nombre del medicamento

    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stockMedicamentos; // Stock disponible

    @NotBlank(message = "El nombre del encargado es obligatorio")
    @Size(min = 2, max = 50)
    private String encargadoNombre; // Encargado de la farmacia

    @NotBlank(message = "El télefono de la farmacia es obligatorio")
    private String telefonoFarmacia;

    @NotBlank(message = "El proveedor es obligatorio")
    private String proveedor;

    @NotBlank(message = "El teléfono del proveedor es obligatorio")
    private String telefonoProveedor;

    @NotBlank(message = "El horario es obligatorio")
    private String horarioFarmacia;

}
