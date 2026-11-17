package org.example.entities;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Producto {
    private Integer idProducto;
    private String nombre;
    private Integer precio;
    private Integer stock;
}
