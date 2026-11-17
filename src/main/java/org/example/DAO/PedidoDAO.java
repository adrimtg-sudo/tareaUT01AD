package org.example.DAO;

import org.example.entities.Pedido;

import java.util.List;

public interface PedidoDAO {
    void nuevoPedido(Pedido pedido);
    List<Pedido> listarPedidos();
}
