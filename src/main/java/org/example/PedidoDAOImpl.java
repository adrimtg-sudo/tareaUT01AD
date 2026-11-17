package org.example;

import org.example.DAO.PedidoDAO;
import org.example.entities.Pedido;
import org.example.jdbc.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {

    @Override
    public void nuevoPedido(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedidos (cantidad, id_producto) VALUES (?, ?)";
        String sqlStock = "UPDATE productos SET stock = (stock - ?) WHERE id_producto = ? AND stock >= ?";
        String sqlComprobar = "SELECT id_producto FROM productos WHERE id_producto = ?";

        try (Connection conexion = ConexionDB.getConnection()) {
            conexion.setAutoCommit(false);

            try (PreparedStatement realizarPedido = conexion.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement comprobarStock = conexion.prepareStatement(sqlStock)) {

                comprobarStock.setInt(1, pedido.getCantidad());
                comprobarStock.setInt(2, pedido.getIdProducto());
                comprobarStock.setInt(3, pedido.getCantidad());

                int filas = comprobarStock.executeUpdate();

                if (filas == 0) {
                    try (PreparedStatement comprobarProducto = conexion.prepareStatement(sqlComprobar)) {
                        comprobarProducto.setInt(1, pedido.getIdProducto());

                        try (ResultSet resultSet = comprobarProducto.executeQuery()) {
                            if (!resultSet.next()) {
                                throw new SQLException("El producto no existe.");
                            }
                        }
                    }

                    throw new SQLException("No hay stock suficiente del producto.");
                }

                realizarPedido.setInt(1, pedido.getCantidad());
                realizarPedido.setInt(2, pedido.getIdProducto());

                realizarPedido.executeUpdate();

                try(ResultSet resultSet = realizarPedido.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        pedido.setIdPedido(resultSet.getInt(1));
                    }
                }

                conexion.commit();
                System.out.println("Pedido realizado.");

            } catch (SQLException e) {
                System.out.println("ERROR: " + e.getMessage());
                conexion.rollback();
            }


        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    @Override
    public List<Pedido> listarPedidos() {
        String sql = "SELECT pedidos.id_pedido, pedidos.fecha_pedido, pedidos.cantidad, pedidos.id_producto, productos.nombre " +
                        "FROM pedidos JOIN productos ON pedidos.id_producto = productos.id_producto";
        List<Pedido> listaPedidos = new ArrayList<>();

        try (Connection conexion = ConexionDB.getConnection();
            Statement statement = conexion.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                listaPedidos.add(new Pedido(
                        resultSet.getInt("id_pedido"),
                        resultSet.getInt("cantidad"),
                        resultSet.getInt("id_producto"),
                        resultSet.getDate("fecha_pedido"),
                        resultSet.getString("nombre")
                ));
            }
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        return listaPedidos;
    }
}
