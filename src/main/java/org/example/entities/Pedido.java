package org.example.entities;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Pedido {
    private Integer idPedido;
    private Integer cantidad;
    private Integer idProducto;
    private Date fechaPedido;
    private String nombre;
}
