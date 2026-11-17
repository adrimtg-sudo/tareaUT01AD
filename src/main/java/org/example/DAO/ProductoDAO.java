package org.example.DAO;

import org.example.entities.Producto;

public interface ProductoDAO {
    void insertarProducto(Producto producto);
    Producto consultarProducto(Integer idProducto);
    void actualizarStock(Integer idProducto, Integer nuevoStock);
}
