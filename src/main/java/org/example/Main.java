package org.example;

import org.example.DAO.PedidoDAO;
import org.example.DAO.ProductoDAO;
import org.example.entities.Pedido;
import org.example.entities.Producto;
import org.example.jdbc.ConexionDB;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {

    static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Boolean salir = false;

        System.out.println("Bienvenido al gestor JDBC.");

        while (!salir) {
            System.out.println("Elige una opción:" +
                    "\n1. Insertar producto" +
                    "\n2. Consultar producto por ID" +
                    "\n3. Actualizar stock de producto" +
                    "\n4. Insertar pedido" +
                    "\n5. Listado de pedidos" +
                    "\n6. Salir");

            try (Connection conexion = ConexionDB.getConnection()){
                int input = scanner.nextInt();
                switch (input) {
                    case 1 -> {
                        System.out.println("Introduce el nombre del producto: ");
                        String nombre = scanner.next();

                        System.out.println("Introduce el precio del producto: ");
                        Integer precio = scanner.nextInt();

                        System.out.println("Introduce el Stock inicial del producto: ");
                        Integer stock = scanner.nextInt();

                        ProductoDAO productoDAO = new ProductoDAOImpl();

                        Producto nuevoProducto = new Producto();
                        nuevoProducto.setNombre(nombre);
                        nuevoProducto.setPrecio(precio);
                        nuevoProducto.setStock(stock);

                        productoDAO.insertarProducto(nuevoProducto);

                        System.out.println("Producto añadido.");
                    }

                    case 2 -> {
                        System.out.println("Introduce el ID del producto a consultar: ");
                        Integer id = scanner.nextInt();

                        ProductoDAO productoDAO = new ProductoDAOImpl();
                        System.out.println(productoDAO.consultarProducto(id));
                    }

                    case 3 -> {
                        System.out.println("Introduce el ID del producto a modificar: ");
                        Integer id = scanner.nextInt();

                        System.out.println("Introduce la nueva cantidad de stock: ");
                        Integer stock = scanner.nextInt();

                        ProductoDAO productoDAO = new ProductoDAOImpl();
                        productoDAO.actualizarStock(id, stock);

                        System.out.println("Stock actualizado.");
                    }

                    case 4 -> {
                        System.out.println("Introduce el ID del producto que quieres pedir: ");
                        Integer id = scanner.nextInt();

                        System.out.println("Introduce la cantidad que deseas pedir: ");
                        Integer cantidad = scanner.nextInt();

                        PedidoDAO pedidoDAO = new PedidoDAOImpl();

                        Pedido pedido = new Pedido();
                        pedido.setIdProducto(id);
                        pedido.setCantidad(cantidad);

                        pedidoDAO.nuevoPedido(pedido);
                    }

                    case 5 -> {
                        PedidoDAO pedidoDAO = new PedidoDAOImpl();
                        List<Pedido> listado = pedidoDAO.listarPedidos();
                        for (Pedido item : listado) {
                            System.out.println(item);
                        }
                    }

                    case 6 -> {
                        salir = true;
                        System.out.println("Hasta la vista!");
                    }
                    default -> System.out.println("El comando no se encuentra en la lista.");
                }

            } catch (InputMismatchException e) {
                System.out.println("El valor introducido no es un número.");
                scanner.nextLine();
            } catch (SQLException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }

    }
}
