package org.example;

import org.example.DAO.ProductoDAO;
import org.example.entities.Producto;
import org.example.jdbc.ConexionDB;
import java.sql.*;

public class ProductoDAOImpl implements ProductoDAO {

    @Override
    public void insertarProducto(Producto producto) {
        String sql = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";

        try (Connection conexion = ConexionDB.getConnection();
            PreparedStatement preparedStatement = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, producto.getNombre());
            preparedStatement.setInt(2, producto.getPrecio());
            preparedStatement.setInt(3, producto.getStock());
            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    producto.setIdProducto(resultSet.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    @Override
    public Producto consultarProducto(Integer idProducto) {
        String sql = "SELECT * FROM productos WHERE id_producto = ?";

        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {

             preparedStatement.setInt(1, idProducto);

             try (ResultSet resultSet = preparedStatement.executeQuery()) {
                 if (resultSet.next()) {
                     Integer id = resultSet.getInt("id_producto");
                     String nombre = resultSet.getString("nombre");
                     Integer precio = resultSet.getInt("precio");
                     Integer stock = resultSet.getInt("stock");

                     return new Producto(id, nombre, precio, stock);
                 }
             }

        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        return null;
    }

    @Override
    public void actualizarStock(Integer idProducto, Integer nuevoStock) {
        String sql = "UPDATE productos SET stock = ? WHERE id_producto = ?";

        try(Connection conexion = ConexionDB.getConnection();
            PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {

            preparedStatement.setInt(1, nuevoStock);
            preparedStatement.setInt(2, idProducto);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
